/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : WebSocketConfig
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Configuration
 *  @Package    : Configuration
 *
 **/

package dz.sh.trc.hyflo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import dz.sh.trc.hyflo.configuration.websocket.WebSocketAuthInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * WebSocket configuration for real-time notifications
 */
@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Value("${notification.websocket.allowed-origins:http://localhost:3000,http://localhost:4200}")
    private String[] allowedOrigins;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
        
        log.info("Message broker configured: /topic, /queue, /app, /user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(allowedOrigins)
                .withSockJS();
        
        log.info("STOMP endpoint registered: /ws with allowed origins: {}", 
                String.join(", ", allowedOrigins));
    }

    /**
     * Configure client inbound channel with authentication interceptor
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketAuthInterceptor);
        log.info("WebSocket authentication interceptor registered");
    }
}
