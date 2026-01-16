# HyFloAPI - Hydrocarbon Flow API Documentation

## Overview

HyFloAPI is a comprehensive REST API designed for managing hydrocarbon flow networks, including pipelines, facilities, equipment, and real-time flow measurements. The API provides secure endpoints for authentication, infrastructure management, and flow data operations.

**Version:** 0.1.0  
**Base URL:** `http://localhost:8080/hyflo/api`  
**Production URL:** `https://api.hyflo.dz`

---

## Architecture

### Technology Stack

#### Core Framework
- **Spring Boot 4.0.1** - Main application framework
- **Java 17** - Programming language
- **Maven** - Dependency management and build tool

#### Data & Persistence
- **Spring Data JPA** - ORM and database abstraction
- **Hibernate** - JPA implementation
- **MySQL** - Relational database
- **HikariCP** - High-performance connection pooling

#### Security & Authentication
- **Spring Security** - Security framework
- **JWT (JSON Web Tokens)** - Authentication mechanism
  - jjwt-api 0.13.0
  - Access token expiration: 1 hour (3600000ms)
  - Refresh token expiration: 24 hours (86400000ms)

#### Validation & Data Processing
- **Spring Validation** - Request validation
- **Lombok** - Code generation for POJOs
- **Jackson** - JSON serialization/deserialization

#### Documentation
- **SpringDoc OpenAPI 2.3.0** - API documentation and Swagger UI
- **Swagger UI** - Interactive API documentation

#### Monitoring
- **Spring Boot Actuator** - Application health monitoring and metrics
- **Prometheus** - Metrics export support

---

## Application Architecture

### Layer Structure

```
dz.sh.trc.hyflo/
├── configuration/       # Application configuration classes
├── exception/          # Custom exception handlers
├── general/            # General-purpose utilities
├── network/            # Core business domain
│   ├── common/        # Common entities (Partner, Vendor, Product, etc.)
│   ├── core/          # Infrastructure entities (Pipeline, Facility, Equipment, etc.)
│   ├── flow/          # Flow measurement entities
│   └── type/          # Type/lookup entities
└── system/            # System-level functionality
    ├── audit/         # Audit logging
    ├── auth/          # Authentication & authorization
    ├── security/      # Security configuration
    └── utility/       # System utilities
```

### Database Configuration

- **Database:** MySQL
- **Connection Pool:** HikariCP with optimized settings
  - Minimum idle connections: 5
  - Maximum pool size: 20
  - Connection timeout: 30 seconds
- **Schema Management:** Hibernate auto-update
- **Performance Optimizations:**
  - Prepared statement caching
  - Batch processing enabled
  - Query plan caching

---

## API Endpoints

### Authentication & Authorization

**Base Path:** `/auth`

| Method | Endpoint | Description | Authentication Required |
|--------|----------|-------------|------------------------|
| POST | `/auth/login` | User login with credentials | No |
| POST | `/auth/register` | Register new user account | No |
| POST | `/auth/refresh` | Refresh access token using refresh token | No |
| POST | `/auth/logout` | Logout and invalidate tokens | Yes |

**Login Request Example:**
```json
{
  "username": "user@example.com",
  "password": "password123"
}
```

**Login Response Example:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

---

### Core Infrastructure Endpoints

**Base Path:** `/network/core`

#### Pipeline System Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/pipeline-systems` | List all pipeline systems |
| GET | `/pipeline-systems/{id}` | Get specific pipeline system |
| POST | `/pipeline-systems` | Create new pipeline system |
| PUT | `/pipeline-systems/{id}` | Update pipeline system |
| DELETE | `/pipeline-systems/{id}` | Delete pipeline system |

#### Pipeline Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/pipelines` | List all pipelines |
| GET | `/pipelines/{id}` | Get specific pipeline |
| POST | `/pipelines` | Create new pipeline |
| PUT | `/pipelines/{id}` | Update pipeline |
| DELETE | `/pipelines/{id}` | Delete pipeline |

#### Pipeline Segment Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/pipeline-segments` | List all pipeline segments |
| GET | `/pipeline-segments/{id}` | Get specific segment |
| POST | `/pipeline-segments` | Create new segment |
| PUT | `/pipeline-segments/{id}` | Update segment |
| DELETE | `/pipeline-segments/{id}` | Delete segment |

#### Facility Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/facilities` | List all facilities |
| GET | `/facilities/{id}` | Get specific facility |
| POST | `/facilities` | Create new facility |
| PUT | `/facilities/{id}` | Update facility |
| DELETE | `/facilities/{id}` | Delete facility |

#### Infrastructure Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/infrastructures` | List all infrastructures |
| GET | `/infrastructures/{id}` | Get specific infrastructure |
| POST | `/infrastructures` | Create new infrastructure |
| PUT | `/infrastructures/{id}` | Update infrastructure |
| DELETE | `/infrastructures/{id}` | Delete infrastructure |

