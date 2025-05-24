package vn.khanhduc.courseservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChapterCreationResponse implements Serializable {
    private Long userId;
    private String courseId;
    private String chapterId;
    private String name;
    private String description;
}
