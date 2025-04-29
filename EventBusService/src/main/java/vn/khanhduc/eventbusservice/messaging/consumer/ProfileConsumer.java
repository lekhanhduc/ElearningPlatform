package vn.khanhduc.eventbusservice.messaging.consumer;

import vn.khanhduc.eventbusservice.common.Channel;
import vn.khanhduc.event.dto.NotificationEvent;
import vn.khanhduc.event.dto.ProfileEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "PROFILE-CONSUMER")
public class ProfileConsumer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "profile-created", groupId = "event-bus-service")
    @Retryable(maxAttempts = 3, retryFor = Exception.class, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void profileCreated(ProfileEvent profileEvent, Acknowledgment acknowledgment) {
        log.info("Profile-Consumer received event from ProfileService{}", profileEvent );

        var param = new HashMap<String, Object>();
        param.put("subject", "Welcome to Book Store");
        param.put("name", String.format("%s %s", profileEvent.getFirstName(), profileEvent.getLastName()));
        param.put("body", "Hello " + profileEvent.getFirstName() + " " + profileEvent.getLastName());

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel(Channel.EMAIL.name())
                .recipient(profileEvent.getEmail())
                .param(param)
                .build();

        kafkaTemplate.send("user-onboard-success", notificationEvent);
        acknowledgment.acknowledge();
        log.info("Profile-Consumer send event to NotificationService");
    }

}
