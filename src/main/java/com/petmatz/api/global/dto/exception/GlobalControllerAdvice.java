package com.petmatz.api.global.dto.exception;

import com.petmatz.api.global.dto.Response;
import com.petmatz.common.exception.CustomException;
import com.petmatz.common.exception.ErrorReason;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response<Void>> handleCustomException(CustomException e, HttpServletRequest request) {
        log.error("Error occurred in {}: {}", e.getSourceLayer(), e.getMessage());
        return ResponseEntity.status(e.getStatus())
                .body(Response.error(
                        e.getErrorCode().getErrorReason(),
                        request.getRequestURI(),
                        e.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleGeneralException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error occurred: ", e);
        ErrorReason internalError = ErrorReason.of(500, "INTERNAL_SERVER_ERROR", "알 수 없는 서버 에러");
        return ResponseEntity.status(internalError.status())
                .body(Response.error(
                        internalError,
                        request.getRequestURI(),
                        e.getMessage()
                ));
    }
}

