# Shortly — URL Shortener Service

A minimalist URL shortener built with **Spring Boot** and **Kotlin**. Supports shortening URLs, redirecting via short codes, and peeking short codes.

-------------

## 🚀 Getting Started

### Requirements

- Java 17+
- Kotlin 1.9+
- Gradle (Wrapper included)


## ▶️ Running the Application

### Local Run

```bash
./gradlew bootRun
```

### Endpoints

| Method | URL                | Description                  |
|--------|--------------------|------------------------------|
| POST   | `/api/urls`        | Shorten a URL (JSON body)    |
| GET    | `/api/urls/{code}` | Get original URL info        |
| GET    | `/{code}`          | Redirect to original URL     |


## 🧪 Running Tests

### Unit + Integration Tests

Run all tests:

```bash
./gradlew test -i
```

## 🔍 Sample Test Payloads

### Shorten a URL

```bash
curl -X POST http://localhost:8080/api/urls \
  -H "Content-Type: application/json" \
  -d '{"url": "https://example.com"}'
```

### Redirect Using Code

```bash
curl -v http://localhost:8080/{shortCode}
```

Should respond with:

```
HTTP/1.1 302 Found
Location: https://example.com
```

### Check URL status

```bash
curl -v http://localhost:8080/api/urls/{shortCode}
```

Should respond with:

```json
{
	"shortCode": "{shortCode}",
	"originalUrl": "https://example.com"
}
```

## 🛠 Dev Notes

- Uses in-memory `ConcurrentHashMap` + atomic counter
- Short codes generated via modular exponentiation cyclic group (base-36, pseudo-random, reversible)
- Input validation via custom exceptions
- Designed for easy replacement with Redis or DB-backed counter
