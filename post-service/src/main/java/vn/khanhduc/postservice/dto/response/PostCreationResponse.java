package vn.khanhduc.postservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Builder
public class PostCreationResponse implements Serializable {
    private String id;
    private String content;
    private Long userId;
    private String created;
    private Instant createdDate;
    private Instant modifiedDate;
}
