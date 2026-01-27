package com.example.apitraining.web;

import com.example.apitraining.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> notFound(NotFoundException ex, HttpServletRequest req) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("NOT_FOUND", ex.getMessage(), req.getRequestURI(), null));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    Map<String, Object> details = new HashMap<>();
    Map<String, String> fields = new HashMap<>();
    for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
      fields.put(fe.getField(), fe.getDefaultMessage());
    }
    details.put("fields", fields);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("VALIDATION_ERROR", "Request validation failed", req.getRequestURI(), details));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> other(Exception ex, HttpServletRequest req) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("INTERNAL_ERROR", ex.getMessage(), req.getRequestURI(), null));
  }
}
