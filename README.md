# 🛒 ECom-App

A production-oriented **Spring Boot e-commerce REST API** built as a monolithic backend with **PostgreSQL, Redis, Spring Security, JWT authentication, Docker, and Docker Compose**.

The project focuses on building a realistic backend with authentication, role-based authorization, product management, shopping carts, order processing, caching, database persistence, and containerization.

> 🚀 **Live API:** https://ecom-app-gbst.onrender.com/
> 💻 **Repository:** https://github.com/anuPhoenixbis/ecom-app

---

## ✨ Features

### 🔐 Authentication & Authorization

* User registration and login
* JWT-based authentication
* BCrypt password hashing
* Role-based authorization
* `CUSTOMER` and `ADMIN` roles
* Protected admin endpoints
* Stateless REST API security
* Custom JWT authentication filter

### 👤 User Management

* User profile creation and management
* User information retrieval
* Address management
* Admin user management
* Admin user deletion

### 📦 Product Management

* Create products
* Update products
* Delete products
* Activate/deactivate products
* Product search
* Product category support
* Product stock management
* Product validation
* Active/inactive product handling

### 🛒 Shopping Cart

* Add products to cart
* Retrieve current user's cart
* Change cart quantities
* Remove cart items
* Clear cart after successful order creation
* Product stock validation

### 📋 Order Management

* Create orders from cart items
* Retrieve user orders
* Retrieve individual orders
* Cancel orders
* Order status management
* Stock restoration when an order is cancelled
* Historical order item pricing and quantities

### ⚡ Redis Caching

Redis is used as a caching layer for frequently accessed product data.

Currently cached operations include:

* Get all active products
* Product search results
* Individual product lookups

The application follows a **cache-aside** approach:

```text
Client
  │
  ▼
Spring Boot
  │
  ▼
Redis
  │
  ├── Cache Hit ──────► Return cached data
  │
  └── Cache Miss
          │
          ▼
      PostgreSQL
          │
          ▼
        Redis
          │
          ▼
       Response
```

Cached data uses TTL-based expiration to prevent stale entries from remaining indefinitely.

### 🐳 Docker

The application includes:

* Multi-stage Docker build
* Java 21 runtime image
* Docker Compose configuration
* Redis container
* Redis persistent volume
* Redis health check
* Dedicated Docker network
* Environment-based configuration

The Docker setup separates the Maven build environment from the lightweight Java runtime image.

---

## 🏗️ Architecture

The current application intentionally uses a **monolithic architecture**.

```text
                         Client
                           │
                           ▼
                  ┌─────────────────┐
                  │   Spring Boot   │
                  │     ECom-App    │
                  └────────┬────────┘
                           │
             ┌─────────────┼─────────────┐
             │             │             │
             ▼             ▼             ▼
        PostgreSQL       Redis        Security
          (Neon)        Cache          + JWT
             │
             ▼
      Persistent Data
```

The backend is organized internally by responsibility:

```text
Controller
    │
    ▼
Service
    │
    ├──────────────► Redis
    │
    ▼
Repository
    │
    ▼
PostgreSQL
```

The monolith is deliberately being kept simple at this stage. The project can later be evolved into a microservice architecture once the current backend is stable.

---

## 🧰 Tech Stack

| Technology           | Purpose                           |
| -------------------- | --------------------------------- |
| Java 21              | Programming language              |
| Spring Boot 4.1.0    | Backend framework                 |
| Spring Web MVC       | REST API                          |
| Spring Data JPA      | Database persistence              |
| Hibernate            | ORM                               |
| PostgreSQL           | Primary database                  |
| Neon PostgreSQL      | Managed PostgreSQL hosting        |
| Spring Security      | Authentication & authorization    |
| JWT / JJWT           | Token-based authentication        |
| BCrypt               | Password hashing                  |
| Redis                | Application caching               |
| Spring Data Redis    | Redis integration                 |
| Maven                | Build & dependency management     |
| Docker               | Containerization                  |
| Docker Compose       | Local multi-container environment |
| Lombok               | Boilerplate reduction             |
| Bean Validation      | Request validation                |
| Spring Boot Actuator | Application monitoring            |

The project currently targets Java 21 and uses Spring Boot 4.1.0.

---

## 📁 Project Structure

```text
ecom-app/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/app/ecom_app/
│   │   │
│   │   │       ├── config/
│   │   │       │   ├── RedisConfig.java
│   │   │       │   └── SpringSecurity.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   ├── AdminOrderController.java
│   │   │       │   ├── AdminProductController.java
│   │   │       │   ├── AdminUserController.java
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── CartController.java
│   │   │       │   ├── OrderController.java
│   │   │       │   ├── ProductController.java
│   │   │       │   └── UserController.java
│   │   │       │
│   │   │       ├── dto/
│   │   │       ├── enums/
│   │   │       ├── filter/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       ├── utils/
│   │   │       └── EcomAppApplication.java
│   │   │
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.yml
│   │
│   └── test/
│
├── Dockerfile
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
└── pom.xml
```

---

# 🔑 Authentication Flow

The application uses JWT-based stateless authentication.

```text
                    POST /auth/login
                           │
                           ▼
                    Authenticate User
                           │
                           ▼
                    Generate JWT
                           │
                           ▼
                     Return Token
                           │
                           ▼
              Authorization: Bearer <JWT>
                           │
                           ▼
                      JwtFilter
                           │
                           ▼
                 Validate JWT Token
                           │
                           ▼
                SecurityContextHolder
                           │
                           ▼
                  Protected Endpoint
```

The JWT contains authentication-related information and is validated by the custom `JwtFilter` before protected requests reach the application logic.

---

# 👑 Role-Based Authorization

The application supports two primary roles:

