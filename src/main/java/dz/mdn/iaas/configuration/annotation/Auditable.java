/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Auditable
 *	@CreatedOn	: 10-27-2025
 *
 *	@Type		: Annotation
 *	@Layer		: Annotation
 *	@Package	: Configuration / Annotation
 *
 **/

package dz.mdn.iaas.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dz.mdn.iaas.system.audit.model.Audited.AuditAction;

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
    AuditAction action();
    
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
