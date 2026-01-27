package com.example.apitraining.dto;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {
  public Instant timestamp = Instant.now();
  public String code;
  public String message;
  public String path;
  public Map<String, Object> details;

  public ErrorResponse(String code, String message, String path, Map<String, Object> details) {
    this.code = code;
    this.message = message;
    this.path = path;
    this.details = details;
  }
}
