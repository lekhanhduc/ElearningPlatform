package vn.khanhduc.courseservice.service;

import vn.khanhduc.courseservice.dto.request.LessonCreationRequest;
import vn.khanhduc.courseservice.dto.response.LessonCreationResponse;

public interface LessonService {
    LessonCreationResponse create(LessonCreationRequest request);
}
