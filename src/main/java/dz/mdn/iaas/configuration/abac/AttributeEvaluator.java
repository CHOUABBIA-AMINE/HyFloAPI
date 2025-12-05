/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AttributeEvaluator
 *	@CreatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: abac
 *	@Package	: Configuration
 *
 **/

package dz.mdn.iaas.configuration.abac;

import dz.mdn.iaas.system.security.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * AttributeEvaluator
 * 
 * Evaluates attribute-based access control policies.
 * Supports evaluation of user attributes, resource attributes, and conditions.
 * 
 * @author RAAS Security Team
 * @version 2.0
 */
@Component
@Slf4j
public class AttributeEvaluator {

    /**
     * Evaluates attribute-based access control policies.
     * 
     * @param user The authenticated user
     * @param resourceAttributes Attributes of the resource being accessed
     * @param condition The ABAC condition to evaluate
     * @return true if access is granted, false otherwise
     */
    public boolean evaluate(User user, 
                           Map<String, Object> resourceAttributes,
                           String condition) {
        if (condition == null || condition.isEmpty()) {
            return true;
        }

        log.debug("Evaluating ABAC condition for user: {}", user.getUsername());
        log.debug("Condition: {}", condition);
        log.debug("Resource attributes: {}", resourceAttributes);

        try {
            return evaluateCondition(user, resourceAttributes, condition);
        } catch (Exception e) {
            log.error("Error evaluating ABAC condition", e);
            return false;
        }
    }

    /**
     * Evaluate the condition expression
     * This is a simplified implementation - for production, consider using:
     * - Spring Expression Language (SpEL)
     * - A dedicated policy engine like SAPL or Open Policy Agent
     * - Custom DSL parser
     */
    private boolean evaluateCondition(User user,
                                     Map<String, Object> resourceAttributes,
                                     String condition) {

        // Example condition patterns:
        // "user.department == resource.department"
        // "user.clearanceLevel >= 3"
        // "user.role == 'ADMIN' && resource.status == 'PUBLIC'"

        // For demonstration purposes, implement simple equality checks
        // In production, use a proper expression evaluator

        if (condition.contains("==")) {
            return evaluateEquality(user, resourceAttributes, condition);
        } else if (condition.contains(">=")) {
            return evaluateGreaterOrEqual(user, resourceAttributes, condition);
        } else if (condition.contains("&&")) {
            return evaluateAnd(user, resourceAttributes, condition);
        }

        // Default: deny access if condition format is not recognized
        log.warn("Unrecognized condition format: {}", condition);
        return false;
    }

    private boolean evaluateEquality(User user, Map<String, Object> resourceAttributes, String condition) {
        // Parse "user.attribute == resource.attribute" or "user.attribute == 'value'"
        String[] parts = condition.split("==");
        if (parts.length != 2) return false;

        String left = parts[0].trim();
        String right = parts[1].trim();

        Object leftValue = resolveValue(user, resourceAttributes, left);
        Object rightValue = resolveValue(user, resourceAttributes, right);

        return leftValue != null && leftValue.equals(rightValue);
    }

    private boolean evaluateGreaterOrEqual(User user, Map<String, Object> resourceAttributes, String condition) {
        // Parse "user.attribute >= value"
        String[] parts = condition.split(">=");
        if (parts.length != 2) return false;

        String left = parts[0].trim();
        String right = parts[1].trim();

        Object leftValue = resolveValue(user, resourceAttributes, left);
        Object rightValue = resolveValue(user, resourceAttributes, right);

        if (leftValue instanceof Number && rightValue instanceof Number) {
            return ((Number) leftValue).doubleValue() >= ((Number) rightValue).doubleValue();
        }

        return false;
    }

    private boolean evaluateAnd(User user, Map<String, Object> resourceAttributes, String condition) {
        // Parse "condition1 && condition2"
        String[] parts = condition.split("&&");
        if (parts.length != 2) return false;

        boolean result1 = evaluateCondition(user, resourceAttributes, parts[0].trim());
        boolean result2 = evaluateCondition(user, resourceAttributes, parts[1].trim());

        return result1 && result2;
    }

    /**
     * Resolve a value from the condition expression
     * Supports: user.attribute, resource.attribute, 'literal', number
     */
    private Object resolveValue(User user, Map<String, Object> resourceAttributes, String expression) {
        // Remove quotes if it's a string literal
        if (expression.startsWith("'") && expression.endsWith("'")) {
            return expression.substring(1, expression.length() - 1);
        }

        // Try to parse as number
        try {
            return Double.parseDouble(expression);
        } catch (NumberFormatException ignored) {
            // Not a number, continue
        }

        // Check if it's a user attribute
        if (expression.startsWith("user.")) {
            String attribute = expression.substring(5);
            return getUserAttribute(user, attribute);
        }

        // Check if it's a resource attribute
        if (expression.startsWith("resource.")) {
            String attribute = expression.substring(9);
            return resourceAttributes.get(attribute);
        }

        return null;
    }

    /**
     * Get user attribute value
     * Extend this method based on your User entity attributes
     */
    private Object getUserAttribute(User user, String attribute) {
        return switch (attribute) {
            case "username" -> user.getUsername();
            case "email" -> user.getEmail();
            case "enabled" -> user.isEnabled();
            // Add more attributes as needed
            // case "department" -> user.getDepartment();
            // case "clearanceLevel" -> user.getClearanceLevel();
            default -> null;
        };
    }
}
