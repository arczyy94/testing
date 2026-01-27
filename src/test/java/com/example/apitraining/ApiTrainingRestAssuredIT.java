package com.example.apitraining;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "app.ratelimit.enabled=false",
    "app.auth.enabled=true",
    "app.auth.token=training-token"
})
class ApiTrainingRestAssuredIT {

  @LocalServerPort
  int port;

  @BeforeEach
  void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  @Test
  void health_shouldBeUp_withoutAuth() {
    given()
      .when().get("/health")
      .then()
      .statusCode(200)
      .body("status", Matchers.equalTo("UP"))
      .body("time", Matchers.notNullValue());
  }

  @Test
  void users_shouldReturn401_whenNoAuthHeader() {
    given()
      .when().get("/api/users")
      .then()
      .statusCode(401)
      .body("code", Matchers.equalTo("UNAUTHORIZED"));
  }

  @Test
  void createUser_shouldWork_withValidPayload() {
    String body = """
      {
        "email": "test.user@example.com",
        "fullName": "Test User"
      }
      """;

    given()
      .header("Authorization", "Bearer training-token")
      .contentType(ContentType.JSON)
      .body(body)
      .when().post("/api/users")
      .then()
      .statusCode(200)
      .body("id", Matchers.notNullValue())
      .body("email", Matchers.equalTo("test.user@example.com"))
      .body("fullName", Matchers.equalTo("Test User"))
      .body("active", Matchers.equalTo(true))
      .body("createdAt", Matchers.notNullValue());
  }

  @Test
  void createUser_shouldReturn400_onValidationError() {
    String body = """
      {
        "email": "not-an-email",
        "fullName": "Te"
      }
      """;

    given()
      .header("Authorization", "Bearer training-token")
      .contentType(ContentType.JSON)
      .body(body)
      .when().post("/api/users")
      .then()
      .statusCode(400)
      .body("code", Matchers.equalTo("VALIDATION_ERROR"))
      .body("details.fields.email", Matchers.notNullValue())
      .body("details.fields.fullName", Matchers.notNullValue());
  }
}
