package vn.khanhduc.reviewservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class ReviewCreationResponse implements Serializable {
    private String id;
    private Long userId;
    private String bookId;
    private String content;
    private Integer rating;
    private Long parentReview;
}