#### Equipment Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/equipment` | List all equipment |
| GET | `/equipment/{id}` | Get specific equipment |
| POST | `/equipment` | Create new equipment |
| PUT | `/equipment/{id}` | Update equipment |
| DELETE | `/equipment/{id}` | Delete equipment |

#### Production Field Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/production-fields` | List all production fields |
| GET | `/production-fields/{id}` | Get specific production field |
| POST | `/production-fields` | Create new production field |
| PUT | `/production-fields/{id}` | Update production field |
| DELETE | `/production-fields/{id}` | Delete production field |

#### Processing Plant Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/processing-plants` | List all processing plants |
| GET | `/processing-plants/{id}` | Get specific processing plant |
| POST | `/processing-plants` | Create new processing plant |
| PUT | `/processing-plants/{id}` | Update processing plant |
| DELETE | `/processing-plants/{id}` | Delete processing plant |

#### Station Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/stations` | List all stations |
| GET | `/stations/{id}` | Get specific station |
| POST | `/stations` | Create new station |
| PUT | `/stations/{id}` | Update station |
| DELETE | `/stations/{id}` | Delete station |

#### Terminal Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/terminals` | List all terminals |
| GET | `/terminals/{id}` | Get specific terminal |
| POST | `/terminals` | Create new terminal |
| PUT | `/terminals/{id}` | Update terminal |
| DELETE | `/terminals/{id}` | Delete terminal |

---

### Flow Measurement Endpoints

**Base Path:** `/network/flow`

#### Flow Data Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/flow-produced` | Get production flow data |
| POST | `/flow-produced` | Record production flow |
| GET | `/flow-consumed` | Get consumption flow data |
| POST | `/flow-consumed` | Record consumption flow |
| GET | `/flow-transported` | Get transportation flow data |
| POST | `/flow-transported` | Record transportation flow |
| GET | `/flow-volume` | Get volume flow data |
| POST | `/flow-volume` | Record volume flow |
| GET | `/flow-pressure` | Get pressure measurements |
| POST | `/flow-pressure` | Record pressure measurement |

#### Measurement Hour Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/measurement-hours` | List measurement hours |
| GET | `/measurement-hours/{id}` | Get specific measurement hour |
| POST | `/measurement-hours` | Create measurement hour |
| PUT | `/measurement-hours/{id}` | Update measurement hour |
| DELETE | `/measurement-hours/{id}` | Delete measurement hour |

#### Dashboard

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/dashboard` | Get dashboard summary data |
| GET | `/dashboard/statistics` | Get flow statistics |
| GET | `/dashboard/trends` | Get flow trends over time |

---

### Common Reference Data Endpoints

**Base Path:** `/network/common`

#### Partner Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/partners` | List all partners |
| GET | `/partners/{id}` | Get specific partner |
| POST | `/partners` | Create new partner |
| PUT | `/partners/{id}` | Update partner |
| DELETE | `/partners/{id}` | Delete partner |

#### Vendor Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/vendors` | List all vendors |
| GET | `/vendors/{id}` | Get specific vendor |
| POST | `/vendors` | Create new vendor |
| PUT | `/vendors/{id}` | Update vendor |
| DELETE | `/vendors/{id}` | Delete vendor |

#### Product Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/products` | List all products |
| GET | `/products/{id}` | Get specific product |
| POST | `/products` | Create new product |
| PUT | `/products/{id}` | Update product |
| DELETE | `/products/{id}` | Delete product |

#### Alloy Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/alloys` | List all alloys |
| GET | `/alloys/{id}` | Get specific alloy |
| POST | `/alloys` | Create new alloy |
| PUT | `/alloys/{id}` | Update alloy |
| DELETE | `/alloys/{id}` | Delete alloy |

