package vn.khanhduc.courseservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LessonDetailResponse implements Serializable {
    private String lessonId;
    private String lessonName;
    private String lessonDescription;
    private String linkVideoLesson;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
