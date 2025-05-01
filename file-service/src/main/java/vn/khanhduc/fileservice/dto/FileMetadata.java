package vn.khanhduc.fileservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class FileMetadata implements Serializable {
    private String contentType;
    private Resource resource;
}
