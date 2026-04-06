/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuditConfig
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 10-27-2025
 *
 *	@Type		: Class
 *	@Layer		: Configuration
 *	@Package	: Configuration
 *
 **/

package dz.sh.trc.hyflo.configuration.audit;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration for audit functionality
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AuditConfig {

}
