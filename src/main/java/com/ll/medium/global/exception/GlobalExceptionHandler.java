package com.ll.medium.global.exception;

import com.ll.medium.global.common.Message;
import com.ll.medium.global.rsData.RsData;
import com.ll.medium.standard.base.Empty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus
    public ResponseEntity<RsData<Empty>> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RsData.of(
                        "404",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    @ResponseStatus
    public ResponseEntity<RsData<Empty>> handleAccessDeniedException(CustomAccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(RsData.of(
                        "403",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus
    public ResponseEntity<RsData<Empty>> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(RsData.of(
                        "403",
                        Message.Error.NOT_LOGGED_IN.getMessage()
                ));
    }
}
