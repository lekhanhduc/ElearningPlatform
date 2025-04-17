package com.example.fileservice.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.io.Serializable;

@Document(collection = "file_management")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileManagement implements Serializable {

    @MongoId
    private String name;
    private Long userId;
    private String contentType;
    private long size;
    private String md5Checksum;
    private String path;

}