#### Operational Status

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/operational-statuses` | List all operational statuses |
| GET | `/operational-statuses/{id}` | Get specific status |
| POST | `/operational-statuses` | Create new status |
| PUT | `/operational-statuses/{id}` | Update status |
| DELETE | `/operational-statuses/{id}` | Delete status |

---

### Type/Lookup Endpoints

**Base Path:** `/network/type`

All type endpoints follow the same CRUD pattern:

| Entity Type | Base Path |
|-------------|-----------|
| Company Types | `/company-types` |
| Equipment Types | `/equipment-types` |
| Facility Types | `/facility-types` |
| Partner Types | `/partner-types` |
| Processing Plant Types | `/processing-plant-types` |
| Production Field Types | `/production-field-types` |
| Station Types | `/station-types` |
| Terminal Types | `/terminal-types` |
| Vendor Types | `/vendor-types` |

**Standard Operations for Each Type:**

| Method | Endpoint Pattern | Description |
|--------|-----------------|-------------|
| GET | `/{type}` | List all items |
| GET | `/{type}/{id}` | Get specific item |
| POST | `/{type}` | Create new item |
| PUT | `/{type}/{id}` | Update item |
| DELETE | `/{type}/{id}` | Delete item |

---

## Security Configuration

### Authentication Mechanism

- **Type:** JWT (JSON Web Token)
- **Header:** `Authorization: Bearer {token}`
- **Access Token Lifetime:** 1 hour
- **Refresh Token Lifetime:** 24 hours

### Security Features

- **Brute Force Protection:** Max 5 failed login attempts, 30-minute lockout
- **Password Expiration:** 90 days
- **CORS Configuration:** Enabled for specified origins
- **HTTPS:** Can be enabled via configuration
- **Object-Level Security:** Enabled
- **Audit Logging:** Enabled with 365-day retention

### Protected Endpoints

All endpoints except `/auth/login`, `/auth/register`, and `/auth/refresh` require authentication.

---

## Monitoring & Health Checks

### Actuator Endpoints

**Base Path:** `/actuator`

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Application health status |
| `/actuator/info` | Application information |
| `/actuator/metrics` | Application metrics |
| `/actuator/env` | Environment properties |
| `/actuator/configprops` | Configuration properties |
| `/actuator/loggers` | Logger configuration |

### Health Check Details

- **Liveness Probe:** Enabled at `/actuator/health/liveness`
- **Readiness Probe:** Enabled at `/actuator/health/readiness`
- **Database Health:** Automatically monitored
- **Disk Space:** Automatically monitored

---

## API Documentation Access

### Swagger UI

- **Development:** `http://localhost:8080/hyflo/api/swagger-ui.html`
- **Production:** `https://api.hyflo.dz/swagger-ui.html`

### OpenAPI Specification

- **JSON Format:** `http://localhost:8080/hyflo/api/api-docs`
- **YAML Format:** `http://localhost:8080/hyflo/api/api-docs.yaml`

### Swagger UI Features

- Interactive API testing
- Request/response examples
- Schema definitions
- Try-it-out functionality enabled
- Operation sorting by HTTP method
- Tag sorting alphabetically
- Request duration display

---

## Error Handling

### Standard Error Response Format

```json
{
  "timestamp": "2026-01-16T12:29:45.123+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for request",
  "path": "/hyflo/api/pipelines"
}
```

### Common HTTP Status Codes

| Code | Meaning | Description |
|------|---------|-------------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 204 | No Content | Successful deletion |
| 400 | Bad Request | Invalid request data |
| 401 | Unauthorized | Authentication required or failed |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource conflict (e.g., duplicate) |
| 500 | Internal Server Error | Server error |

---

## Data Models

### Common Attributes

Most entities in the system share these common attributes:

```json
{
  "id": "Long",
  "createdBy": "String",
  "createdDate": "LocalDateTime",
  "lastModifiedBy": "String",
  "lastModifiedDate": "LocalDateTime",
  "version": "Integer"
}
```

### Pagination & Sorting

List endpoints support pagination and sorting:

**Query Parameters:**
- `page` - Page number (0-indexed)
- `size` - Page size (default: 20)
- `sort` - Sort field and direction (e.g., `name,asc`)

**Example Request:**
```
GET /hyflo/api/network/core/pipelines?page=0&size=20&sort=name,asc
```

**Response Structure:**
```json
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 100,
  "totalPages": 5,
  "last": false,
  "first": true
}
```

---

## Configuration

### Environment Variables

Key configuration properties can be overridden via environment variables:

- `SERVER_PORT` - Server port (default: 8080)
- `DB_URL` - Database connection URL
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `JWT_SECRET` - JWT signing secret
- `CORS_ALLOWED_ORIGINS` - Allowed CORS origins

### Application Profiles

The application supports Spring profiles for different environments:

- `dev` - Development environment
- `prod` - Production environment
- `test` - Testing environment

**Usage:** `-Dspring.profiles.active=prod`

---

## File Upload Configuration

### Settings

- **Maximum File Size:** 50MB
- **Maximum Request Size:** 50MB
- **Upload Directory:** `C:/uploads` (configurable)
- **Allowed Extensions:** pdf, jpg, jpeg, png, gif, doc, docx, xls, xlsx, ppt, pptx, txt, csv, zip, rar

---

## Development Setup

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Running the Application

1. Clone the repository
2. Configure database connection in `application.properties`
3. Run Maven build:
   ```bash
   mvn clean install
   ```
4. Start the application:
   ```bash
   mvn spring-boot:run
   ```
5. Access Swagger UI at: `http://localhost:8080/hyflo/api/swagger-ui.html`

### Database Initialization

The application automatically creates the database schema on startup when `spring.jpa.hibernate.ddl-auto=update` is configured.

---

## Support & Contact

For issues, questions, or contributions, please refer to the repository maintainers.

**Repository:** https://github.com/CHOUABBIA-AMINE/HyFloAPI

---

## Version History

- **0.1.0** - Initial release with core functionality

---

*This documentation is automatically generated and maintained for the HyFloAPI project.*
