package vn.khanhduc.postservice.mapper;

import vn.khanhduc.postservice.dto.response.PostDetailResponse;
import vn.khanhduc.postservice.entity.Post;

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
