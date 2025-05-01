package vn.khanhduc.identityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import vn.khanhduc.event.dto.ProfileCreationFailedEvent;
import vn.khanhduc.identityservice.exception.ErrorCode;
import vn.khanhduc.identityservice.exception.IdentityException;
import vn.khanhduc.identityservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "KAFKA-SERVICE")
public class UserRollbackConsumer {

    private final UserRepository userRepository;

    @KafkaListener(topics = "profile-create-failed", groupId = "identity-group")
    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 4,
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            label = "handleProfileCreationFailed")
    public void handleProfileCreationFailed(ProfileCreationFailedEvent event, Acknowledgment acknowledgment) {
        if (event.getUserId() == null) {
            log.error("Received ProfileCreationFailedEvent with null userId");
            acknowledgment.acknowledge();
            return;
        }

        if (!userRepository.existsById(event.getUserId())) {
            log.warn("User with ID {} does not exist, no rollback needed, eventId={}",
                    event.getUserId(), event.getUserId());
            acknowledgment.acknowledge();
            return;
        }
        log.info("Profile create fail with user id: {}", event.getUserId());

        try {
            userRepository.deleteById(event.getUserId());
            acknowledgment.acknowledge();
            log.info("Rollback user creation successfully");
        } catch (Exception e) {
            throw new IdentityException(ErrorCode.ROLL_BACK_ERROR);
        }
    }

}
