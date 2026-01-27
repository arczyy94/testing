package com.example.apitraining.service;

import com.example.apitraining.dto.CreateOrderRequest;
import com.example.apitraining.model.Order;
import com.example.apitraining.model.OrderStatus;
import com.example.apitraining.web.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrdersService {
  private final Map<UUID, Order> orders = new ConcurrentHashMap<>();
  private final UsersService usersService;

  public OrdersService(UsersService usersService) {
    this.usersService = usersService;
  }

  public List<Order> list(Optional<UUID> userId, Optional<OrderStatus> status, int page, int size) {
    var stream = orders.values().stream();

    if (userId.isPresent()) stream = stream.filter(o -> o.getUserId().equals(userId.get()));
    if (status.isPresent()) stream = stream.filter(o -> o.getStatus() == status.get());

    var all = stream
        .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
        .toList();

    int from = Math.min(page * size, all.size());
    int to = Math.min(from + size, all.size());
    return all.subList(from, to);
  }

  public Order get(UUID id) {
    var o = orders.get(id);
    if (o == null) throw new NotFoundException("Order not found: " + id);
    return o;
  }

  public Order create(CreateOrderRequest req) {
    if (!usersService.exists(req.userId)) {
      throw new NotFoundException("User not found for order: " + req.userId);
    }
    var id = UUID.randomUUID();
    var order = new Order(id, req.userId, req.product, req.amount, OrderStatus.NEW, Instant.now());
    orders.put(id, order);
    return order;
  }

  public Order setStatus(java.util.UUID id, OrderStatus status) {
    var o = get(id);
    o.setStatus(status);
    return o;
  }

  public void delete(java.util.UUID id) {
    if (orders.remove(id) == null) throw new NotFoundException("Order not found: " + id);
  }
}
