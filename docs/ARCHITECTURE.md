# Learnova OLMS Architecture

Learnova OLMS is a production-level Online Learning Management System built using Spring Boot microservices architecture.

## Architecture Overview

The system follows a distributed microservices-based design where each business capability is separated into an independent service. Each service owns its own database and communicates with other services using synchronous and asynchronous communication.

## Core Components

| Component | Purpose |
|---|---|
| Discovery Server | Service registry using Eureka |
| API Gateway | Single entry point for client requests |
| Auth Service | Login, registration, JWT generation |
| User Service | User profile and user management |
| Course Service | Course CRUD and course details |
| Enrollment Service | Student course enrollment |
| Lesson Service | Lesson/module management |
| Assesment Service | Quiz/assessment management |
| Progress Service | Track course progress and completion |
| Payment Service | Payment records and purchase flow |
| Discussion Service | Course discussions and comments |
| Notification Service | In-app notifications |
| Certificate Service | PDF certificate generation and download |

## Service Ports

| Service | Port |
|---|---|
| discovery-server | 8761 |
| api-gateway | 8080 |
| auth-service | 8081 |
| user-service | 8082 |
| course-service | 8083 |
| enrollment-service | 8084 |
| lesson-service | 8085 |
| assesment-service | 8086 |
| progress-service | 8087 |
| payment-service | 8088 |
| discussion-service | 8089 |
| notification-service | 8090 |
| certificate-service | 8091 |
| RabbitMQ Dashboard | 15672 |
| Redis | 6379 |

## Database Design

Each service has its own MySQL database.

| Service | Database |
|---|---|
| auth-service | learnova_auth_db |
| user-service | learnova_user_db |
| course-service | learnova_course_db |
| enrollment-service | learnova_enrollment_db |
| lesson-service | learnova_lesson_db |
| assesment-service | learnova_assesment_db |
| progress-service | learnova_progress_db |
| payment-service | learnova_payment_db |
| discussion-service | learnova_discussion_db |
| notification-service | learnova_notification_db |
| certificate-service | learnova_certificate_db |

## Communication

### Synchronous Communication

Services use OpenFeign for direct service-to-service calls.

Examples:

- Certificate Service calls Progress Service to validate course completion.
- Payment Service can communicate with Course Service.
- Enrollment Service can communicate with Course/User related services.

### Asynchronous Communication

RabbitMQ is used for event-driven communication.

Examples:

- Certificate Service publishes a certificate generated event.
- Notification Service consumes the event and stores a notification.

## Security

- Auth Service generates JWT tokens.
- API Gateway acts as the main entry point.
- Services validate JWT where required.
- Swagger and Actuator endpoints can be configured as public or protected.
- Role-based authorization can be applied for ADMIN, INSTRUCTOR, and STUDENT.

## Caching

Redis is used for caching frequently accessed data.

Examples:

- Certificate Service caches user certificates.
- Course Service can cache course details.
- User Service can cache profile data.

## Deployment

The project supports Docker Compose based local deployment.

Main infrastructure:

- MySQL containers
- RabbitMQ
- Redis
- Eureka Discovery Server
- API Gateway
- Individual Spring Boot microservices

## Technology Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3 |
| Language | Java 17 |
| Service Discovery | Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Database | MySQL |
| ORM | Spring Data JPA |
| Security | Spring Security + JWT |
| Communication | OpenFeign, RabbitMQ |
| Caching | Redis |
| Documentation | Swagger/OpenAPI |
| Testing | JUnit, Mockito, MockMvc |
| Quality | JaCoCo, SonarQube |
| Deployment | Docker, Docker Compose |