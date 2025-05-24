package vn.khanhduc.chatservice.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenRouterConfiguration {

//    @Value("${openai.base-url}")
//    private String OPENROUTER_API_URL;
//
//    @Value("${openai.api-key}")
//    private String OPENROUTER_API_KEY;
//
//    @Value("${openai.model}")
//    private String MODEL;
//
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(Environment env) {
//        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
//        Properties properties = new Properties();
//        properties.setProperty("spring.ai.openai.api-key", env.getProperty("openai.api-key"));
//        properties.setProperty("spring.ai.openai.base-url", env.getProperty("openai.base-url"));
//        configurer.setProperties(properties);
//        return configurer;
//    }
//
//    @Bean
//    public OpenAiApi openAiApi() {
//        return OpenAiApi.builder()
//                .baseUrl(OPENROUTER_API_URL)
//                .apiKey(OPENROUTER_API_KEY)
//                .build();
//    }
//
//    @Bean
//    public OpenAiChatOptions openAiChatOptions() {
//        return OpenAiChatOptions.builder()
//                .model(MODEL)
//                .temperature(0.5)
//                .topP(0.95)
//                .frequencyPenalty(0.1)
//                .maxTokens(4000)
//                .seed(123)
//                .build();
//
//    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultSystem("You are a helpful AI assistant.")
                .defaultAdvisors(
                            new PromptChatMemoryAdvisor(chatMemory()),
                        new MessageChatMemoryAdvisor(chatMemory()))
                .build();
    }

    @Bean
    public InMemoryChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

}
