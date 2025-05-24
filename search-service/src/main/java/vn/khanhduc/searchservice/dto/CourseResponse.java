package vn.khanhduc.searchservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponse implements Serializable {
    private String courseId;
    private String name;
    private String description;
    private String categoryName;
    private String courseCover;
    private BigDecimal price;
}
