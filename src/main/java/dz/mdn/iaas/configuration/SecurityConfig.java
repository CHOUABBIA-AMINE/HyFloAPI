/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: SecurityConfig
 *	@CreatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Configuration
 *	@Package	: Configuration
 *
 **/

package dz.mdn.iaas.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import dz.mdn.iaas.configuration.jwt.JwtAuthenticationEntryPoint;
import dz.mdn.iaas.configuration.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

/**
 * SecurityConfig
 * 
 * Modern Spring Security 6+ configuration.
 * Uses Spring Boot auto-configuration for authentication provider.
 * 
 * @author RAAS Security Team
 * @version 3.0 (Spring Boot 3.x / Spring Security 6+ compatible)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Password encoder bean
     * BCrypt is the recommended password hashing algorithm
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication manager bean
     * Required for manual authentication in AuthenticationService
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) 
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Security filter chain configuration
     * 
     * Modern approach using lambda DSL (Spring Security 6+)
     * No need for manual DaoAuthenticationProvider configuration
     * Spring Boot auto-configures it using UserDetailsService bean
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (not needed for stateless JWT authentication)
            .csrf(AbstractHttpConfigurer::disable)

            // Configure CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Configure exception handling
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )

            // Stateless session management (JWT-based)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/raas/api/auth/**").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()

                // Protected endpoints
                /*.requestMatchers("/api/users/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/roles/**").hasRole("ADMIN")
                .requestMatchers("/api/groups/**").hasRole("ADMIN")
                .requestMatchers("/api/permissions/**").hasRole("ADMIN")
                .requestMatchers("/api/authorities/**").hasRole("ADMIN")*/

                // All other requests require authentication
                .anyRequest().authenticated()
            );

        // Add JWT filter before Spring Security's authentication filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS configuration
     * Define allowed origins, methods, and headers
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed origins (configure based on your frontend URLs)
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",  // React default
            "http://localhost:4200",  // Angular default
            "http://localhost:8081"   // Vue default
        ));

        // Allowed HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // Allowed headers
        configuration.setAllowedHeaders(List.of("*"));

        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);

        // Max age for preflight requests (1 hour)
        configuration.setMaxAge(3600L);

        // Expose headers to client
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
