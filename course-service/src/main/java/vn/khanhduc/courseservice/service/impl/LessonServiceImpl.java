package vn.khanhduc.courseservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.khanhduc.courseservice.dto.request.LessonCreationRequest;
import vn.khanhduc.courseservice.dto.response.LessonCreationResponse;
import vn.khanhduc.courseservice.entity.Lesson;
import vn.khanhduc.courseservice.exception.CourseException;
import vn.khanhduc.courseservice.exception.ErrorCode;
import vn.khanhduc.courseservice.repository.ChapterRepository;
import vn.khanhduc.courseservice.repository.CourseRepository;
import vn.khanhduc.courseservice.repository.LessonRepository;
import vn.khanhduc.courseservice.service.LessonService;
import vn.khanhduc.courseservice.common.SecurityUtils;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "LESSON-SERVICE")
public class LessonServiceImpl implements LessonService {

    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final LessonRepository lessonRepository;

    @Override
    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public LessonCreationResponse create(LessonCreationRequest request) {
        SecurityUtils.getCurrentUser();

        var course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new CourseException(ErrorCode.COURSE_NOT_FOUND));

        var chapter = chapterRepository.findById(request.getChapterId())
                .orElseThrow(() -> new CourseException(ErrorCode.CHAPTER_NOT_FOUND));

        var lesson = Lesson.builder()
                .name(request.getLessonName())
                .description(request.getLessonDescription())
                .lessonUrl(request.getLinkVideoLesson())
                .chapter(chapter)
                .build();
        lessonRepository.save(lesson);
        log.info("Create lesson successfully. Course name: {}, Chapter name: {} Lesson name: {}",
                course.getName(), chapter.getName(), lesson.getName());

        return LessonCreationResponse.builder()
                .courseId(lesson.getChapter().getCourse().getId())
                .chapterId(lesson.getChapter().getId())
                .lessonId(lesson.getId())
                .lessonName(lesson.getName())
                .lessonDescription(lesson.getDescription())
                .linkVideoLesson(lesson.getLessonUrl())
                .createdAt(lesson.getCreatedAt())
                .build();
    }

}
