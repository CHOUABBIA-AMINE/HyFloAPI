/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Auditable
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-06-2025
 *
 *	@Type		: Interface
 *	@Layer		: Annotation
 *	@Package	: Configuration / Annotation
 *
 **/

package dz.sh.trc.hyflo.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods for automatic audit logging
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    
    /**
     * Name of the entity being audited
     */
    String entityName();
    
    /**
     * Type of action being performed
     */
    String action();
    
    /**
     * Business module (e.g., "CONSULTATION", "CONTRACT")
     */
    String module() default "";
    
    /**
     * Business process name (e.g., "SUBMISSION_CREATION")
     */
    String businessProcess() default "";
    
    /**
     * Custom description template
     */
    String description() default "";
}
