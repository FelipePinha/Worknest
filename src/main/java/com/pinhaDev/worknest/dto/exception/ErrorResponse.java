package com.pinhaDev.worknest.dto.exception;

import org.springframework.http.HttpStatus;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {
}
