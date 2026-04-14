/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: GenericDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-10-2025
 *
 *	@Type		: Abstract Class
 *	@Layer		: Template
 *	@Package	: Configuration / Template
 *
 **/

package dz.sh.trc.hyflo.platform.kernel;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Base DTO for all HyFlo modules (Flow, Pipeline, AI, Validation, etc.)
 *
 * Responsibility:
 * - PURE DATA CARRIER ONLY
 * - NO business logic
 * - NO mapping logic
 *
 * Must be used only at API boundaries.
 */
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class GenericDTO {

    /**
     * Unique identifier of the resource
     */
    private Long id;


    /**
     * Check if DTO represents a new resource (not persisted yet)
     */
    public boolean isNew() {
        return id == null;
    }

    /**
     * Check if DTO represents an existing persisted resource
     */
    public boolean isExisting() {
        return id != null;
    }

    /**
     * Get simple type name for logging/debugging
     */
    public String getDtoType() {
        return this.getClass().getSimpleName();
    }
}
