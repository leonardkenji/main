package com.ctbjj.main.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String error,
        int status,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(String error, int status) {
        return new ErrorResponse(error, status, LocalDateTime.now());
    }
}
