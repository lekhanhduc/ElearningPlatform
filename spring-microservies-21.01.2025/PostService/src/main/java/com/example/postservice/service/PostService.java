package com.example.postservice.service;

import com.example.postservice.dto.request.PostCreationRequest;
import com.example.postservice.dto.response.PageResponse;
import com.example.postservice.dto.response.PostCreationResponse;
import com.example.postservice.dto.response.PostDetailResponse;

public interface PostService {
    PostCreationResponse createPost(PostCreationRequest request);
    PageResponse<PostDetailResponse> getAllPosts(int page, int size);
}
