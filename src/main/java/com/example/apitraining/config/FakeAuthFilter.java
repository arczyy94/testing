package com.example.apitraining.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FakeAuthFilter extends OncePerRequestFilter {

  @Value("${app.auth.enabled:true}")
  boolean enabled;

  @Value("${app.auth.token:training-token}")
  String token;

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

    String auth = request.getHeader("Authorization");
    String expected = "Bearer " + token;

    if (auth == null || !auth.equals(expected)) {
      response.setStatus(401);
      response.setContentType("application/json");
      response.getWriter().write("{\"code\":\"UNAUTHORIZED\",\"message\":\"Missing/invalid token\"}");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
