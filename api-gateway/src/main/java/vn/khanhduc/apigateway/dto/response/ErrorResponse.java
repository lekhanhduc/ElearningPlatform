package vn.khanhduc.apigateway.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
public class ErrorResponse implements Serializable {

    private Date timestamp;
    private int code;
    private String error;
    private String message;
    private String path;
}
