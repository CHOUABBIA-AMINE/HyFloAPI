/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: WebConfig
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-06-2025 (Spring Boot 4.0 - Properties Only)
 *
 *	@Type		: Class
 *	@Layer		: Configuration
 *	@Package	: Configuration
 *
 **/

package dz.mdn.iaas.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web Configuration Class - Spring Boot 4.0 Compatible
 * 
 * CLEAN APPROACH - No Jackson configuration in Java code
 * All Jackson settings are in application.properties
 * 
 * This is the RECOMMENDED approach for Spring Boot 4.0:
 * - No deprecated APIs
 * - Simple and maintainable
 * - Spring Boot handles all Jackson configuration
 * - JavaTimeModule auto-registered
 * 
 * Features:
 * - CORS configuration from application.properties
 * - Static resource handling
 * - Zero Jackson code (all in properties)
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed.origins:http://localhost:3000,http://localhost:4200}")
    private String allowedOrigins;

    @Value("${cors.allowed.methods:GET,POST,PUT,DELETE,PATCH,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.allow.credentials:true}")
    private boolean allowCredentials;

    @Value("${cors.max.age:3600}")
    private long maxAge;

    /**
     * Configure CORS settings for API endpoints
     * Reads configuration from application.properties
     */
    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOrigins.split(","))
                .allowedMethods(allowedMethods.split(","))
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Content-Disposition", "X-Total-Count")
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }*/

    /**
     * Configure static resource handlers
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static resources
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // Serve API documentation
        registry.addResourceHandler("/docs/**")
                .addResourceLocations("classpath:/static/docs/");

        // Serve uploaded files (if applicable)
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:f:/files/");
    }

    /*
     * NO JACKSON CONFIGURATION HERE!
     * 
     * All Jackson settings are configured in application.properties:
     * - spring.jackson.serialization.write-dates-as-timestamps=false
     * - spring.jackson.default-property-inclusion=non_null
     * - etc.
     * 
     * This approach avoids ALL deprecated Jackson APIs and is
     * the recommended way for Spring Boot 4.0
     */
}