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

### Author

- [Soumo Sarkar](https://www.linkedin.com/in/soumo-sarkar/)
