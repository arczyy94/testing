package com.example.apitraining.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

  @Value("${app.ratelimit.enabled:true}")
  boolean enabled;

  @Value("${app.ratelimit.requestsPerMinute:30}")
  int rpm;

  private static class Bucket {
    long windowStartEpochMinute;
    AtomicInteger count = new AtomicInteger(0);
  }

  private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return request.getRequestURI().equals("/health");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (!enabled) {
      filterChain.doFilter(request, response);
      return;
    }

    String key = request.getRemoteAddr();
    long minute = Instant.now().getEpochSecond() / 60;

    Bucket b = buckets.computeIfAbsent(key, k -> new Bucket());
    synchronized (b) {
      if (b.windowStartEpochMinute != minute) {
        b.windowStartEpochMinute = minute;
        b.count.set(0);
      }
      int n = b.count.incrementAndGet();
      response.setHeader("X-RateLimit-Limit", String.valueOf(rpm));
      response.setHeader("X-RateLimit-Remaining", String.valueOf(Math.max(0, rpm - n)));

      if (n > rpm) {
        response.setStatus(429);
        response.setContentType("application/json");
        response.getWriter().write("{\"code\":\"TOO_MANY_REQUESTS\",\"message\":\"Rate limit exceeded\"}");
        return;
      }
    }

    filterChain.doFilter(request, response);
  }
}
