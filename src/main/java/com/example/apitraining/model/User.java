package com.example.apitraining.model;

import java.time.Instant;
import java.util.UUID;

public class User {
  private UUID id;
  private String email;
  private String fullName;
  private boolean active;
  private Instant createdAt;

  public User(UUID id, String email, String fullName, boolean active, Instant createdAt) {
    this.id = id;
    this.email = email;
    this.fullName = fullName;
    this.active = active;
    this.createdAt = createdAt;
  }

  public UUID getId() { return id; }
  public String getEmail() { return email; }
  public String getFullName() { return fullName; }
  public boolean isActive() { return active; }
  public Instant getCreatedAt() { return createdAt; }

  public void setEmail(String email) { this.email = email; }
  public void setFullName(String fullName) { this.fullName = fullName; }
  public void setActive(boolean active) { this.active = active; }
}
