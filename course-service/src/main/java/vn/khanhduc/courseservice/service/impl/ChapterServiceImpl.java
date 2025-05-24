package vn.khanhduc.courseservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.khanhduc.courseservice.dto.request.ChapterCreationRequest;
import vn.khanhduc.courseservice.dto.response.ChapterCreationResponse;
import vn.khanhduc.courseservice.entity.Chapter;
import vn.khanhduc.courseservice.exception.CourseException;
import vn.khanhduc.courseservice.exception.ErrorCode;
import vn.khanhduc.courseservice.mapper.ChapterMapper;
import vn.khanhduc.courseservice.repository.ChapterRepository;
import vn.khanhduc.courseservice.repository.CourseRepository;
import vn.khanhduc.courseservice.service.ChapterService;
import vn.khanhduc.courseservice.common.SecurityUtils;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CHAPTER-SERVICE")
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final ChapterMapper chapterMapper;

    @Override
    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public ChapterCreationResponse create(ChapterCreationRequest request) {
        SecurityUtils.getCurrentUser();

        var course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new CourseException(ErrorCode.COURSE_NOT_FOUND));

        var chapter = Chapter.builder()
                .name(request.getName())
                .description(request.getDescription())
                .course(course)
                .build();
        chapterRepository.save(chapter);

        return chapterMapper.toChapterCreationResponse(chapter);
    }

}
