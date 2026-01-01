/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AttributeBasedAuthorizationManager
 *	@CreatedOn	: 11-18-2025
 *	@UpdatedOn	: 12-06-2025 (Fixed for Spring Security 6.x - Spring Boot 4.0)
 *
 *	@Type		: Class
 *	@Layer		: abac
 *	@Package	: Configuration
 *
 **/

package dz.sh.trc.hyflo.configuration.abac;

import dz.sh.trc.hyflo.system.security.model.User;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * AttributeBasedAuthorizationManager
 * 
 * Spring Security 6.x compatible implementation using AuthorizationManager
 * Replaces deprecated AccessDecisionVoter approach.
 * 
 * FIXED for Spring Boot 4.0:
 * - Method name changed from check() to authorize()
 * - Return type changed from AuthorizationDecision to AuthorizationDecision (same but different signature)
 * - Follows Spring Security 6.x AuthorizationManager contract
 * 
 * @author IAAS Security Team
 * @version 3.0 (Spring Security 6.x compatible - Spring Boot 4.0)
 */
@Component
public class AttributeBasedAuthorizationManager 
        implements AuthorizationManager<RequestAuthorizationContext> {

    private final AttributeEvaluator attributeEvaluator;

    public AttributeBasedAuthorizationManager(AttributeEvaluator attributeEvaluator) {
        this.attributeEvaluator = attributeEvaluator;
    }

    /**
     * Make an authorization decision based on attributes.
     * 
     * CRITICAL: Method name MUST be "authorize" not "check" in Spring Security 6.x
     * 
     * @param authentication Supplier providing the Authentication
     * @param context The RequestAuthorizationContext containing HTTP request info
     * @return AuthorizationDecision - granted (true) or denied (false)
     */
    @Override
    public AuthorizationDecision authorize(
            Supplier<? extends Authentication> authentication,
            RequestAuthorizationContext context) {

        Authentication auth = authentication.get();

        if (auth == null || !auth.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        // Check if principal is our User entity
        if (!(auth.getPrincipal() instanceof User)) {
            return new AuthorizationDecision(false);
        }

        User user = (User) auth.getPrincipal();

        // Extract resource attributes from the request
        Map<String, Object> resourceAttributes = extractResourceAttributes(context);

        // Extract ABAC condition from method annotation
        String condition = extractConditionFromRequest(context);

        if (condition != null && !condition.isEmpty()) {
            boolean granted = attributeEvaluator.evaluate(user, resourceAttributes, condition);
            return new AuthorizationDecision(granted);
        }

        // If no ABAC condition, allow other security mechanisms to decide
        return new AuthorizationDecision(true);
    }

    /**
     * Extract resource attributes from the HTTP request
     * This can include path variables, request parameters, headers, etc.
     */
    private Map<String, Object> extractResourceAttributes(RequestAuthorizationContext context) {
        Map<String, Object> attributes = new HashMap<>();

        // Extract path variables
        var request = context.getRequest();

        // Add request URI
        attributes.put("requestUri", request.getRequestURI());
        attributes.put("method", request.getMethod());

        // You can add more attribute extraction logic here based on your needs
        // For example: extract from request parameters, headers, etc.

        return attributes;
    }

    /**
     * Extract ABAC condition from the target method's @RequireAttribute annotation
     * This requires integration with method-level security
     */
    private String extractConditionFromRequest(RequestAuthorizationContext context) {
        // This is a placeholder - you would need to inspect the target controller method
        // One approach: Use HandlerMethod from RequestMappingHandlerMapping
        // For simplicity, returning null here (can be enhanced based on needs)
        return null;
    }
}