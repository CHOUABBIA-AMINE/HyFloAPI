/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : WebSocketSecurityConfig
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Configuration
 *  @Package    : Configuration
 *
 **/

package dz.sh.trc.hyflo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

/**
 * WebSocket Security Configuration for Spring Security 6+
 * 
 * Replaces deprecated AbstractSecurityWebSocketMessageBrokerConfigurer
 * Uses modern @EnableWebSocketSecurity annotation
 * 
 * Security Rules:
 * - CONNECT: Requires authentication
 * - SUBSCRIBE to /user/queue/**: Requires authentication (user-specific)
 * - SUBSCRIBE to /topic/**: Public broadcasts (if needed)
 * - Application messages (/app/**): Requires authentication
 * - Everything else: Deny
 */
@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

    /**
     * Configure WebSocket message authorization
     * 
     * This bean replaces the old configureInbound() method
     */
    @Bean
    AuthorizationManager<Message<?>> messageAuthorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        
        messages
                // Allow connection (authentication checked by interceptor)
                .nullDestMatcher().authenticated()
                
                // User-specific queues require authentication
                .simpDestMatchers("/user/queue/**").authenticated()
                
                // Public topic subscriptions (for broadcasts)
                .simpDestMatchers("/topic/**").permitAll()
                
                // Application messages (from client to server)
                .simpDestMatchers("/app/**").authenticated()
                
                // Subscribe patterns require authentication
                .simpSubscribeDestMatchers("/user/**").authenticated()
                
                // Deny everything else
                .anyMessage().denyAll();
        
        return messages.build();
    }
}
