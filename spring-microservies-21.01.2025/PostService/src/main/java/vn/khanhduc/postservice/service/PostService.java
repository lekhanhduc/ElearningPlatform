package vn.khanhduc.postservice.service;

import vn.khanhduc.postservice.dto.request.PostCreationRequest;
import vn.khanhduc.postservice.dto.response.PageResponse;
import vn.khanhduc.postservice.dto.response.PostCreationResponse;
import vn.khanhduc.postservice.dto.response.PostDetailResponse;

public interface PostService {
    PostCreationResponse createPost(PostCreationRequest request);
    PageResponse<PostDetailResponse> getAllPosts(int page, int size);
}
