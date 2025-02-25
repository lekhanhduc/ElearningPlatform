package com.example.postservice.service.impl;

import com.example.postservice.dto.request.PostCreationRequest;
import com.example.postservice.dto.response.PageResponse;
import com.example.postservice.dto.response.PostCreationResponse;
import com.example.postservice.dto.response.PostDetailResponse;
import com.example.postservice.mapper.PostMapper;
import com.example.postservice.model.Post;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.service.DateTimeFormater;
import com.example.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "POST-SERVICE")
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final DateTimeFormater dateTimeFormater;

    @Override
    public PostCreationResponse createPost(PostCreationRequest request) {
        log.info("Creating new post");
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = authentication.getName();
        Post post = Post.builder()
                .content(request.getContent())
                .userId(Long.parseLong(userId))
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();
        postRepository.save(post);
        log.info("Post created");
        return PostCreationResponse.builder()
                .id(post.getId())
                .userId(Long.parseLong(userId))
                .content(post.getContent())
                .created(dateTimeFormater.format(post.getCreatedDate()))
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();
    }

    @Override
    public PageResponse<PostDetailResponse> getAllPosts(int page, int size) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = authentication.getName();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Post> posts = postRepository.findAllByUserId(Long.parseLong(userId), pageable);
        return PageResponse.<PostDetailResponse>builder()
                .currentPage(page)
                .size(size)
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .data(posts.getContent().stream().map(post -> {
                    var postResponse = PostMapper.toPostDetail(post);
                    postResponse.setCreated(dateTimeFormater.format(post.getCreatedDate()));
                    return postResponse;
                }).toList())
                .build();
    }

}
