package vn.khanhduc.enrollmentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.khanhduc.enrollmentservice.dto.request.EnrollmentCreationRequest;
import vn.khanhduc.enrollmentservice.dto.response.ResponseData;
import vn.khanhduc.enrollmentservice.service.EnrollmentService;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/creation")
    ResponseData<Void> creation(@RequestBody EnrollmentCreationRequest request) {
        enrollmentService.create(request);
        return ResponseData.<Void>builder()
                .code(HttpStatus.CREATED.value())
                .message("Enrollment created successfully")
                .build();
    }

}
