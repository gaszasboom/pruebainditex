package com.inditex.priceservice.infrastructure.adapter.in.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "resource_not_found", "Not Found");
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ProblemDetail> handleBadRequestException(Exception ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid parameters provided", "invalid_request", "Bad Request");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error occurred", "internal_error", "Internal Server Error");
    }

    private ResponseEntity<ProblemDetail> buildErrorResponse(HttpStatus status, String detail, String code, String title) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create("https://tools.ietf.org/html/rfc9457"));
        problemDetail.setProperty("code", code);
        problemDetail.setProperty("request_id", UUID.randomUUID().toString());
        return ResponseEntity.status(status).body(problemDetail);
    }
}
