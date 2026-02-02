/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuditConfig
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 10-27-2025
 *
 *	@Type		: Class
 *	@Layer		: Configuration
 *	@Package	: Configuration
 *
 **/

package dz.sh.trc.hyflo.configuration;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Configuration for audit functionality
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AuditConfig {

    /**
     * Provides the current auditor (user) for JPA auditing
     */
    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    /**
     * Implementation of AuditorAware to get current user
     */
    public static class AuditorAwareImpl implements AuditorAware<String> {
    	@Override
        public Optional<String> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of("anonymous");
            }
            
            // If authentication is anonymous
            if (authentication.getPrincipal() instanceof String) {
                String principal = (String) authentication.getPrincipal();
                if ("anonymousUser".equals(principal)) {
                    return Optional.of("anonymous");
                }
                return Optional.of(principal);
            }
            
            // If using UserDetails
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                return Optional.of(userDetails.getUsername());
            }
            
            // Fallback
            return Optional.of(authentication.getName());
        }
    }
}
