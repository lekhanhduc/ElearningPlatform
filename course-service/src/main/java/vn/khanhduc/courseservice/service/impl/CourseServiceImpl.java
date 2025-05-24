package vn.khanhduc.courseservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.khanhduc.courseservice.common.Level;
import vn.khanhduc.courseservice.dto.request.CourseCreationRequest;
import vn.khanhduc.courseservice.dto.response.*;
import vn.khanhduc.courseservice.entity.Course;
import vn.khanhduc.courseservice.exception.CourseException;
import vn.khanhduc.courseservice.exception.ErrorCode;
import vn.khanhduc.courseservice.mapper.CourseConverter;
import vn.khanhduc.courseservice.mapper.CourseMapper;
import vn.khanhduc.courseservice.repository.CategoryRepository;
import vn.khanhduc.courseservice.repository.CourseRepository;
import vn.khanhduc.courseservice.repository.httpclient.FileClient;
import vn.khanhduc.courseservice.service.CourseService;
import vn.khanhduc.courseservice.common.SecurityUtils;
import vn.khanhduc.event.dto.CourseEvent;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "COURSE-SERVICE")
public class CourseServiceImpl implements CourseService {

    @Value("${minio.bucket-name}")
    private String bucketName;

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final CourseMapper courseMapper;
    private final FileClient fileClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public CourseCreationResponse create(CourseCreationRequest request, MultipartFile file) throws Exception {
        Long userId = SecurityUtils.getCurrentUser();

        var category = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new CourseException(ErrorCode.CATEGORY_NOT_FOUND));

        var course = Course.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .userId(userId)
                .category(category)
                .level(Level.valueOf(request.getLevel()))
                .build();

        if(file != null && !file.isEmpty()) {
            var fileResponse = fileClient.uploadFileToMinio(file, bucketName);
            log.info("Upload file successfully, FileName: {}, FileType: {}", fileResponse.getData().getFileName(), fileResponse.getData().getFileType());
            course.setCourseCover(fileResponse.getData().getUrl());
        }
        courseRepository.save(course);
        log.info("Create course successfully. Course name: {}", course.getName());

        CourseEvent courseEvent = CourseEvent.builder()
                .courseId(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .price(course.getPrice().doubleValue())
                .categoryName(course.getCategory().getName())
                .courseCover(course.getCourseCover())
                .build();

        kafkaTemplate.send("created-course", courseEvent);

        return courseMapper.toCourseCreationResponse(course);
    }

    @Override
    public CourseDetailResponse getAllDetailCourses(String courseId) {
        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseException(ErrorCode.COURSE_NOT_FOUND));

        return CourseDetailResponse.builder()
                .courseId(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .price(course.getPrice())
                .categoryName(course.getCategory().getName())
                .courseCover(course.getCourseCover())
                .createdAt(course.getCreatedAt())
                .chapters(course.getChapters().stream()
                        .map(CourseConverter::toChapterDetailResponse)
                        .toList())
                .build();
    }

    @Override
    public PageResponse<CourseResponse> getAllCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Course> courses = courseRepository.findAll(pageable);

        List<CourseResponse> responses = courses.getContent().stream()
                .map(course -> CourseResponse.builder()
                        .courseId(course.getId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .price(course.getPrice())
                        .categoryName(course.getCategory().getName())
                        .courseCover(course.getCourseCover())
                        .createdAt(course.getCreatedAt())
                        .build())
                .toList();

        return PageResponse.<CourseResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalPages(courses.getTotalPages())
                .totalElements(courses.getTotalElements())
                .data(responses)
                .build();
    }

    @Override
    public PageResponse<CourseResponse> getAllCoursesByCategoryName(int page, int size, String categoryName) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Course> coursePage = courseRepository.findCourseByCategoryName(categoryName, pageable);
        List<Course> courses = coursePage.getContent();

        return PageResponse.<CourseResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalPages(coursePage.getTotalPages())
                .totalElements(coursePage.getTotalElements())
                .data(courses.stream().map(course -> CourseResponse.builder()
                        .courseId(course.getId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .price(course.getPrice())
                        .categoryName(course.getCategory().getName())
                        .courseCover(course.getCourseCover())
                        .createdAt(course.getCreatedAt())
                        .build()).toList())
                .build();
    }

    @Override
    public CourseExistsResponse existsById(String id) {
        var exists = courseRepository.existsById(id);
        return CourseExistsResponse.builder()
                .exists(exists)
                .build();
    }

}
