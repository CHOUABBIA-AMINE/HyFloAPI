/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: MethodSecurityConfig
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Configuration
 *	@Package	: Configuration
 *
 **/

package dz.sh.trc.hyflo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * MethodSecurityConfig
 * 
 * Configuration for method-level security with custom expressions.
 * Enables @PreAuthorize, @PostAuthorize, @Secured annotations.
 * 
 * @Author IAAS Security Team
 * @version 1.0
 */
@Configuration
@EnableMethodSecurity(
    prePostEnabled = true,    // Enable @PreAuthorize and @PostAuthorize
    securedEnabled = true,    // Enable @Secured
    jsr250Enabled = true      // Enable @RolesAllowed
)
public class MethodSecurityConfig {

    /**
     * Configure method security expression handler
     * You can customize this to add your own security expressions
     */
    @Bean
    MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        // You can set custom permission evaluator here if needed
        return handler;
    }
}
