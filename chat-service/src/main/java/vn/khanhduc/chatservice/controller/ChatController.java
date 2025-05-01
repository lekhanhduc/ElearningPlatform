package vn.khanhduc.chatservice.controller;

import vn.khanhduc.chatservice.dto.request.SendMessageRequest;
import vn.khanhduc.chatservice.dto.response.SendMessageResponse;
import vn.khanhduc.chatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/message")
    public void sendMessage(@Payload SendMessageRequest sendMessageRequest,
                            SimpMessageHeaderAccessor headerAccessor) {
        log.info("Send message: {}", sendMessageRequest);
        log.info("Message headers: {}", headerAccessor.toNativeHeaderMap());
        log.info("User in header: {}", headerAccessor.getUser());
        chatService.sendMessage(sendMessageRequest);
    }

    @GetMapping("/send")
    public String chat() {
        return "chat";
    }

    @GetMapping("/conversations/{receiverId}/messages")
    @ResponseBody
    public List<SendMessageResponse> getMessages(@PathVariable Long receiverId) {
        return chatService.getMessages(receiverId);
    }


}
