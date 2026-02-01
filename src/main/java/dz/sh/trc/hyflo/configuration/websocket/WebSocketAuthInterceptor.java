/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : WebSocketAuthInterceptor
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : WebSocket
 *  @Package    : Configuration / Notification
 *
 **/

package dz.sh.trc.hyflo.configuration.websocket;

import dz.sh.trc.hyflo.configuration.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * WebSocket authentication interceptor
 * 
 * Intercepts WebSocket CONNECT messages and validates JWT token
 * from the STOMP headers.
 * 
 * Client must send JWT token in STOMP header:
 * Authorization: Bearer <token>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;  // CHANGED: From JwtService to JwtUtil
    private final UserDetailsService userDetailsService;

    /**
     * Intercept messages before sending to channel
     * Validates JWT token on CONNECT command
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                message, 
                StompHeaderAccessor.class
        );

        // Only process CONNECT commands
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authToken = accessor.getFirstNativeHeader("Authorization");
            
            if (authToken != null && authToken.startsWith("Bearer ")) {
                String jwt = authToken.substring(7);
                
                try {
                    // Extract username from token using JwtUtil
                    String username = jwtUtil.extractUsername(jwt);
                    
                    if (username != null) {
                        // Load user details
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        
                        // Validate token using JwtUtil
                        if (jwtUtil.validateToken(jwt, userDetails)) {
                            // Create authentication
                            UsernamePasswordAuthenticationToken authentication = 
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                            null,
                                            userDetails.getAuthorities()
                                    );
                            
                            // Set authentication in security context
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            
                            // Set user principal in STOMP session
                            accessor.setUser(authentication);
                            
                            log.debug("WebSocket authentication successful for user: {}", username);
                        } else {
                            log.warn("Invalid JWT token for WebSocket connection");
                        }
                    }
                } catch (Exception e) {
                    log.error("Error authenticating WebSocket connection", e);
                }
            } else {
                log.warn("WebSocket CONNECT without Authorization header");
            }
        }

        return message;
    }
}
