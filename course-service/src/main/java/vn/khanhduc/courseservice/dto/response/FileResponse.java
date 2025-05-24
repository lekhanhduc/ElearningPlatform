package vn.khanhduc.courseservice.dto.response;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse implements Serializable {
    private String fileName;
    private String fileType;
    private String url;
}
