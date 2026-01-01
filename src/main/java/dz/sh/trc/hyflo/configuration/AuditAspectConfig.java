/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AuditAspect
 *	@CreatedOn	: 10-27-2025
 *
 *	@Type		: Class
 *	@Layer		: Aspect
 *	@Package	: Configuration
 *
 **/

package dz.sh.trc.hyflo.configuration;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import dz.sh.trc.hyflo.configuration.annotation.Auditable;
import dz.sh.trc.hyflo.system.audit.model.Audited.AuditStatus;
import dz.sh.trc.hyflo.system.audit.service.AuditedService;
import dz.sh.trc.hyflo.system.audit.service.AuditedService.AuditEventBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Aspect for automatic audit logging
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspectConfig {

    private final AuditedService auditedService;

    @Around("@annotation(auditable)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        // Get method information
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        
        // Get request information
        HttpServletRequest request = getCurrentRequest();
        String ipAddress = getClientIpAddress(request);
        String userAgent = request != null ? request.getHeader("User-Agent") : null;
        String sessionId = request != null ? request.getSession().getId() : null;
        
        // Get current user (you'll need to implement this based on your security setup)
        String username = getCurrentUsername();
        
        // Create audit event builder
        AuditEventBuilder eventBuilder = AuditEventBuilder.create()
                .entityName(auditable.entityName())
                .action(auditable.action())
                .username(username)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .methodName(methodName)
                .module(auditable.module())
                .businessProcess(auditable.businessProcess())
                .sessionId(sessionId)
                .parameters(Arrays.asList(joinPoint.getArgs()));

        Object result = null;
        try {
            // Execute the method
            result = joinPoint.proceed();
            
            // Calculate duration
            long duration = System.currentTimeMillis() - startTime;
            
            // Extract entity ID from result if possible
            Long entityId = extractEntityId(result, auditable);
            
            // Log successful operation
            eventBuilder
                    .entityId(entityId)
                    .newValues(result)
                    .status(AuditStatus.SUCCESS)
                    .duration(duration)
                    .description(generateDescription(auditable, entityId, AuditStatus.SUCCESS));
            
            auditedService.logAuditEvent(eventBuilder);
            
            return result;
            
        } catch (Exception e) {
            // Calculate duration
            long duration = System.currentTimeMillis() - startTime;
            
            // Log failed operation
            eventBuilder
                    .status(AuditStatus.FAILED)
                    .errorMessage(e.getMessage())
                    .duration(duration)
                    .description(generateDescription(auditable, null, AuditStatus.FAILED));
            
            auditedService.logAuditEvent(eventBuilder);
            
            throw e;
        }
    }

    /**
     * Get current HTTP request
     */
    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attributes.getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extract client IP address
     */
    private String getClientIpAddress(HttpServletRequest request) {
        if (request == null) return null;
        
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    /**
     * Get current authenticated username
     * Implement this based on your security setup (Spring Security, JWT, etc.)
     */
    private String getCurrentUsername() {
        // TODO: Implement based on your authentication mechanism
        // Example with Spring Security:
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // return authentication != null ? authentication.getName() : "anonymous";
        return "system"; // Placeholder
    }

    /**
     * Extract entity ID from method result
     */
    private Long extractEntityId(Object result, Auditable auditable) {
        if (result == null) return null;
        
        try {
            // If result is a DTO with getId() method
            Method getIdMethod = result.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(result);
            return id instanceof Long ? (Long) id : null;
        } catch (Exception e) {
            // If extraction fails, try to get from audit annotation
            return null;
        }
    }

    /**
     * Generate human-readable description
     */
    private String generateDescription(Auditable auditable, Long entityId, AuditStatus status) {
        StringBuilder description = new StringBuilder();
        
        switch (auditable.action()) {
            case CREATE:
                description.append("Created new ").append(auditable.entityName().toLowerCase());
                break;
            case UPDATE:
                description.append("Updated ").append(auditable.entityName().toLowerCase());
                break;
            case DELETE:
                description.append("Deleted ").append(auditable.entityName().toLowerCase());
                break;
            case READ:
                description.append("Retrieved ").append(auditable.entityName().toLowerCase());
                break;
            default:
                description.append(auditable.action().name().toLowerCase())
                          .append(" operation on ").append(auditable.entityName().toLowerCase());
        }
        
        if (entityId != null) {
            description.append(" with ID ").append(entityId);
        }
        
        if (status == AuditStatus.FAILED) {
            description.append(" - Operation failed");
        }
        
        return description.toString();
    }
}
