package vn.khanhduc.chatservice.controller;

import vn.khanhduc.chatservice.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @GetMapping(value = "/chatbot/ask", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> askQuestion(@RequestParam(name = "question") String question) {
        return chatbotService.askQuestion(question);
    }

    @PostMapping(value = "/chatbot/clear-history")
    public Mono<Void> clearHistory() {
        chatbotService.clearHistory();
        return Mono.empty();
    }

}