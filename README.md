# Job Portal System

AI-powered job portal built as a Spring Boot multi-module microservice platform. The project includes cloud infrastructure, service discovery, API gateway routing, shared libraries, and multiple business services for a complete hiring workflow.

## Overview

This platform supports the full lifecycle of a job portal:

- User authentication, authorization, and profile management
- Company onboarding and company profile management
- Job posting, category, skill, and tag management
- Resume creation and management with education, work experience, projects, certifications, awards, languages, and skills
- Job application tracking and notes
- Saved jobs and candidate preference management
- AI-powered assistance for job descriptions, resumes, cover letters, candidate scoring, skill gaps, search enhancement, and career guidance
- Kafka-driven email notifications for application status updates and workflow communication

## Current Architecture

```text
job-portal-system
├── cloud
│   ├── job-portal-service-registry
│   ├── job-portal-config-server
│   └── job-portal-api-gateway
├── services
│   ├── job-portal-user-service
│   ├── job-portal-company-service
│   ├── job-portal-job-service
│   ├── job-portal-resume-service
│   ├── job-portal-application-service
│   ├── job-portal-preferences-service
│   ├── job-portal-ai-service
│   └── job-portal-notification-service
├── common-lib
├── pom.xml
└── README.md
```

## Tech Stack

- Java 21
- Spring Boot 4.1.0
- Spring Cloud 2025.1.2
- Spring Cloud Config Server
- Netflix Eureka service discovery
- Spring Cloud Gateway MVC
- Spring Data JPA
- PostgreSQL
- Kafka
- Spring Mail
- OpenFeign
- JWT with JJWT
- Lombok
- Google GenAI SDK
- Maven multi-module build

## Modules

| Module | Purpose |
| --- | --- |
| `cloud/job-portal-service-registry` | Eureka server for service registration and discovery. Runs on port `8761`. |
| `cloud/job-portal-config-server` | Spring Cloud Config server for externalized configuration. Runs on port `8888`. |
| `cloud/job-portal-api-gateway` | Central API entry point for routing requests to backend services. |
| `common-lib` | Shared DTOs, validation utilities, constants, and common service support code. |
| `services/job-portal-user-service` | Authentication, users, profiles, and account data. |
| `services/job-portal-company-service` | Company profile and company-related APIs. |
| `services/job-portal-job-service` | Job management, categories, skills, tags, and job search. |
| `services/job-portal-resume-service` | Resume and candidate profile sections. |
| `services/job-portal-application-service` | Job applications and application notes. |
| `services/job-portal-preferences-service` | Saved jobs and user preference workflows. |
| `services/job-portal-ai-service` | AI features backed by Google GenAI/Gemini. |
| `services/job-portal-notification-service` | Kafka-based email notification service for status change events and alerts. |

## Core Features

### User & Access Management

- Sign up and login
- JWT-based authentication and authorization
- User profile management
- Role-aware access patterns for application workflows

### Hiring Workflow

- Company management
- Job creation and management
- Categories, skills, and tags
- Resume and profile management
- Candidate applications and notes
- Saved jobs and preference tracking

### AI Capabilities

- AI-generated job descriptions
- Resume summaries and enhancement suggestions
- Cover letter generation
- Candidate scoring
- Skill gap analysis
- Search assistance and career guidance
- AI-powered application insights

### Notifications

- Kafka event-driven messaging
- Email notifications for application updates
- Status-based communication between services
- Email templates for user-facing updates

## Main API Areas

- `POST /auth/signup`
- `POST /auth/login`
- `/api/users`
- `/api/companies`
- `/api/jobs`
- `/api/job-categories`
- `/api/job-skills`
- `/api/job-tags`
- `/api/resumes`
- `/api/resumes/{resumeId}/educations`
- `/api/resumes/{resumeId}/work-experiences`
- `/api/resumes/{resumeId}/projects`
- `/api/resumes/{resumeId}/certifications`
- `/api/resumes/{resumeId}/awards`
- `/api/resumes/{resumeId}/languages`
- `/api/resumes/{resumeId}/skills`
- `/api/applications`
- `/api/application-notes`
- `/api/preferences/saved-jobs`
- `/api/ai`
- `/api/ai/job`
- `/api/ai/resume`
- `/api/ai/application`
- `/api/notifications`

## Prerequisites

- JDK 21
- Maven 3.9+ or the included Maven wrapper
- PostgreSQL
- Kafka broker
- SMTP email provider (for notification service)
- Spring Cloud Config repository access
- Google Gemini API key for `job-portal-ai-service`

## Configuration

Most services load configuration from Spring Cloud Config:

```yaml
spring:
  config:
    import: configserver:http://localhost:8888
```

The config server references:

```text
https://github.com/Devendra15197/job-portal-config
```

Keep the following in external config or environment variables:

- database credentials
- JWT secrets
- service ports
- Kafka bootstrap servers
- SMTP credentials
- Google AI credentials

Do not commit real secrets or API keys into service configuration files.

## Build

Build the entire project from the root:

```bash
./mvnw clean install
```

Skip tests:

```bash
./mvnw clean install -DskipTests
```

Build a specific service and its required dependencies:

```bash
./mvnw clean install -pl services/job-portal-user-service -am
```

## Run Locally

Start infrastructure services first:

1. Service registry
2. Config server
3. API gateway
4. Business services
5. Notification service

Example commands:

```bash
./mvnw spring-boot:run -pl cloud/job-portal-service-registry
./mvnw spring-boot:run -pl cloud/job-portal-config-server
./mvnw spring-boot:run -pl cloud/job-portal-api-gateway
./mvnw spring-boot:run -pl services/job-portal-user-service
./mvnw spring-boot:run -pl services/job-portal-notification-service
```

Run a business service individually:

```bash
./mvnw spring-boot:run -pl services/job-portal-ai-service
```

Run tests:

```bash
./mvnw test
```

## Default Infrastructure Ports

| Service | Port |
| --- | --- |
| Eureka service registry | `8761` |
| Config server | `8888` |
| API gateway | configured via external config |
| Database services | configured via external config |
| Kafka broker | `9092` by default for local notification setup |

## Environment Variables

For local development, the notification service commonly uses:

```bash
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USERNAME=your-email
export MAIL_PASSWORD=your-app-password
```

For AI service integration, configure the relevant Google AI credentials through the config server or environment.

## Development Notes

- Use the root `pom.xml` for shared dependency and plugin management.
- Add reusable DTOs, validation helpers, and common utilities to `common-lib`.
- Keep service-specific business logic and persistence inside each microservice.
- Use Spring Cloud Config for environment-sensitive values.
- Prefer event-driven communication for async workflows like notifications.
- Keep deployment configuration externalized rather than hard-coded in the source tree.

## Contribution

This is a multi-module microservice project. When adding a new feature or module:

- register the module in the appropriate parent `pom.xml`
- keep the service isolated and focused on one business domain
- document API contracts and environment dependencies
- update this README when introducing new modules, workflows, or infrastructure components
