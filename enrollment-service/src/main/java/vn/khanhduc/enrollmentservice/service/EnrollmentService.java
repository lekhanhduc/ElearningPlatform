package vn.khanhduc.enrollmentservice.service;

import vn.khanhduc.enrollmentservice.dto.request.EnrollmentCreationRequest;

public interface EnrollmentService {
    void create(EnrollmentCreationRequest request);
}
