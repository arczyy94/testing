package com.example.apitraining.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UpdateUserRequest {
  @Email
  public String email;

  @Size(min = 3, max = 80)
  public String fullName;

  public Boolean active;
}
