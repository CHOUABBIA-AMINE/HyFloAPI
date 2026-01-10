# 🚀 Swagger UI Setup & Usage Guide

## ✅ What Was Added

Your HyFlo API now has **complete OpenAPI/Swagger documentation**! Here's what was configured:

1. ✅ **Springdoc OpenAPI dependency** added to `pom.xml`
2. ✅ **OpenAPIConfig.java** created with JWT authentication
3. ✅ **Application properties** configured for Swagger UI
4. ✅ **Comprehensive API documentation** with examples
5. ✅ **README.md** and **API_DOCUMENTATION.md** created

---

## 🎯 Quick Start (5 Steps)

### Step 1: Build the Project
```bash
cd HyFloAPI
mvn clean install
```

### Step 2: Run the Application
```bash
mvn spring-boot:run
```

Wait for the message: `Started HyFloApi in X.XXX seconds`

### Step 3: Open Swagger UI
Open your browser and navigate to:
```
http://localhost:8080/hyflo/api/swagger-ui.html
```

### Step 4: Authenticate
1. Scroll to **"Auth"** section
2. Click `POST /auth/login`
3. Click **"Try it out"**
4. Enter your credentials:
   ```json
   {
     "username": "admin",
     "password": "your_password"
   }
   ```
5. Click **"Execute"**
6. Copy the **accessToken** from the response

### Step 5: Authorize Swagger
1. Click the **🔒 Authorize** button (top right)
2. In the popup, enter:
   ```
   Bearer <paste-your-token-here>
   ```
3. Click **"Authorize"**
4. Click **"Close"**

**Done!** Now all endpoints will automatically include your JWT token.

---

## 📍 Available URLs

| URL | Purpose |
|-----|----------|
| `http://localhost:8080/hyflo/api/swagger-ui.html` | Interactive Swagger UI |
| `http://localhost:8080/hyflo/api/api-docs` | OpenAPI JSON specification |
| `http://localhost:8080/hyflo/api/api-docs.yaml` | OpenAPI YAML specification |
| `http://localhost:8080/hyflo/api/actuator/health` | Health check endpoint |

---

## 💡 Using Swagger UI

### Testing an Endpoint

1. **Find your endpoint** (e.g., "Pipeline" section)
2. Click on `GET /network/core/pipeline`
3. Click **"Try it out"**
4. Fill in parameters (if any):
   - `page`: 0
   - `size`: 20
   - `sortBy`: name
   - `sortDir`: asc
5. Click **"Execute"**
6. View the response below

### Creating a New Resource

1. Find `POST /network/core/pipeline`
2. Click **"Try it out"**
3. Edit the request body:
   ```json
   {
     "code": "PL-001",
     "name": "Main Oil Pipeline",
     "diameter": 24.0,
     "length": 150.5,
     "material": "Steel",
     "maxPressure": 1200.0,
     "status": "OPERATIONAL"
   }
   ```
4. Click **"Execute"**
5. Check the response (should be **201 Created**)

### Searching/Filtering

1. Find `GET /system/security/user/search`
2. Click **"Try it out"**
3. Enter search query: `q=john`
4. Click **"Execute"**

---

## 🎨 Swagger UI Features

### Endpoint Organization
- **Auth**: Login, registration, token operations
- **Security**: Users, roles, groups, permissions
- **Network Core**: Pipelines, stations, terminals, equipment
- **Network Flow**: Flow measurements and monitoring
- **General**: Localization and organization

### Color Coding
- 🟢 **GET** (Green): Read operations
- 🟦 **POST** (Blue): Create operations
- 🟨 **PUT** (Yellow): Update operations
- 🟥 **DELETE** (Red): Delete operations

### Locked Endpoints 🔒
Endpoints with a lock icon require authentication. Make sure you've authorized first!

---

## 📖 Example Workflows

### Workflow 1: Create a User

```bash
1. Login → Get token
2. Authorize in Swagger
3. POST /system/security/user → Create user
4. GET /system/security/user/{id} → Verify creation
```

### Workflow 2: Manage Pipeline

```bash
1. Login → Get token
2. Authorize in Swagger
3. GET /network/core/pipeline → List all
4. POST /network/core/pipeline → Create new
5. PUT /network/core/pipeline/{id} → Update
6. GET /network/core/pipeline/{id} → View changes
```

### Workflow 3: Record Flow Data

```bash
1. Login → Get token
2. Authorize in Swagger
3. POST /network/flow/produced → Record production
4. POST /network/flow/consumed → Record consumption
5. GET /network/flow/measurement-hour → View aggregations
```

