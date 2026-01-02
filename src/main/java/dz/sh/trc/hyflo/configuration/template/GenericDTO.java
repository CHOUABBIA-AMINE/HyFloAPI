/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: GenericDTO
 *	@CreatedOn	: 12-10-2025
 *	@UpdatedOn	: 12-10-2025
 *
 *	@Type		: Abstract Class
 *	@Layer		: DTO Base
 *	@Package	: Configuration / Template
 *
 **/

package dz.sh.trc.hyflo.configuration.template;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Generic DTO Base Class
 * Provides common structure and contract for all DTOs
 * 
 * IMPORTANT: Use @SuperBuilder instead of @Builder in subclasses!
 * 
 * Usage Example:
 * <pre>
 * {@code
 * @Data
 * @EqualsAndHashCode(callSuper = true)
 * @SuperBuilder  // <-- Use SuperBuilder, not Builder!
 * @NoArgsConstructor
 * @AllArgsConstructor
 * @JsonInclude(JsonInclude.Include.NON_NULL)
 * public class CurrencyDTO extends GenericDTO<Currency> {
 *     
 *     @NotBlank
 *     private String code;
 *     
 *     @NotBlank
 *     private String designation;
 *     
 *     @Override
 *     public Currency toEntity() {
 *         Currency entity = new Currency();
 *         entity.setId(getId());
 *         entity.setCode(this.code);
 *         entity.setDesignation(this.designation);
 *         return entity;
 *     }
 *     
 *     @Override
 *     public void updateEntity(Currency entity) {
 *         if (this.code != null) {
 *             entity.setCode(this.code);
 *         }
 *         if (this.designation != null) {
 *             entity.setDesignation(this.designation);
 *         }
 *     }
 *     
 *     public static CurrencyDTO fromEntity(Currency entity) {
 *         if (entity == null) return null;
 *         
 *         return CurrencyDTO.builder()
 *                 .id(entity.getId())
 *                 .code(entity.getCode())
 *                 .designation(entity.getDesignation())
 *                 .build();
 *     }
 * }
 * }
 * </pre>
 * 
 * @param <E> Entity type
 */
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class GenericDTO<E> {

    /**
     * Entity ID
     * Common to all entities
     */
    private Long id;

    // ========== ABSTRACT METHODS ==========

    /**
     * Convert DTO to entity (for create operations)
     * Must be implemented by subclass
     * 
     * @return New entity instance
     */
    public abstract E toEntity();

    /**
     * Update entity from DTO (for update operations)
     * Should modify the existing entity with values from DTO
     * Must be implemented by subclass
     * 
     * @param entity Existing entity to update
     */
    public abstract void updateEntity(E entity);

    // ========== UTILITY METHODS ==========

    /**
     * Check if DTO represents a new entity (no ID)
     * 
     * @return true if ID is null (new entity)
     */
    public boolean isNew() {
        return this.id == null;
    }

    /**
     * Check if DTO represents an existing entity (has ID)
     * 
     * @return true if ID is not null (existing entity)
     */
    public boolean isExisting() {
        return this.id != null;
    }

    /**
     * Get entity type name for logging/debugging
     * Can be overridden by subclass for custom name
     * 
     * @return Entity type name
     */
    public String getEntityTypeName() {
        return this.getClass().getSimpleName().replace("DTO", "");
    }
}
