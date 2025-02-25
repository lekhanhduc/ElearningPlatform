package com.example.postservice.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.io.Serializable;
import java.time.Instant;

@Document(value = "post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Post implements Serializable {
    @MongoId
    private String id;
    private Long userId;
    private String content;
    private Instant createdDate;
    private Instant modifiedDate;
}
