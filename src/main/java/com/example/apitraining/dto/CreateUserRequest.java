package com.example.apitraining.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {
  @NotBlank
  @Email
  public String email;

  @NotBlank
  @Size(min = 3, max = 80)
  public String fullName;
}
