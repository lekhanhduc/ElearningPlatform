package com.example.fileservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class FileResponse implements Serializable {
    private String fileName;
    private String fileType;
    private String url;
}
