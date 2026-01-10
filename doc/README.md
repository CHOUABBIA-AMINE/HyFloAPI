# HyFlo API - Hydrocarbon Flow Management System

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Swagger](https://img.shields.io/badge/Swagger-3.0-85EA2D.svg)](https://swagger.io/)
[![License](https://img.shields.io/badge/License-Proprietary-red.svg)](LICENSE)

> A comprehensive REST API for managing hydrocarbon pipeline networks, flow monitoring, and operational management.

---

## 🌟 Features

- ✅ **Authentication & Authorization**: JWT-based security with RBAC and ABAC
- ✅ **Pipeline Management**: Complete CRUD operations for pipelines, stations, and terminals
- ✅ **Flow Monitoring**: Real-time tracking of production, consumption, and transportation
- ✅ **User Management**: Comprehensive user, role, group, and permission management
- ✅ **Geospatial Support**: Location-based services with coordinate mapping
- ✅ **API Documentation**: Interactive Swagger UI for testing and exploration
- ✅ **Audit Logging**: Complete audit trail for all operations
- ✅ **Exception Handling**: Global error handling with detailed error messages

---

## 📦 Technology Stack

| Technology | Version | Purpose |
|------------|---------|----------|
| **Spring Boot** | 4.0.1 | Application framework |
| **Java** | 17 | Programming language |
| **MySQL** | 8.0+ | Database |
| **Spring Security** | Latest | Authentication & authorization |
| **JWT** | 0.13.0 | Token-based authentication |
| **Hibernate/JPA** | Latest | ORM framework |
| **Springdoc OpenAPI** | 2.3.0 | API documentation |
| **Lombok** | Latest | Boilerplate code reduction |
| **HikariCP** | Latest | Connection pooling |
| **Maven** | 3.6+ | Build tool |

---

## 🚀 Quick Start

### Prerequisites

```bash
• Java 17 or higher
• Maven 3.6+
• MySQL 8.0+
• Git
```

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/CHOUABBIA-AMINE/HyFloAPI.git
   cd HyFloAPI
   ```

2. **Create MySQL database**
   ```sql
   CREATE DATABASE hyflo_v01;
   ```

3. **Import database schema**
   ```bash
   mysql -u root -p hyflo_v01 < src/main/resources/hyflo_db.sql
   ```

4. **Configure application**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/hyflo_v01
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

5. **Build the project**
   ```bash
   mvn clean install
   ```

6. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

7. **Access the API**
   - **API Base URL**: `http://localhost:8080/hyflo/api`
   - **Swagger UI**: `http://localhost:8080/hyflo/api/swagger-ui.html`
   - **API Docs (JSON)**: `http://localhost:8080/hyflo/api/api-docs`
   - **Health Check**: `http://localhost:8080/hyflo/api/actuator/health`

---

## 📖 API Documentation

### Swagger UI

Interactive API documentation is available at:

```
http://localhost:8080/hyflo/api/swagger-ui.html
```

**Features**:
- 📝 Browse all available endpoints
- ▶️ Test endpoints directly from the browser
- 🔐 Authenticate with JWT tokens
- 📑 View request/response schemas
- 📊 See example data for all operations

### Authentication Flow

1. **Login** to get JWT token:
   ```bash
   POST /auth/login
   {
     "username": "admin",
     "password": "your_password"
   }
   ```

2. **Copy the access token** from the response

3. **Click "Authorize"** button in Swagger UI

4. **Enter**: `Bearer <your-token>`

5. **Test protected endpoints**

For detailed API documentation, see [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

---

## 📚 API Modules

### 🔐 Authentication (`/auth`)
- User login and registration
- JWT token generation and refresh
- Logout functionality

### 👥 Security (`/system/security`)
- **Users**: User management with roles and groups
- **Roles**: Role-based access control
- **Groups**: User group management
- **Permissions**: Fine-grained permission system
- **Authorities**: Authority management

### 🛍️ Network Core (`/network/core`)
- **Pipelines**: Pipeline CRUD operations
- **Stations**: Station management
- **Terminals**: Terminal operations
- **Equipment**: Equipment tracking
- **Facilities**: Facility management
- **Hydrocarbon Fields**: Field operations
- **Infrastructure**: Infrastructure management
- **Pipeline Segments**: Segment tracking
- **Pipeline Systems**: System-level management

### 💧 Network Flow (`/network/flow`)
- **Flow Produced**: Production tracking
- **Flow Consumed**: Consumption monitoring
- **Flow Transported**: Transportation data
- **Flow Volume**: Volume measurements
- **Flow Pressure**: Pressure monitoring
- **Measurement Hour**: Hourly aggregations

### 🌍 General
- **Localization**: Countries, cities, communes, regions
- **Organization**: Employees and departments
- **Types**: Domain type definitions

---

## 💻 Development

### Project Structure

```
HyFloAPI/
├── src/
│   ├── main/
│   │   ├── java/dz/sh/trc/hyflo/
│   │   │   ├── configuration/      # App configuration
│   │   │   │   ├── OpenAPIConfig.java
│   │   │   │   └── template/          # Generic base classes
│   │   │   ├── exception/          # Global exception handling
│   │   │   ├── system/
│   │   │   │   ├── auth/              # Authentication
│   │   │   │   ├── security/          # User/Role management
│   │   │   │   └── audit/             # Audit logging
│   │   │   ├── network/
│   │   │   │   ├── core/              # Pipeline/Station mgmt
│   │   │   │   └── flow/              # Flow monitoring
│   │   │   └── general/            # Localization/Org
│   │   └── resources/
│   │       ├── application.properties
│   │       └── hyflo_db.sql        # Database schema
│   └── test/
├── pom.xml
├── README.md
└── API_DOCUMENTATION.md
```

### Architecture Pattern

```
Controller → Service → Repository → Database
    ↑          ↑           ↑
   DTO      Business     Entity
           Logic       (JPA)
```

### Adding New Endpoints

1. **Create Entity** (extends `GenericModel`)
2. **Create DTO** (extends `GenericDTO`)
3. **Create Repository** (extends `JpaRepository`)
4. **Create Service** (extends `GenericService`)
5. **Create Controller** (extends `GenericController`)

Example:
```java
@RestController
@RequestMapping("/my-module/my-entity")
public class MyEntityController extends GenericController<MyEntityDTO, Long> {
    public MyEntityController(MyEntityService service) {
        super(service, "MyEntity");
    }
}
```

---

## 🛡️ Security

### Authentication
- JWT-based token authentication
- Access token expiration: 1 hour
- Refresh token expiration: 24 hours

### Authorization
- **RBAC**: Role-Based Access Control
- **ABAC**: Attribute-Based Access Control
- Method-level security with `@PreAuthorize`

### Permissions Format
```
<ENTITY>:<ACTION>

Examples:
- USER:READ
- USER:WRITE
- USER:DELETE
- PIPELINE:READ
- PIPELINE:ADMIN
```

---

## 📊 Database Schema

- **50+ tables** with comprehensive relationships
- **Foreign keys** with proper constraints
- **Unique constraints** on critical fields
- **Audit fields** (created_by, created_at, updated_by, updated_at)
- **Sample data** included in Excel files

---

## 🧠 Testing

### Run Tests
```bash
mvn test
```

### Test Coverage
```bash
mvn clean test jacoco:report
```

---

## 🚀 Deployment

### Build JAR
```bash
mvn clean package
```

### Run JAR
```bash
java -jar target/HyFfloAPI-0.1.0.jar
```

### Docker (Coming Soon)
```bash
docker build -t hyflo-api .
docker run -p 8080:8080 hyflo-api
```

---

## 👥 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📝 Changelog

### Version 0.1.0 (2026-01-10)
- ✅ Initial release
- ✅ Complete authentication system
- ✅ User and role management
- ✅ Pipeline and station management
- ✅ Flow monitoring capabilities
- ✅ Swagger documentation

---

## 🐛 Known Issues

- Test coverage needs improvement
- WebSocket support for real-time monitoring (planned)
- Data import tools (in progress)

---

## 📞 Support

- **Email**: support@trc.dz
- **GitHub Issues**: [Create an issue](https://github.com/CHOUABBIA-AMINE/HyFloAPI/issues)
- **Documentation**: [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

---

## 📄 License

Proprietary - All rights reserved to TRC (Transport par Canalisation)

---

## 👨‍💻 Authors

- **CHOUABBIA Amine** - *Initial work* - [@CHOUABBIA-AMINE](https://github.com/CHOUABBIA-AMINE)
- **MEDJERAB Abir** - *Core development*

---

## 🚀 Roadmap

- [ ] Unit and integration tests
- [ ] WebSocket support for real-time data
- [ ] Data import/export tools
- [ ] Advanced analytics and reporting
- [ ] Mobile app API support
- [ ] Notification system
- [ ] File upload service
- [ ] Backup and restore utilities

---

**Made with ❤️ by TRC Development Team**

**Last Updated**: January 10, 2026  
**Version**: 0.1.0