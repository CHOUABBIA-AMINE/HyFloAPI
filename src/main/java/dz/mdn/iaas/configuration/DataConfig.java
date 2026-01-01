/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DataConfig
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-21-2025
 *
 *	@Type		: Class
 *	@Layer		: Configuration
 *	@Package	: Configuration
 *
 **/

package dz.mdn.iaas.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Data Layer Configuration
 * 
 * Enables JPA repositories and transaction management for the application.
 * All DataSource, EntityManagerFactory, and Hibernate settings are handled by
 * Spring Boot auto-configuration through application.properties.
 * 
 * == Configuration Properties (application.properties) ==
 * 
 * Database Connection:
 *   - spring.datasource.url
 *   - spring.datasource.username
 *   - spring.datasource.password
 *   - spring.datasource.driver-class-name
 * 
 * HikariCP Connection Pool:
 *   - spring.datasource.hikari.minimum-idle
 *   - spring.datasource.hikari.maximum-pool-size
 *   - spring.datasource.hikari.connection-timeout
 *   - spring.datasource.hikari.idle-timeout
 *   - spring.datasource.hikari.max-lifetime
 *   - spring.datasource.hikari.leak-detection-threshold
 *   - spring.datasource.hikari.auto-commit
 *   - spring.datasource.hikari.data-source-properties.*
 * 
 * JPA/Hibernate:
 *   - spring.jpa.database-platform
 *   - spring.jpa.hibernate.ddl-auto
 *   - spring.jpa.show-sql
 *   - spring.jpa.properties.hibernate.*
 * 
 * == Auto-Configured Beans ==
 * 
 * Spring Boot automatically creates and configures:
 *   - DataSource (HikariCP by default)
 *   - EntityManagerFactory (Hibernate JPA provider)
 *   - PlatformTransactionManager (JpaTransactionManager)
 * 
 * No manual bean definitions needed!
 */
@Configuration
@EnableJpaRepositories(basePackages = "dz.mdn.iaas")
@EnableTransactionManagement
public class DataConfig {
    
    // All configuration is handled by Spring Boot auto-configuration
    // See application.properties for database and JPA settings
    
}
