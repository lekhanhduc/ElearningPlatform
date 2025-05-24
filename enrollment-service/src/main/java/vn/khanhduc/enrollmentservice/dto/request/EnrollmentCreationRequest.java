package vn.khanhduc.enrollmentservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class EnrollmentCreationRequest implements Serializable {

    @NotBlank(message = "CourseId cannot be blank")
    private String courseId;

    @NotNull(message = "Price cannot be null")
    private BigDecimal price;
}
