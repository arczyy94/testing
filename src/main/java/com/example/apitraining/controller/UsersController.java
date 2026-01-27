package com.example.apitraining.controller;

import com.example.apitraining.dto.CreateUserRequest;
import com.example.apitraining.dto.UpdateUserRequest;
import com.example.apitraining.model.User;
import com.example.apitraining.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UsersController {
  private final UsersService usersService;

  public UsersController(UsersService usersService) {
    this.usersService = usersService;
  }

  @GetMapping
  public List<User> list(
      @RequestParam Optional<Boolean> active,
      @RequestParam Optional<String> q,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    return usersService.list(active, q, page, size);
  }

  @GetMapping("/{id}")
  public User get(@PathVariable UUID id) {
    return usersService.get(id);
  }

  @PostMapping
  public User create(@Valid @RequestBody CreateUserRequest req) {
    return usersService.create(req);
  }

  @PatchMapping("/{id}")
  public User patch(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest req) {
    return usersService.update(id, req);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    usersService.delete(id);
  }
}
