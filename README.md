# Job Portal System

AI-based job portal built as a Spring Boot multi-module microservices system. The project separates cloud infrastructure, business services, and shared code into Maven modules.

## Overview

The system supports common job portal workflows:

- User signup, login, JWT-based authentication, and user profile management
- Company profile management
- Job, category, skill, and tag management
- Resume profile management with education, work experience, projects, certifications, awards, languages, and skills
- Job applications and application notes
- Saved jobs and candidate preferences
- AI-assisted features for job descriptions, resume summaries, cover letters, candidate scoring, skill gaps, search enhancement, and career feedback

## Tech Stack

- Java 21
- Spring Boot 4.1.0
- Spring Cloud 2025.1.2
- Spring Cloud Config Server
- Netflix Eureka service discovery
- Spring Cloud Gateway MVC
- Spring Data JPA
- PostgreSQL
- OpenFeign
- JWT with JJWT
- Lombok
- Google GenAI SDK
- Maven multi-module build

## Project Structure

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
│   └── job-portal-ai-service
├── common-lib
└── pom.xml
```

## Modules

| Module | Purpose |
| --- | --- |
| `cloud/job-portal-service-registry` | Eureka server for service discovery. Runs on port `8761`. |
| `cloud/job-portal-config-server` | Centralized Spring Cloud Config server. Runs on port `8888`. |
| `cloud/job-portal-api-gateway` | API gateway for routing requests to backend services. |
| `common-lib` | Shared DTOs, validation, constants, and common support classes. |
| `services/job-portal-user-service` | Authentication, user accounts, and profiles. |
| `services/job-portal-company-service` | Company profile CRUD APIs. |
| `services/job-portal-job-service` | Job posting, categories, skills, tags, and search. |
| `services/job-portal-resume-service` | Resume and candidate profile sections. |
| `services/job-portal-application-service` | Job applications and application notes. |
| `services/job-portal-preferences-service` | Saved jobs and user job preferences. |
| `services/job-portal-ai-service` | AI helper APIs backed by Google GenAI/Gemini. |

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

## Prerequisites

- JDK 21
- Maven 3.9+ or the included Maven wrapper
- PostgreSQL
- Access to the Spring Cloud Config repository configured in `job-portal-config-server`
- Google Gemini API key for `job-portal-ai-service`

## Configuration

Most services import configuration from:

```yaml
spring:
  config:
    import: configserver:http://localhost:8888
```

The config server currently points to:

```text
https://github.com/Devendra15197/job-portal-config
```

Keep database credentials, JWT secrets, service ports, and AI provider keys in the external config repository or environment-specific configuration. Do not commit real API keys or secrets into service `application.yaml` files.

## Build

Build all modules from the project root:

```bash
./mvnw clean install
```

Skip tests:

```bash
./mvnw clean install -DskipTests
```

Build a single module with its required dependencies:

```bash
./mvnw clean install -pl services/job-portal-user-service -am
```

## Run Locally

Start services in this order:

1. Service registry
2. Config server
3. API gateway
4. Business services

Example commands:

```bash
./mvnw spring-boot:run -pl cloud/job-portal-service-registry
./mvnw spring-boot:run -pl cloud/job-portal-config-server
./mvnw spring-boot:run -pl cloud/job-portal-api-gateway
```

Run an individual business service:

```bash
./mvnw spring-boot:run -pl services/job-portal-user-service
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

Other service ports are expected to come from the external Spring Cloud Config repository.

## Development Notes

- Use the root `pom.xml` for dependency and plugin version management.
- Add shared request/response classes and reusable validation code to `common-lib`.
- Register new services with Eureka by including the Eureka client dependency and config-server import.
- Keep service-specific persistence logic inside each service module.
- Prefer externalized configuration for environment-specific values.
