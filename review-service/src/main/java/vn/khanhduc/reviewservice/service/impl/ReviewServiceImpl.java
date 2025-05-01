package vn.khanhduc.reviewservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.khanhduc.reviewservice.dto.request.ReviewCreationRequest;
import vn.khanhduc.reviewservice.dto.response.ReviewCreationResponse;
import vn.khanhduc.reviewservice.repository.ReviewRepository;
import vn.khanhduc.reviewservice.service.ReviewService;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "REVIEW-SERVICE")
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewCreationResponse create(ReviewCreationRequest request) {

        return null;
    }
}
