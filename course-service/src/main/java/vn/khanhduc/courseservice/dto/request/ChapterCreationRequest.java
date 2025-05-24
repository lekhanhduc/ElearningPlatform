package vn.khanhduc.courseservice.dto.request;

import lombok.Getter;
import java.io.Serializable;

@Getter
public class ChapterCreationRequest implements Serializable {
    private String courseId;
    private String name;
    private String description;
}
