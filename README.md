# API Training (Spring Boot + Gradle)

## Requirements
- Java 17+

## Run (with local Gradle installed)
```bash
gradle bootRun
```

Health:
```bash
curl http://localhost:8080/health
```

Authorized calls:
```bash
curl -H "Authorization: Bearer training-token" http://localhost:8080/api/users
```

## Gradle Wrapper (recommended)
This zip **does not include** the Gradle Wrapper jar (to keep it lightweight).
Generate it once (requires local `gradle` installed):
```bash
gradle wrapper
```
Then:
```bash
./gradlew bootRun
./gradlew test
```

## Tests
Integration tests with Rest Assured are in `src/test/java/...`
