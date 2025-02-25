package com.example.postservice.controller;

import com.example.postservice.dto.request.PostCreationRequest;
import com.example.postservice.dto.response.PageResponse;
import com.example.postservice.dto.response.PostCreationResponse;
import com.example.postservice.dto.response.PostDetailResponse;
import com.example.postservice.dto.response.ResponseData;
import com.example.postservice.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/creation")
    ResponseData<PostCreationResponse> createPost(@RequestBody @Valid PostCreationRequest request) {
        return ResponseData.<PostCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .data(postService.createPost(request))
                .build();
    }

    @GetMapping
    ResponseData<PageResponse<PostDetailResponse>> getAllPosts(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        return ResponseData.<PageResponse<PostDetailResponse>>builder()
                .code(HttpStatus.OK.value())
                .data(postService.getAllPosts(page, size))
                .build();
    }

}
