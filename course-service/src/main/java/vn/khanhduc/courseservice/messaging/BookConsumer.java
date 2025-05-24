package vn.khanhduc.courseservice.messaging;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import vn.khanhduc.courseservice.repository.CourseRepository;
import vn.khanhduc.event.dto.CourseEvent;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "BOOK-CONSUMER")
public class BookConsumer {

    private final ElasticsearchClient elasticsearchClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CourseRepository courseRepository;

    @KafkaListener(topics = "created-course", groupId = "course-group")
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void insertCourseToElasticSearch(CourseEvent event, Acknowledgment acknowledgment) {
        try {
            elasticsearchClient.index(i -> i.index("courses")
                    .id(event.getCourseId()).document(event));
            acknowledgment.acknowledge();
            log.info("Insert course to elastic search successfully with courseId: {}", event.getCourseId());
        } catch (IOException e) {
            log.error("Insert course to elastic search failed with courseId: {}", event.getCourseId());
            kafkaTemplate.send("failed-to-save-elastic", event.getCourseId());
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "failed-to-save-elastic", groupId = "course-group")
    @Retryable(retryFor = Exception.class, maxAttempts = 4, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void rollbackCourse(String courseId, Acknowledgment acknowledgment) {
        log.info("Rollback course with courseId: {}", courseId);
        courseRepository.deleteById(courseId);
        acknowledgment.acknowledge();
        log.info("Rollback course successfully with courseId: {}", courseId);
    }

}
