package com.ll.medium.global.exception;

import com.ll.medium.global.common.ErrorMessage;
import com.ll.medium.global.rsData.RsData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RsData<Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RsData.of(
                        "404",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<RsData<Object>> handleAccessDeniedException(CustomAccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(RsData.of(
                        "403",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RsData<Object>> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(RsData.of(
                        "403",
                        ErrorMessage.NOT_LOGGED_IN.getMessage()
                ));
    }
}
