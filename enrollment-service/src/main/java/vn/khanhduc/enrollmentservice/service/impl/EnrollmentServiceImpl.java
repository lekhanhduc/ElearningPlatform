package vn.khanhduc.enrollmentservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import vn.khanhduc.enrollmentservice.common.EnrollmentStatus;
import vn.khanhduc.enrollmentservice.common.SecurityUtils;
import vn.khanhduc.enrollmentservice.dto.request.EnrollmentCreationRequest;
import vn.khanhduc.enrollmentservice.entity.Enrollment;
import vn.khanhduc.enrollmentservice.exception.EnrollmentException;
import vn.khanhduc.enrollmentservice.exception.ErrorCode;
import vn.khanhduc.enrollmentservice.repository.EnrollmentRepository;
import vn.khanhduc.enrollmentservice.repository.httpclient.CourseClient;
import vn.khanhduc.enrollmentservice.service.EnrollmentService;
import vn.khanhduc.event.dto.PaymentEvent;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ENROLLMENT-SERVICE")
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseClient courseClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void create(EnrollmentCreationRequest request) {
        Long userId = SecurityUtils.getCurrentUser();

        var response = courseClient.existsById(request.getCourseId());
        if(!response.getData().isExists()) throw new EnrollmentException(ErrorCode.COURSE_NOT_FOUND);

        if(enrollmentRepository.checkExistByUserIdAndCourseId(userId, request.getCourseId())) {
            throw new EnrollmentException(ErrorCode.ENROLLMENT_EXISTED);
        }

        var enrollment = Enrollment.builder()
                .courseId(request.getCourseId())
                .userId(userId)
                .status(EnrollmentStatus.PENDING)
                .build();

        enrollmentRepository.save(enrollment);

        String currentTimeString = String.valueOf(new Date().getTime());
        Long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

        PaymentEvent event  = PaymentEvent.builder()
                .userId(userId)
                .orderCode(orderCode)
                .totalPrice(request.getPrice())
                .orderId(enrollment.getId())
                .build();

        kafkaTemplate.send("enrollment-pending", event);
    }

}
