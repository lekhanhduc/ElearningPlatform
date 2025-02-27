package com.example.bookservice.exception;

import lombok.Getter;

@Getter
public class BookException extends RuntimeException {

    private final ErrorCode errorCode;

    public BookException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
