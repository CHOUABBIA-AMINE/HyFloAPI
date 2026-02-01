/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : WebSocketEventListener
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : WebSocket
 *  @Package    : Configuration / Notification
 *
 **/

package dz.sh.trc.hyflo.configuration.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;

/**
 * WebSocket event listener for monitoring connections
 * 
 * Logs:
 * - User connections
 * - User disconnections
 * - Subscription events
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    /**
     * Handle WebSocket connection events
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        String username = getUsernameFromEvent(headerAccessor);
        String sessionId = headerAccessor.getSessionId();
        
        log.info("WebSocket connected - User: {}, SessionId: {}", username, sessionId);
    }

    /**
     * Handle WebSocket disconnection events
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        String username = getUsernameFromEvent(headerAccessor);
        String sessionId = headerAccessor.getSessionId();
        
        log.info("WebSocket disconnected - User: {}, SessionId: {}", username, sessionId);
    }

    /**
     * Handle subscription events
     */
    @EventListener
    public void handleSubscriptionEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        String username = getUsernameFromEvent(headerAccessor);
        String destination = headerAccessor.getDestination();
        String sessionId = headerAccessor.getSessionId();
        
        log.debug("WebSocket subscription - User: {}, Destination: {}, SessionId: {}", 
                username, destination, sessionId);
    }

    /**
     * Extract username from STOMP header accessor
     */
    private String getUsernameFromEvent(StompHeaderAccessor headerAccessor) {
        Principal user = headerAccessor.getUser();
        return (user != null) ? user.getName() : "Anonymous";
    }
}
