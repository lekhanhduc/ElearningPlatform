package vn.khanhduc.fileservice.exception;

import lombok.Getter;

@Getter
public class FileException extends RuntimeException{

    private final ErrorCode errorCode;

    public FileException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
