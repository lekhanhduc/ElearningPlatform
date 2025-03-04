package vn.khanhduc.postservice.controller;

import vn.khanhduc.postservice.dto.request.PostCreationRequest;
import vn.khanhduc.postservice.dto.response.PageResponse;
import vn.khanhduc.postservice.dto.response.PostCreationResponse;
import vn.khanhduc.postservice.dto.response.PostDetailResponse;
import vn.khanhduc.postservice.dto.response.ResponseData;
import vn.khanhduc.postservice.service.PostService;
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
