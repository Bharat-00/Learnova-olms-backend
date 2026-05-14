# Learnova — Online Learning Management System

A production-style microservices-based learning platform built using Spring Boot, Spring Cloud, Docker, RabbitMQ, Redis, and MySQL.

The project is designed around scalable distributed system principles where every service is independently deployable, independently scalable, and communicates through both synchronous and asynchronous mechanisms.

---

# Project Overview

Learnova is designed to simulate how modern enterprise learning platforms are built in real-world production environments.

Instead of building a monolithic application, the system is divided into multiple independent microservices responsible for authentication, courses, enrollments, assessments, payments, notifications, certificates, and progress tracking.

The architecture focuses on:

- Scalability
- Fault isolation
- Service independence
- Distributed communication
- API security
- Containerized deployment
- Enterprise-grade backend practices

---

# Architecture

```text
                        +-------------------+
                        |      Client       |
                        +---------+---------+
                                  |
                                  v
                        +-------------------+
                        |    API Gateway    |
                        +---------+---------+
                                  |
             -------------------------------------------------
             |         |         |         |         |       |
             v         v         v         v         v       v

      +-----------+ +-----------+ +-----------+ +-----------+
      |   Auth    | |  Course   | | Enrollment| |  Lesson   |
      |  Service  | |  Service  | |  Service  | |  Service  |
      +-----------+ +-----------+ +-----------+ +-----------+

      +-----------+ +-----------+ +-----------+ +-----------+
      | Assesment | | Progress  | |  Payment  | | Certificate|
      |  Service  | |  Service  | |  Service  | |  Service   |
      +-----------+ +-----------+ +-----------+ +-----------+

                           +-------------------+
                           | Notification Svc  |
                           +-------------------+

                                  |
                                  v

                     +--------------------------+
                     |     RabbitMQ / Redis     |
                     +--------------------------+

                                  |
                                  v

                     +--------------------------+
                     |   Independent Databases  |
                     +--------------------------+
```

---

# Core Infrastructure

| Component | Purpose |
|---|---|
| Eureka Discovery Server | Service registration & discovery |
| API Gateway | Centralized routing & JWT validation |
| RabbitMQ | Event-driven asynchronous communication |
| Redis | Distributed caching |
| Docker | Containerized deployment |
| MySQL | Persistent storage |

---

# Microservices

| Service | Responsibility | Port |
|---|---|---|
| Auth Service | Authentication & JWT | 8081 |
| User Service | User management | 8082 |
| Course Service | Course management | 8083 |
| Enrollment Service | Student enrollments | 8084 |
| Lesson Service | Lesson handling | 8085 |
| Assesment Service | Quizzes & assessments | 8086 |
| Progress Service | Learning progress tracking | 8087 |
| Payment Service | Payment processing | 8088 |
| Certificate Service | Certificate generation | 8091 |
| Notification Service | Notifications & messaging | 8092 |

---

# Tech Stack

## Backend

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- OpenFeign

## Cloud & Distributed Systems

- Spring Cloud Gateway
- Eureka Discovery Server
- RabbitMQ
- Redis

## Database

- MySQL 8

## DevOps

- Docker
- Docker Compose
- Maven Multi-Module Architecture

## Documentation & Monitoring

- Swagger / OpenAPI
- Spring Boot Actuator
- JaCoCo

---

# Key Features

### Authentication & Authorization

- JWT-based authentication
- Role-based authorization
- Secure API Gateway token validation

### Course Ecosystem

- Course creation & management
- Lesson handling
- Enrollment workflows
- Assessment system

### Student Experience

- Progress tracking
- Certificate generation
- Notification system
- Course completion workflows

### Distributed Communication

#### Synchronous

- OpenFeign Clients

#### Asynchronous

- RabbitMQ Event Messaging

---

# Enterprise-Level Implementations

- Centralized API Gateway
- Eureka-based service discovery
- Global exception handling
- Standardized API responses
- Logging standardization
- Distributed architecture
- Independent service scalability
- Dockerized deployment
- Health checks
- Multi-module Maven architecture
- Unit testing with Mockito & MockMvc
- JaCoCo code coverage integration

---

# Running the Project

## Build Entire Project

```bash
mvn clean install
```

---

## Start Using Docker

```bash
docker compose up -d --build
```

---

## Stop Containers

```bash
docker compose down
```

---

# API Documentation

Swagger UI examples:

```text
http://localhost:8081/swagger-ui.html
```

```text
http://localhost:8083/swagger-ui.html
```

```text
http://localhost:8091/swagger-ui.html
```

---

# Health Monitoring

Example actuator endpoint:

```text
http://localhost:8081/actuator/health
```

---

# Testing

## Run Unit Tests

```bash
mvn test
```

---

## Generate Coverage Report

```bash
mvn clean verify
```

JaCoCo report:

```text
target/site/jacoco/index.html
```

---

# Future Improvements

- CI/CD pipelines
- Kubernetes deployment
- Centralized logging
- Distributed tracing
- Prometheus & Grafana monitoring
- Rate limiting
- Circuit breakers
- ELK Stack integration
- API analytics

---

# Project Highlights

- Production-style distributed microservices architecture
- Real-world backend engineering concepts
- Independent databases per service
- Event-driven communication using RabbitMQ
- Secure JWT authentication system
- Fully containerized deployment workflow
- Enterprise-grade backend structure