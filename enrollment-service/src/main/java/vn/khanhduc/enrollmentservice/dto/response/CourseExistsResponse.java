package vn.khanhduc.enrollmentservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CourseExistsResponse {
    private boolean exists;
}
