package com.example.postservice.mapper;

import com.example.postservice.dto.response.PostDetailResponse;
import com.example.postservice.model.Post;

public class PostMapper {
    private PostMapper() {}

    public static PostDetailResponse toPostDetail(Post post) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .content(post.getContent())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .build();
    }
}
