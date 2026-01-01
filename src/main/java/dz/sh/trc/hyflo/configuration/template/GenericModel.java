/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: GenericModel
 *	@CreatedOn	: 12-10-2025
 *
 *	@Type		: Abstract Class
 *	@Layer		: Configuration / Template
 *	@Package	: Configuration / Template
 *
 *	@Description: Base abstract class for all JPA entities.
 *	              Provides common ID field with standard configuration.
 *	              All entity models should extend this class to inherit
 *	              the primary key definition and maintain consistency.
 *
 **/

package dz.sh.trc.hyflo.configuration.template;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Generic Model - Base class for all JPA entities
 * 
 * Provides:
 * - Primary key field (id) mapped to F_00 in database
 * - Auto-generation strategy using database identity column
 * - Serialization support
 * 
 * Usage:
 * <pre>
 * {@code
 * @Entity
 * @Table(name="T_02_01_01")
 * public class Currency extends GenericModel {
 *     // Entity-specific fields only
 *     // ID is inherited from GenericModel
 * }
 * }
 * </pre>
 * 
 * Benefits:
 * - Eliminates code duplication across entities
 * - Ensures consistent ID configuration
 * - Single point of maintenance for primary key strategy
 * - Follows DRY (Don't Repeat Yourself) principle
 * 
 * @author CHOUABBIA Amine
 * @since 1.0
 */
@Getter
@Setter
@MappedSuperclass
public abstract class GenericModel {

    /**
     * Primary key identifier
     * 
     * Database column: F_00 (standard for all entities)
     * Generation strategy: IDENTITY (auto-increment in database)
     * Type: Long (supports large number of records)
     * 
     * This field is inherited by all entity classes that extend GenericModel.
     * Do not override or redeclare this field in subclasses.
     */
    @Id
    @Column(name = "F_00")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
