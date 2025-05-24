package vn.khanhduc.courseservice.dto.request;

import lombok.Getter;
import java.io.Serializable;

@Getter
public class CategoryCreationRequest implements Serializable {
    private String name;
    private String description;
}
