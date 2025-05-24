package vn.khanhduc.courseservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class CourseCreationRequest implements Serializable {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotNull(message = "Price must not be null")
    @Min(value = 0)
    private BigDecimal price;

    @NotBlank(message = "CategoryName cover must not be blank")
    private String categoryName;

    @NotBlank(message = "Level course must not be blank")
    private String level;
}
