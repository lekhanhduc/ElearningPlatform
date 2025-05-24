package vn.khanhduc.courseservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.khanhduc.courseservice.dto.request.CourseCreationRequest;
import vn.khanhduc.courseservice.dto.response.*;
import vn.khanhduc.courseservice.service.CourseService;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/creation")
    ResponseData<CourseCreationResponse> create(@RequestPart("request") @Valid CourseCreationRequest request,
                                                @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        var result = courseService.create(request, file);
        return ResponseData.<CourseCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .data(result)
                .build();
    }

    @GetMapping("/info-detail/{courserId}")
    ResponseData<CourseDetailResponse> getAllInfoCourses(@PathVariable String courserId) {
        return ResponseData.<CourseDetailResponse>builder()
                .code(HttpStatus.OK.value())
                .data(courseService.getAllDetailCourses(courserId))
                .build();
    }

    @GetMapping("/fetch-all")
    ResponseData<PageResponse<CourseResponse>> getAllCourses(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return ResponseData.<PageResponse<CourseResponse>>builder()
                .code(HttpStatus.OK.value())
                .data(courseService.getAllCourses(page, size))
                .build();
    }

    @GetMapping("/search/category")
    ResponseData<PageResponse<CourseResponse>> getAllCoursesByCategory(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String categoryName
    ) {
        return ResponseData.<PageResponse<CourseResponse>>builder()
                .code(HttpStatus.OK.value())
                .data(courseService.getAllCoursesByCategoryName(page, size, categoryName))
                .build();
    }

    @GetMapping("/{id}/exists")
    ResponseData<CourseExistsResponse> existsById(@PathVariable String id) {
        return ResponseData.<CourseExistsResponse>builder()
                .code(HttpStatus.OK.value())
                .data(courseService.existsById(id))
                .build();
    }

}
