package com.example.apitraining.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateOrderRequest {
  @NotNull
  public UUID userId;

  @NotBlank
  public String product;

  @NotNull
  @DecimalMin("0.01")
  public BigDecimal amount;
}
