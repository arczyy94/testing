package com.example.apitraining.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Order {
  private UUID id;
  private UUID userId;
  private String product;
  private BigDecimal amount;
  private OrderStatus status;
  private Instant createdAt;

  public Order(UUID id, UUID userId, String product, BigDecimal amount, OrderStatus status, Instant createdAt) {
    this.id = id;
    this.userId = userId;
    this.product = product;
    this.amount = amount;
    this.status = status;
    this.createdAt = createdAt;
  }

  public UUID getId() { return id; }
  public UUID getUserId() { return userId; }
  public String getProduct() { return product; }
  public BigDecimal getAmount() { return amount; }
  public OrderStatus getStatus() { return status; }
  public Instant getCreatedAt() { return createdAt; }

  public void setProduct(String product) { this.product = product; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public void setStatus(OrderStatus status) { this.status = status; }
}
