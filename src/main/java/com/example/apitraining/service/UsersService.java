package com.example.apitraining.service;

import com.example.apitraining.dto.CreateUserRequest;
import com.example.apitraining.dto.UpdateUserRequest;
import com.example.apitraining.model.User;
import com.example.apitraining.web.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsersService {
  private final Map<UUID, User> users = new ConcurrentHashMap<>();

  public UsersService() {
    var u1 = new User(UUID.randomUUID(), "anna@example.com", "Anna Kowalska", true, Instant.now());
    var u2 = new User(UUID.randomUUID(), "jan@example.com", "Jan Nowak", true, Instant.now());
    users.put(u1.getId(), u1);
    users.put(u2.getId(), u2);
  }

  public List<User> list(Optional<Boolean> active, Optional<String> q, int page, int size) {
    var stream = users.values().stream();

    if (active.isPresent()) {
      stream = stream.filter(u -> u.isActive() == active.get());
    }
    if (q.isPresent() && !q.get().isBlank()) {
      var qq = q.get().toLowerCase(Locale.ROOT);
      stream = stream.filter(u ->
          u.getEmail().toLowerCase(Locale.ROOT).contains(qq) ||
          u.getFullName().toLowerCase(Locale.ROOT).contains(qq));
    }

    var all = stream
        .sorted(Comparator.comparing(User::getCreatedAt).reversed())
        .toList();

    int from = Math.min(page * size, all.size());
    int to = Math.min(from + size, all.size());
    return all.subList(from, to);
  }

  public User get(UUID id) {
    var u = users.get(id);
    if (u == null) throw new NotFoundException("User not found: " + id);
    return u;
  }

  public User create(CreateUserRequest req) {
    var id = UUID.randomUUID();
    var user = new User(id, req.email, req.fullName, true, Instant.now());
    users.put(id, user);
    return user;
  }

  public User update(UUID id, UpdateUserRequest req) {
    var u = get(id);
    if (req.email != null) u.setEmail(req.email);
    if (req.fullName != null) u.setFullName(req.fullName);
    if (req.active != null) u.setActive(req.active);
    return u;
  }

  public void delete(UUID id) {
    if (users.remove(id) == null) throw new NotFoundException("User not found: " + id);
  }

  public boolean exists(UUID id) {
    return users.containsKey(id);
  }
}
