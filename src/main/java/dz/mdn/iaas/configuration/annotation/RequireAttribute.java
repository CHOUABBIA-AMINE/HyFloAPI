/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RequireAttribute
 *	@CreatedOn	: 10-27-2025
 *
 *	@Type		: Annotation
 *	@Layer		: Annotation
 *	@Package	: Configuration / Annotation
 *
 **/

package dz.mdn.iaas.configuration.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireAttribute {
    String[] attributes() default {};
    String condition() default "";
}
