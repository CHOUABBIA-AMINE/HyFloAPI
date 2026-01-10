/**
 *	
 *	@Author		: CHOUABBIA Amine
 *
 *	@Name		: OpenAPIConfig
 *	@CreatedOn	: 01-10-2026
 *	@UpdatedOn	: 01-10-2026
 *
 *	@Type		: Class
 *	@Layer		: Configuration
 *	@Package	: Configuration
 *
 **/

package dz.sh.trc.hyflo.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${hyflo.openapi.dev-url:http://localhost:8080}")
    private String devUrl;

    @Value("${hyflo.openapi.prod-url:https://api.hyflo.dz}")
    private String prodUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        // Define JWT Security Scheme
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .description("JWT Authentication Token. Format: Bearer {token}");

        // Define Security Requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Authentication");

        // Development Server
        Server devServer = new Server()
                .url(devUrl)
                .description("Development Environment");

        // Production Server
        Server prodServer = new Server()
                .url(prodUrl)
                .description("Production Environment");

        // Contact Information
        Contact contact = new Contact()
                .name("HyFlo Development Team")
                .email("support@trc.dz")
                .url("https://trc.dz");

        // License Information
        License license = new License()
                .name("Proprietary License")
                .url("https://trc.dz/license");

        // API Information
        Info info = new Info()
                .title("HyFlo API - Hydrocarbon Flow Management System")
                .version("0.1.0")
                .description(
                        "# HyFlo API Documentation\n\n" +
                        "Comprehensive REST API for managing hydrocarbon pipeline networks, flow monitoring, and operations.\n\n" +
                        "## Features\n" +
                        "- **Authentication & Authorization**: JWT-based authentication with role-based access control (RBAC) and attribute-based access control (ABAC)\n" +
                        "- **Network Management**: Manage pipelines, stations, terminals, equipment, and infrastructure\n" +
                        "- **Flow Monitoring**: Track production, consumption, transportation, volume, and pressure data\n" +
                        "- **User Management**: Complete user, role, group, and permission management\n" +
                        "- **Geospatial Data**: Location-based services with support for countries, cities, and geographical coordinates\n" +
                        "- **Organization**: Employee and department management\n\n" +
                        "## Authentication\n" +
                        "Most endpoints require authentication. Use the `/auth/login` endpoint to obtain a JWT token, then include it in the `Authorization` header:\n\n" +
                        "```\n" +
                        "Authorization: Bearer <your-jwt-token>\n" +
                        "```\n\n" +
                        "## API Modules\n" +
                        "- **Auth**: Login, registration, token refresh\n" +
                        "- **Security**: Users, roles, groups, permissions, authorities\n" +
                        "- **Network Core**: Pipelines, stations, terminals, equipment, facilities, fields\n" +
                        "- **Network Flow**: Flow measurements (produced, consumed, transported, volume, pressure)\n" +
                        "- **Localization**: Countries, cities, communes, regions\n" +
                        "- **Organization**: Employees, departments\n" +
                        "- **Types**: Domain type definitions\n\n" +
                        "## Pagination & Sorting\n" +
                        "List endpoints support pagination and sorting via query parameters:\n" +
                        "- `page`: Page number (default: 0)\n" +
                        "- `size`: Items per page (default: 20)\n" +
                        "- `sortBy`: Field to sort by (default: id)\n" +
                        "- `sortDir`: Sort direction - asc or desc (default: asc)\n\n" +
                        "## Error Handling\n" +
                        "The API uses standard HTTP status codes:\n" +
                        "- `200 OK`: Successful request\n" +
                        "- `201 Created`: Resource created successfully\n" +
                        "- `400 Bad Request`: Invalid request data\n" +
                        "- `401 Unauthorized`: Missing or invalid authentication\n" +
                        "- `403 Forbidden`: Insufficient permissions\n" +
                        "- `404 Not Found`: Resource not found\n" +
                        "- `500 Internal Server Error`: Server error\n\n" +
                        "Error responses include detailed error messages and validation errors when applicable."
                )
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer))
                .addSecurityItem(securityRequirement)
                .schemaRequirement("Bearer Authentication", securityScheme);
    }
}