package vn.khanhduc.chatservice.service;

import vn.khanhduc.chatservice.exception.ChatException;
import vn.khanhduc.chatservice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CHATBOT-SERVICE")
public class ChatbotService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public Flux<String> askQuestion(String question) {

        Optional<String> principal = SecurityContextHolder.getContext().getAuthentication().getName().describeConstable();
        if(principal.isEmpty()) throw new ChatException(ErrorCode.UNAUTHENTICATED);

        String userId = principal.get();

        return chatClient.prompt()
                .user(question)
                .advisors(advisor -> advisor.param("chat_memory_conversation_id", userId))
                .stream()
                .content();
    }

    public void clearHistory() {
        chatMemory.clear(null);
    }

}