---

## 🔧 Configuration Details

### OpenAPIConfig.java Location
```
src/main/java/dz/sh/trc/hyflo/configuration/OpenAPIConfig.java
```

### Key Configuration Properties
```properties
# Swagger UI enabled
springdoc.swagger-ui.enabled=true

# Swagger UI path
springdoc.swagger-ui.path=/swagger-ui.html

# API docs path
springdoc.api-docs.path=/api-docs

# Custom URLs
hyflo.openapi.dev-url=http://localhost:8080/hyflo/api
hyflo.openapi.prod-url=https://api.hyflo.dz
```

### Security Scheme
- **Type**: HTTP Bearer
- **Format**: JWT
- **Header**: Authorization
- **Value Format**: `Bearer <token>`

---

## ⚡ Tips & Best Practices

### 1. Token Expiration
- Access tokens expire after **1 hour**
- Use `/auth/refresh` to get a new token without re-logging in

### 2. Pagination
Most list endpoints support pagination:
```
page=0       # First page (zero-indexed)
size=20      # Items per page
sortBy=name  # Sort field
sortDir=asc  # Sort direction (asc/desc)
```

### 3. Search
Use the search parameter for filtering:
```
q=search_term
```

### 4. Response Status Codes
- **200**: Success
- **201**: Created
- **400**: Bad request (validation error)
- **401**: Unauthorized (missing/invalid token)
- **403**: Forbidden (insufficient permissions)
- **404**: Not found
- **500**: Server error

### 5. Export OpenAPI Spec
Download the OpenAPI specification:
```bash
curl http://localhost:8080/hyflo/api/api-docs > openapi.json
curl http://localhost:8080/hyflo/api/api-docs.yaml > openapi.yaml
```

---

## 🚨 Troubleshooting

### Problem: Swagger UI shows blank page

**Solution**:
1. Check if application is running: `http://localhost:8080/hyflo/api/actuator/health`
2. Clear browser cache (Ctrl+Shift+Del)
3. Try incognito/private window
4. Check console for errors (F12)

### Problem: "Authorize" button doesn't work

**Solution**:
1. Make sure you include `Bearer ` prefix
2. Token format: `Bearer eyJhbGci...`
3. No extra spaces
4. Token must be valid (not expired)

### Problem: 401 Unauthorized on endpoints

**Solution**:
1. Click 🔒 Authorize button
2. Enter your JWT token with `Bearer ` prefix
3. Click Authorize
4. Try the endpoint again

### Problem: Can't see all endpoints

**Solution**:
1. Make sure packages are being scanned correctly
2. Check `application.properties`:
   ```properties
   springdoc.packages-to-scan=dz.sh.trc.hyflo
   springdoc.paths-to-match=/auth/**,/system/**,/network/**,/general/**
   ```
3. Restart the application

### Problem: Request validation errors

**Solution**:
1. Check the **Schema** section for required fields
2. Ensure data types match (numbers, strings, dates)
3. Follow the example format provided
4. Check min/max constraints

---

## 🎓 Advanced Features

### Custom Server URLs
Switch between development and production:
1. Click the **Servers** dropdown (top of Swagger UI)
2. Select:
   - Development: `http://localhost:8080/hyflo/api`
   - Production: `https://api.hyflo.dz`

### Download Response
Click the **Download** button next to responses to save JSON/XML

### Schema Viewer
Click **Schema** tab to see the data structure for requests/responses

### Try Multiple Requests
Open multiple endpoint panels simultaneously to compare

---

## 📚 Additional Resources

- **Full API Documentation**: [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
- **Project README**: [README.md](README.md)
- **Springdoc OpenAPI**: [https://springdoc.org/](https://springdoc.org/)
- **OpenAPI Specification**: [https://swagger.io/specification/](https://swagger.io/specification/)

---

## 🎉 What's Next?

1. ✅ Explore all API endpoints in Swagger UI
2. ✅ Test authentication and authorization
3. ✅ Create sample data via API
4. ✅ Integrate with frontend application
5. ✅ Share API documentation with team
6. ✅ Export OpenAPI spec for code generation

---

## 💬 Need Help?

- **Email**: support@trc.dz
- **GitHub Issues**: [Report an issue](https://github.com/CHOUABBIA-AMINE/HyFloAPI/issues)

---

**Happy API Testing! 🚀**

*Last Updated: January 10, 2026*