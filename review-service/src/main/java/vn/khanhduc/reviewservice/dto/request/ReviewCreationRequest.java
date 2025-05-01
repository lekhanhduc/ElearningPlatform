package vn.khanhduc.reviewservice.dto.request;

import lombok.Getter;
import java.io.Serializable;

@Getter
public class ReviewCreationRequest implements Serializable {
    private Long userId;
    private String bookId;
    private String content;
    private Integer rating;
}
