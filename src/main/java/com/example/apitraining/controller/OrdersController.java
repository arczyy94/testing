package com.example.apitraining.controller;

import com.example.apitraining.dto.CreateOrderRequest;
import com.example.apitraining.model.Order;
import com.example.apitraining.model.OrderStatus;
import com.example.apitraining.service.OrdersService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
  private final OrdersService ordersService;

  public OrdersController(OrdersService ordersService) {
    this.ordersService = ordersService;
  }

  @GetMapping
  public List<Order> list(
      @RequestParam Optional<UUID> userId,
      @RequestParam Optional<OrderStatus> status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    return ordersService.list(userId, status, page, size);
  }

  @GetMapping("/{id}")
  public Order get(@PathVariable UUID id) {
    return ordersService.get(id);
  }

  @PostMapping
  public Order create(@Valid @RequestBody CreateOrderRequest req) {
    return ordersService.create(req);
  }

  @PostMapping("/{id}/status")
  public Order setStatus(@PathVariable UUID id, @RequestParam OrderStatus status) {
    return ordersService.setStatus(id, status);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    ordersService.delete(id);
  }
}
