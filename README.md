# Trimify

A lightweight web-based application that provide high performance URL shortening service. It allows users to convert long, cumbersome URLs into short, shareable links with built-in redirection logic. The service is designed with scalability, security, and analytics in mind, making it suitable for both personal and enterprise use.

## Objectives of Trimify

- Provide a fast and reliable way to shorten long URLs.
- Ensure unique and collision-free short links through proper encoding or ID generation techniques.
- Handle redirection from short URLs to original destinations seamlessly.
- Log essential click metrics for future analytics (e.g., hit count, timestamp, referrer).
- Offer a RESTful API interface to integrate with web, mobile, or browser-based clients.
- Provide a simple web UI for users to manage or track their shortened links.

---

## URL Trimify – ER Diagram

![Entity Relationship Diagram](./diagram/Trimify-ERD.png)

- `users` → Stores user credentials and roles.
- `url_mapping` → Contains mappings between original and shortened URLs.
  - **Many-to-One** with `users` (A user can generate many shortened URLs)
- `click_event` → Logs each time a shortened URL is accessed.
  - **Many-to-One** with `url_mapping` (Each URL can have multiple click events)

---

## Authentication using JWT (Json Web Token)

![JWT Authentication Process](./diagram/JWT-authentication.png)

- Tokens are sent using HTTP Authorized header.

> **Format** → `Authorization: Bearer <token>`

In its compact form, JSON Web Tokens consist of three parts separated by dots (.), which are:

- Header
- Payload
- Signature

Therefore, a JWT typically looks like the: `xxxxx.yyyyy.zzzzz`

### JWT Implementation Files

- `JwtUtils` → Contains utility methods for generating, parsing, and validating JWTs. Inclues:
  - Generating a token from username
  - Validating a JWT
  - Extracting the username from a token

- `JwtAutheticationFilter` → Filters incoming requests to **check for a valid JWT in the header**, setting the authentication context if the token is valid.
  - Intercepts HTTP requests.
  - Extracts JWT from request header, validates it.
  - Configures the Spring Security context with user details if the token is valid.

- `JwtAutheticationResponse` → DTO for JWT authentication respose.

- `SecurityConfig` → Configures Spring Security files & rules for the application. Sets up the secuirty filter chain, permitting or denying the access based on paths and roles.

### Understand `UserDetailsServiceImpl`

> Spring Security has its own **inbuilt implemenation** of User which would work, but in most of the cases custom implementation is needed.

#### `UserDetailsServiceImpl`

- This is a service that loads user details from the database.
- Bridges the gap between the database (User entity) and Spring Security (User Details interface).

#### `UserDetailsImpl`

- This is a *custom implementation* of Spring Security's `UserDetails` interface.
- Provides a Spring Security-compatible representation of the user for authentication and authorization.

### Maven dependencies for JWT

Refer to the [Official GitHub](https://github.com/jwtk/jjwt) for [Maven dependencies for JWT](https://github.com/jwtk/jjwt?tab=readme-ov-file#maven). Add them to `pom.xml` file.

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
```

---

## Custom Security Configuration

Defines the security rules for handling HTTP requests.

### Configure `JwtAuthenticationFilter` in filter chain

- Spring Security recongnizes it as a filter that will only be executed *once per request*.
- By default, Spring Security doesn't automatically include your custom filter (`JwtAuthenticationFilter`) in the filter chain unless you explicitly add it.

### Configure `DaoAuthenticationProvider`

- It's a pre-built class in Spring Security.
- Sets up how authentication is handled by Spring Security.
- `DaoAuthenticationProvider` is an implementation of `AuthenticationProvider` that **validates user credentials** during login. It uses:
  - A `UserDetailsService` to load user details (typically from a database)
  - A `PasswordEncoder` to check credentials (e.g., hashed password verification)

### Bean for `PasswordEncoder`

```java
@Bean
public PasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder();
}
```

- Creates a **singleton bean** of `PasswordEncoder` using `BCrypt`.
- `BCryptPasswordEncoder` hashes user passwords securely.
- Used during:
  - **Registration**: To hash and store password.
  - **Login**: To compare raw password with the hashed one stored in DB.

> Passwords must never be stored or compared as plain text.

### Bean for `AuthenticationManager`

```java
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
}
```

- This *integrates* cleanly with your existing `DaoAuthenticationProvider`.
- Spring uses the `AuthenticationManager` built from our custom security config (which includes our custom `DaoAuthenticationProvider`).

---

### Author

- [Soumo Sarkar](https://www.linkedin.com/in/soumo-sarkar/)
