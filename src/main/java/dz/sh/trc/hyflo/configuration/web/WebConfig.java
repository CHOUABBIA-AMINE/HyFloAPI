/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: WebConfig
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-06-2025
 *
 *	@Type		: Class
 *	@Layer		: Configuration
 *	@Package	: Configuration
 *
 **/

package dz.sh.trc.hyflo.configuration.web;

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