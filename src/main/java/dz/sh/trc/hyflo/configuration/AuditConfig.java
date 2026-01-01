/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AuditConfig
 *	@CreatedOn	: 10-27-2025
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
            // TODO: Implement based on your security setup
            // Example with Spring Security:
            // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // if (authentication == null || !authentication.isAuthenticated()) {
            //     return Optional.of("system");
            // }
            // return Optional.of(authentication.getName());
            
            return Optional.of("system"); // Placeholder
        }
    }
}
