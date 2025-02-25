package com.example.postservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Builder
public class PostDetailResponse implements Serializable {
    private String id;
    private Long userId;
    private String content;
    private String created;
    private Instant createdDate;
    private Instant modifiedDate;
}
