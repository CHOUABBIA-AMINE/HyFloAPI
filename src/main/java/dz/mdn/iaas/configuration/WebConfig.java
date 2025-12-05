/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: WebConfig
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Configuration
 *	@Package	: Configuration
 *
 **/

package dz.mdn.iaas.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Web Configuration Class
 * 
 * Configures REST API settings, CORS, message converters,
 * and other web-related configurations separated from data layer.
 * 
 * Features:
 * - CORS configuration for cross-origin requests
 * - JSON message converter with proper date handling
 * - Static resource handling
 * - Custom error handling integration
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure CORS settings for API endpoints
     * Allows cross-origin requests from frontend applications
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * Configure static resource handlers
     * Serves static content like documentation, images, etc.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/docs/**")
                .addResourceLocations("classpath:/static/docs/");
    }

    /**
     * Configure HTTP message converters
     * Customizes JSON serialization/deserialization
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter() {
            @Override
            public boolean supports(Class<?> clazz) {
                return UrlResource.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * Custom Jackson message converter with proper date handling
     * and JSON formatting configuration
     */
    @Bean
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    /**
     * ObjectMapper configuration for consistent JSON handling
     * Includes Java 8 time support and proper formatting
     */
    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Register Java Time module for proper date/time serialization
        mapper.registerModule(new JavaTimeModule());

        // Configure serialization features
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper;
    }
}