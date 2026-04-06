/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: RequireAttribute
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 10-27-2025
 *
 *	@Type		: Interface
 *	@Layer		: Annotation
 *	@Package	: Configuration / Annotation
 *
 **/

package dz.sh.trc.hyflo.configuration.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireAttribute {
    String[] attributes() default {};
    String condition() default "";
}
