package vn.khanhduc.chatservice.service;

import vn.khanhduc.chatservice.dto.request.SendMessageRequest;
import vn.khanhduc.chatservice.dto.response.SendMessageResponse;
import vn.khanhduc.chatservice.dto.response.UserProfileResponse;
import vn.khanhduc.chatservice.entity.Conversation;
import vn.khanhduc.chatservice.entity.Message;
import vn.khanhduc.chatservice.enums.ConversationType;
import vn.khanhduc.chatservice.enums.MessageStatus;
import vn.khanhduc.chatservice.exception.ChatException;
import vn.khanhduc.chatservice.exception.ErrorCode;
import vn.khanhduc.chatservice.repository.ConversationRepository;
import vn.khanhduc.chatservice.repository.MessageRepository;
import vn.khanhduc.chatservice.repository.httpclient.ProfileClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CHAT-SERVICE")
public class ChatService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ProfileClient profileClient;

    public void sendMessage(SendMessageRequest sendMessageRequest) {
        log.info("Send message: {}", sendMessageRequest);

        var principal1 = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Principal: {}", principal1);
        Optional<String> principal = SecurityContextHolder.getContext().getAuthentication().getName().describeConstable();
        log.info("Principal: {}", principal);

        if(principal.isEmpty()) throw new ChatException(ErrorCode.UNAUTHENTICATED);

        Long senderId = Long.parseLong(principal.get());
        log.info("Sender id: {}", senderId);

        Long receiverId = sendMessageRequest.getReceiverId();

        Conversation conversation = conversationRepository
                .findBySenderIdAndReceiverId(senderId, receiverId)
                .orElseGet(() -> {
                    UserProfileResponse userProfile = profileClient.getUserProfile(receiverId);
                    log.info("User profile: {}", userProfile);
                    Conversation newConversation = Conversation.builder()
                            .conversationType(ConversationType.ONE_TO_ONE)
                            .senderId(senderId)
                            .receiverId(receiverId)
                            .name(userProfile.getFirstName() + " " + userProfile.getLastName())
                            .createdAt(LocalDateTime.now())
                            .lastMessageId(null)
                            .build();
                    return conversationRepository.save(newConversation);
                });

        Message message = Message.builder()
                .conversationId(conversation.getId())
                .senderId(senderId)
                .receiverId(receiverId)
                .content(sendMessageRequest.getContent())
                .messageType(sendMessageRequest.getMessageType())
                .messageStatus(MessageStatus.SENT)
                .createdAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);

        conversation.setLastMessageId(message.getId());
        conversationRepository.save(conversation);

        SendMessageResponse messageResponse = SendMessageResponse.builder()
                .messageId(message.getId())
                .conversationId(message.getConversationId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .messageType(String.valueOf(message.getMessageType()))
                .createdAt(message.getCreatedAt())
                .build();

        // Gửi tin nhắn đến receiver
        log.info("Sending message to receiver: /user/{}/queue/message", receiverId);
        simpMessagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/message",
                messageResponse);

        // Gửi tin nhắn đến sender
        log.info("Sending message to sender: /user/{}/queue/message", senderId);
        simpMessagingTemplate.convertAndSendToUser(
                senderId.toString(),
                "/queue/message",
                messageResponse);
    }

    public List<SendMessageResponse> getMessages(Long receiverId) {
        String senderIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        if (senderIdStr == null) {
            throw new ChatException(ErrorCode.UNAUTHENTICATED);
        }
        Long senderId = Long.parseLong(senderIdStr);

        Conversation conversation = conversationRepository
                .findBySenderIdAndReceiverId(senderId, receiverId)
                .orElseThrow(() -> new ChatException(ErrorCode.CONVERSATION_NOT_FOUND));

        List<Message> messages = messageRepository.findByConversationId(conversation.getId());

        return messages.stream().map(message -> SendMessageResponse.builder()
                        .messageId(message.getId())
                        .conversationId(message.getConversationId())
                        .senderId(message.getSenderId())
                        .receiverId(message.getReceiverId())
                        .content(message.getContent())
                        .messageType(String.valueOf(message.getMessageType()))
                        .createdAt(message.getCreatedAt())
                        .build())
                .toList();
    }


}
