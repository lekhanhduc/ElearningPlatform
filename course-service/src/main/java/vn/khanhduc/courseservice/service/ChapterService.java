package vn.khanhduc.courseservice.service;

import vn.khanhduc.courseservice.dto.request.ChapterCreationRequest;
import vn.khanhduc.courseservice.dto.response.ChapterCreationResponse;

public interface ChapterService {
    ChapterCreationResponse create(ChapterCreationRequest request);
}
