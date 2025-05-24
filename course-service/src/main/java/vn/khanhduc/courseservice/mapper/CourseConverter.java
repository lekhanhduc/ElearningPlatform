package vn.khanhduc.courseservice.mapper;

import vn.khanhduc.courseservice.dto.response.ChapterDetailResponse;
import vn.khanhduc.courseservice.dto.response.LessonDetailResponse;
import vn.khanhduc.courseservice.entity.Chapter;
import vn.khanhduc.courseservice.entity.Lesson;

public class CourseConverter {

    private CourseConverter() {}

    public static ChapterDetailResponse toChapterDetailResponse(Chapter chapter) {
        return ChapterDetailResponse.builder()
                .chapterId(chapter.getId())
                .name(chapter.getName())
                .description(chapter.getDescription())
                .createdAt(chapter.getCreatedAt())
                .lessons(chapter.getLessons().stream().map(CourseConverter::toLessonDetailResponse)
                        .toList())
                .build();
    }

    private static LessonDetailResponse toLessonDetailResponse(Lesson lesson) {
        return LessonDetailResponse.builder()
                .lessonId(lesson.getId())
                .lessonName(lesson.getName())
                .lessonDescription(lesson.getDescription())
                .linkVideoLesson(lesson.getLessonUrl())
                .createdAt(lesson.getCreatedAt())
                .build();
    }

}
