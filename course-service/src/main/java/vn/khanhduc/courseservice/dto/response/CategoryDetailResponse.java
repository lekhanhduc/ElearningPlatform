package vn.khanhduc.courseservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class CategoryDetailResponse implements Serializable {
    private String categoryId;
    private String name;
}
