package com.learnova.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {

        return new ResponseEntity<>(
                new ErrorResponse(LocalDateTime.now(), 400, ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {

        return new ResponseEntity<>(
                new ErrorResponse(LocalDateTime.now(), 401, ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }
}