# HyFlo API Documentation

## 📚 Table of Contents
- [Overview](#overview)
- [Quick Start](#quick-start)
- [Authentication](#authentication)
- [API Endpoints](#api-endpoints)
- [Using Swagger UI](#using-swagger-ui)
- [Example Requests](#example-requests)
- [Error Handling](#error-handling)
- [Troubleshooting](#troubleshooting)

---

## 🌟 Overview

**HyFlo API** is a comprehensive REST API for managing hydrocarbon pipeline networks, flow monitoring, and operations.

### Key Features
- ✅ JWT-based Authentication & Authorization
- ✅ Role-Based Access Control (RBAC)
- ✅ Attribute-Based Access Control (ABAC)
- ✅ Pipeline & Station Management
- ✅ Real-time Flow Monitoring
- ✅ User & Permission Management
- ✅ Geospatial Data Support
- ✅ Interactive Swagger Documentation

### Technology Stack
- **Framework**: Spring Boot 4.0.1
- **Language**: Java 17
- **Database**: MySQL 8.0
- **Security**: Spring Security + JWT
- **API Docs**: Swagger/OpenAPI 3.0

---

## 🚀 Quick Start

### Prerequisites
```bash
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
```

### 1. Clone the Repository
```bash
git clone https://github.com/CHOUABBIA-AMINE/HyFloAPI.git
cd HyFloAPI
```

### 2. Configure Database
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hyflo_v01
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Import Database Schema
```bash
mysql -u root -p hyflo_v01 < src/main/resources/hyflo_db.sql
```

### 4. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

### 5. Access Swagger UI
Open your browser and navigate to:
```
http://localhost:8080/hyflo/api/swagger-ui.html
```

---

## 🔐 Authentication

### Login
Obtain a JWT token by authenticating:

**Endpoint**: `POST /auth/login`

**Request Body**:
```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response**:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600000,
  "user": {
    "id": 1,
    "username": "admin",
    "email": "admin@hyflo.dz",
    "roles": ["ADMIN"]
  }
}
```

### Using the Token
Include the token in the `Authorization` header for all subsequent requests:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Token Refresh
When the access token expires, use the refresh token:

**Endpoint**: `POST /auth/refresh`

**Request Body**:
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## 📡 API Endpoints

### Authentication Module (`/auth`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/login` | User login | ❌ |
| POST | `/auth/register` | Register new user | ❌ |
| POST | `/auth/refresh` | Refresh access token | ❌ |
| POST | `/auth/logout` | User logout | ✅ |

---

### Security Module (`/system/security`)

#### Users (`/system/security/user`)

| Method | Endpoint | Description | Permission |
|--------|----------|-------------|------------|
| GET | `/system/security/user` | List all users | `USER:READ` |
| GET | `/system/security/user/{id}` | Get user by ID | `USER:READ` |
| POST | `/system/security/user` | Create new user | `USER:ADMIN` |
| PUT | `/system/security/user/{id}` | Update user | `USER:ADMIN` |
| DELETE | `/system/security/user/{id}` | Delete user | `USER:ADMIN` |
| GET | `/system/security/user/username/{username}` | Get by username | `USER:READ` |
| GET | `/system/security/user/email/{email}` | Get by email | `USER:ADMIN` |
| POST | `/system/security/user/reset-password` | Reset password | `USER:ADMIN` |
| POST | `/system/security/user/{userId}/roles/{roleId}` | Assign role | `USER:ADMIN` |
| DELETE | `/system/security/user/{userId}/roles/{roleId}` | Remove role | `USER:ADMIN` |
| PUT | `/system/security/user/{id}/enable` | Enable user | `USER:ADMIN` |
| PUT | `/system/security/user/{id}/disable` | Disable user | `USER:ADMIN` |

#### Roles (`/system/security/role`)

| Method | Endpoint | Description | Permission |
|--------|----------|-------------|------------|
| GET | `/system/security/role` | List all roles | `ROLE:READ` |
| GET | `/system/security/role/{id}` | Get role by ID | `ROLE:READ` |
| POST | `/system/security/role` | Create new role | `ROLE:ADMIN` |
| PUT | `/system/security/role/{id}` | Update role | `ROLE:ADMIN` |
| DELETE | `/system/security/role/{id}` | Delete role | `ROLE:ADMIN` |

#### Groups (`/system/security/group`)

| Method | Endpoint | Description | Permission |
|--------|----------|-------------|------------|
| GET | `/system/security/group` | List all groups | `GROUP:READ` |
| GET | `/system/security/group/{id}` | Get group by ID | `GROUP:READ` |
| POST | `/system/security/group` | Create new group | `GROUP:ADMIN` |
| PUT | `/system/security/group/{id}` | Update group | `GROUP:ADMIN` |
| DELETE | `/system/security/group/{id}` | Delete group | `GROUP:ADMIN` |

#### Permissions & Authorities

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/system/security/permission` | List all permissions |
| GET | `/system/security/authority` | List all authorities |

---

### Network Core Module (`/network/core`)

#### Pipelines (`/network/core/pipeline`)

| Method | Endpoint | Description | Permission |
|--------|----------|-------------|------------|
| GET | `/network/core/pipeline` | List all pipelines | `PIPELINE:READ` |
| GET | `/network/core/pipeline/{id}` | Get pipeline by ID | `PIPELINE:READ` |
| POST | `/network/core/pipeline` | Create new pipeline | `PIPELINE:WRITE` |
| PUT | `/network/core/pipeline/{id}` | Update pipeline | `PIPELINE:WRITE` |
| DELETE | `/network/core/pipeline/{id}` | Delete pipeline | `PIPELINE:DELETE` |

#### Stations (`/network/core/station`)

| Method | Endpoint | Description | Permission |
|--------|----------|-------------|------------|
| GET | `/network/core/station` | List all stations | `STATION:READ` |
| GET | `/network/core/station/{id}` | Get station by ID | `STATION:READ` |
| POST | `/network/core/station` | Create new station | `STATION:WRITE` |
| PUT | `/network/core/station/{id}` | Update station | `STATION:WRITE` |
| DELETE | `/network/core/station/{id}` | Delete station | `STATION:DELETE` |

#### Terminals (`/network/core/terminal`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/network/core/terminal` | List all terminals |
| GET | `/network/core/terminal/{id}` | Get terminal by ID |
| POST | `/network/core/terminal` | Create new terminal |
| PUT | `/network/core/terminal/{id}` | Update terminal |
| DELETE | `/network/core/terminal/{id}` | Delete terminal |

#### Equipment (`/network/core/equipment`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/network/core/equipment` | List all equipment |
| GET | `/network/core/equipment/{id}` | Get equipment by ID |
| POST | `/network/core/equipment` | Create new equipment |
| PUT | `/network/core/equipment/{id}` | Update equipment |
| DELETE | `/network/core/equipment/{id}` | Delete equipment |

#### Other Network Core Endpoints
- `/network/core/facility` - Facility management
- `/network/core/hydrocarbon-field` - Hydrocarbon field management
- `/network/core/infrastructure` - Infrastructure management
- `/network/core/pipeline-segment` - Pipeline segment management
- `/network/core/pipeline-system` - Pipeline system management

---

### Network Flow Module (`/network/flow`)

#### Flow Measurements

| Endpoint | Description |
|----------|-------------|
| `/network/flow/produced` | Production flow data |
| `/network/flow/consumed` | Consumption tracking |
| `/network/flow/transported` | Transportation flow |
| `/network/flow/volume` | Volume measurements |
| `/network/flow/pressure` | Pressure monitoring |
| `/network/flow/measurement-hour` | Hourly aggregations |

Each endpoint supports:
- `GET /` - List all records
- `GET /{id}` - Get by ID
- `POST /` - Create new record
- `PUT /{id}` - Update record
- `DELETE /{id}` - Delete record

---

### General Module

#### Localization (`/general/localization`)
- Countries, cities, communes, regions
- Geospatial coordinates

#### Organization (`/general/organization`)
- Employees
- Departments
- Organizational structure

---

## 📖 Using Swagger UI

### Accessing Swagger
1. Start the application
2. Navigate to: `http://localhost:8080/hyflo/api/swagger-ui.html`

### Testing Endpoints

1. **Authenticate First**:
   - Expand the **Auth** section
   - Click on `POST /auth/login`
   - Click "Try it out"
   - Enter credentials and execute
   - Copy the `accessToken` from the response

2. **Authorize in Swagger**:
   - Click the **"Authorize"** button (🔒) at the top right
   - Enter: `Bearer <your-access-token>`
   - Click "Authorize"
   - Click "Close"

3. **Test Protected Endpoints**:
   - Now all endpoints will include your JWT token automatically
   - Expand any endpoint section
   - Click "Try it out"
   - Fill in parameters and execute

### Swagger Features
- **Interactive Testing**: Test all endpoints directly from the browser
- **Request/Response Examples**: See sample data for each endpoint
- **Model Schemas**: View data structures for all DTOs
- **Authorization**: Secure endpoints marked with 🔒 icon
- **Filtering**: Search endpoints by name or tag

---

## 💡 Example Requests

### Create a New Pipeline

```bash
curl -X POST "http://localhost:8080/hyflo/api/network/core/pipeline" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "PL-001",
    "name": "Main Oil Pipeline",
    "diameter": 24.0,
    "length": 150.5,
    "material": "Steel",
    "maxPressure": 1200.0,
    "status": "OPERATIONAL"
  }'
```

### Get All Stations (with pagination)

```bash
curl -X GET "http://localhost:8080/hyflo/api/network/core/station?page=0&size=20&sortBy=name&sortDir=asc" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Search Users

```bash
curl -X GET "http://localhost:8080/hyflo/api/system/security/user/search?q=john" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Record Flow Data

```bash
curl -X POST "http://localhost:8080/hyflo/api/network/flow/produced" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "stationId": 1,
    "productId": 1,
    "volume": 5000.0,
    "measurementDate": "2026-01-10T12:00:00Z",
    "unit": "BARREL"
  }'
```

---

## ⚠️ Error Handling

### Standard Error Response

```json
{
  "timestamp": "2026-01-10T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/network/core/pipeline",
  "errors": [
    {
      "field": "diameter",
      "message": "Diameter must be greater than 0"
    }
  ]
}
```

### Common HTTP Status Codes

| Code | Meaning | Description |
|------|---------|-------------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 400 | Bad Request | Invalid request data or validation error |
| 401 | Unauthorized | Missing or invalid authentication token |
| 403 | Forbidden | User doesn't have required permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource already exists or conflict |
| 500 | Internal Server Error | Server error occurred |

---

## 🔧 Troubleshooting

### Issue: Cannot access Swagger UI

**Solution**:
1. Verify the application is running: `http://localhost:8080/hyflo/api/actuator/health`
2. Check the correct URL: `http://localhost:8080/hyflo/api/swagger-ui.html`
3. Clear browser cache

### Issue: 401 Unauthorized on protected endpoints

**Solution**:
1. Ensure you've called `/auth/login` and received a token
2. Verify token is included in Authorization header: `Bearer <token>`
3. Check token hasn't expired (default: 1 hour)
4. Use `/auth/refresh` to get a new token if expired

### Issue: 403 Forbidden error

**Solution**:
1. Your user account doesn't have the required permission
2. Check your roles and permissions in the database
3. Contact administrator to assign proper permissions

### Issue: Database connection error

**Solution**:
1. Verify MySQL is running: `mysql -u root -p`
2. Check database exists: `SHOW DATABASES;`
3. Verify credentials in `application.properties`
4. Ensure database schema is imported

### Issue: Port 8080 already in use

**Solution**:
1. Change port in `application.properties`: `server.port=8081`
2. Or kill the process using port 8080:
   - Windows: `netstat -ano | findstr :8080` then `taskkill /PID <PID> /F`
   - Linux/Mac: `lsof -ti:8080 | xargs kill -9`

---

## 📞 Support

For issues or questions:
- **Email**: support@trc.dz
- **GitHub Issues**: [Create an issue](https://github.com/CHOUABBIA-AMINE/HyFloAPI/issues)

---

## 📄 License

Proprietary - All rights reserved to TRC (Transport par Canalisation)

---

**Last Updated**: January 10, 2026  
**Version**: 0.1.0  
**Author**: CHOUABBIA Amine