```text
CUSTOMER
ADMIN
```

Admin endpoints are protected using Spring Security.

Examples of admin functionality include:

```text
/admin/products
/admin/users
/admin/orders
```

while public product read operations can be accessed without authentication.

---

# 🗄️ Database

The application uses **PostgreSQL** as its persistent data store.

The deployed environment uses **Neon PostgreSQL**, while database configuration is supplied through environment variables.

The application expects:

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=
```

The datasource configuration is environment-driven rather than hardcoded.

### Main entities

```text
User
 │
 └── Address

Product

CartItem

Order
 │
 └── OrderItem
```

JPA/Hibernate handles persistence and entity relationships.

---

# ⚡ Redis

Redis is used as an external caching layer rather than as the primary data store.

The application uses a dedicated `RedisService` for:

* Reading cached objects
* Writing objects with TTL
* Deleting individual keys
* Clearing cached data

Example cache keys:

```text
product:{id}
product:all
product:search:{keyword}
```

The Docker Compose configuration runs Redis separately from the application and provides a persistent Redis volume and health check.

---

# 🐳 Running with Docker

## Prerequisites

Make sure you have:

* Docker
* Docker Compose
* A PostgreSQL database
* Required environment variables

Create a `.env` file:

```env
DB_URL=jdbc:postgresql://<host>/<database>
DB_USERNAME=<username>
DB_PASSWORD=<password>

JWT_SECRET=<secret>

REDIS_URL=redis://...
```

> **Never commit `.env` or production credentials to Git.**

Then run:

```bash
docker compose up --build
```

The Compose configuration starts:

```text
ecom-app
    │
    └── Redis
```

The application is exposed on:

```text
http://localhost:8080
```

The Dockerfile uses a multi-stage build: Maven + JDK 21 are used during compilation, while the final image runs on a Java 21 JRE image.

---

# 💻 Running Locally Without Docker

Clone the repository:

```bash
git clone https://github.com/anuPhoenixbis/ecom-app.git
cd ecom-app
```

Configure the required environment variables:

```env
DB_URL=jdbc:postgresql://<host>/<database>
DB_USERNAME=<username>
DB_PASSWORD=<password>
JWT_SECRET=<secret>
REDIS_URL=<redis-url>
```

Then run:

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

---

# 🌐 Deployment

The application is currently deployed on **Render**.

### Live deployment

**https://ecom-app-gbst.onrender.com/**

The application uses environment variables for deployment configuration, allowing credentials and secrets to remain outside the source code.

---

# 🧪 API Overview

### Authentication

```http
POST /auth/signup
POST /auth/login
```

### Products

```http
GET    /api/products
GET    /api/products/{id}
GET    /api/products/search
```

### Cart

```http
POST   /api/cart
GET    /api/cart
DELETE /api/cart/items/{productId}
```

### Orders

```http
POST /api/orders
GET  /api/orders
GET  /api/orders/{id}
```

### User

```http
GET /api/users/me
PUT /api/users/me
```

### Admin

```text
/admin/products
/admin/users
/admin/orders
```

> Endpoint paths can evolve as the project continues to develop. Refer to the controller implementations for the current API contract.

---

# 📊 Observability

Spring Boot Actuator is included in the project for application monitoring and health information.

The application exposes Actuator endpoints through the configured management endpoint settings.

A basic health check can be used to determine whether the application is running correctly.

---

# 🔒 Security Considerations

This project uses environment variables for sensitive configuration:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
REDIS_URL
```

Secrets should **never** be hardcoded into the application or committed to Git.

For production deployments, use the deployment platform's secret/environment-variable management instead of committing credentials.

---

# 🚧 Current Architecture

The project is intentionally a **monolith** at this stage.

```text
                    ECom-App
                       │
       ┌───────────────┼────────────────┐
       │               │                │
       ▼               ▼                ▼
     Auth           Products          Users
       │               │                │
       └───────────────┼────────────────┘
                       │
             ┌─────────┴─────────┐
             ▼                   ▼
         PostgreSQL            Redis
```

The goal is to first build and deploy a complete monolithic backend before introducing distributed-system complexity.

---

# 🔮 Future Improvements

The architecture can later evolve into a microservice-based system.

Potential future services:

```text
API Gateway
     │
     ├── Auth Service
     ├── User Service
     ├── Product Service
     ├── Cart Service
     └── Order Service
```

Potential future infrastructure:

* API Gateway
* Service-to-service REST communication
* Kafka for asynchronous events
* Distributed caching
* Independent service databases
* Saga / distributed transaction patterns
* Centralized configuration
* Service discovery
* Container orchestration

The current monolith provides a foundation for gradually exploring these concepts rather than introducing them prematurely.

---

# 📚 What This Project Demonstrates

This project was built to gain practical experience with:

* Designing RESTful backend APIs
* Spring Boot application architecture
* Spring Data JPA and Hibernate
* PostgreSQL database integration
* JWT authentication
* Spring Security
* Role-based authorization
* Password hashing with BCrypt
* DTO-based API design
* Bean validation
* Redis caching
* Cache-aside patterns
* Cache invalidation
* Docker multi-stage builds
* Docker Compose
* Environment-based configuration
* Cloud PostgreSQL
* Backend deployment
* Application monitoring with Actuator

---

# 👨‍💻 Author

**Anubhav Biswas**

Computer Science Engineering Student
Backend / Full-Stack Developer

### Links

* GitHub: https://github.com/anuPhoenixbis
* Project Repository: https://github.com/anuPhoenixbis/ecom-app
* Live API: https://ecom-app-gbst.onrender.com/

---

## ⭐ If you found this project useful

Feel free to explore the repository, raise an issue, or suggest improvements.
