package vn.khanhduc.courseservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import java.io.Serializable;

@Getter
public class LessonCreationRequest implements Serializable {

    @NotBlank(message = "CourseId must not be blank")
    private String courseId;

    @NotBlank(message = "ChapterId must not be blank")
    private String chapterId;

    @NotBlank(message = "LessonName must not be blank")
    private String lessonName;

    private String lessonDescription;
    private String linkVideoLesson;
}
