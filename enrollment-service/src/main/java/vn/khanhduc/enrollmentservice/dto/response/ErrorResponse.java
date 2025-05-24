package vn.khanhduc.enrollmentservice.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse implements Serializable {
    private Date timestamp;
    private int status;
    private String error;
    private String path;
}
