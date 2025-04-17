package vn.khanhduc.postservice.service.impl;

import org.springframework.data.domain.Sort;
import vn.khanhduc.postservice.dto.request.PostCreationRequest;
import vn.khanhduc.postservice.dto.response.PageResponse;
import vn.khanhduc.postservice.dto.response.PostCreationResponse;
import vn.khanhduc.postservice.dto.response.PostDetailResponse;
import vn.khanhduc.postservice.exception.ErrorCode;
import vn.khanhduc.postservice.exception.PostException;
import vn.khanhduc.postservice.mapper.PostMapper;
import vn.khanhduc.postservice.entity.Post;
import vn.khanhduc.postservice.repository.PostRepository;
import vn.khanhduc.postservice.service.DateTimeFormater;
import vn.khanhduc.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

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
    public PageResponse<PostDetailResponse> getAllPostsByUserLogin(int page, int size) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = authentication.getName().describeConstable();
        if(userId.isEmpty()) {
            throw new PostException(ErrorCode.UNAUTHENTICATED);
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Post> posts = postRepository.findAllByUserId(Long.parseLong(String.valueOf(userId)), pageable);
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

    @Override
    public void deletePost(String id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = authentication.getName().describeConstable();
        if(userId.isEmpty()) {
            throw new PostException(ErrorCode.UNAUTHENTICATED);
        }
        var post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
        postRepository.delete(post);
    }

    @Override
    public PageResponse<PostDetailResponse> getAllPosts(int page, int size) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = authentication.getName().describeConstable();
        if(userId.isEmpty()) {
            throw new PostException(ErrorCode.UNAUTHENTICATED);
        }
        log.info("Get all posts");
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Post> posts = postRepository.findAll(pageable);
        List<PostDetailResponse> responses = posts.getContent()
                .stream()
                .map(PostMapper::toPostDetail)
                .toList();

        return PageResponse.<PostDetailResponse>builder()
                .currentPage(page)
                .size(pageable.getPageSize())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .data(responses)
                .build();
    }

}
