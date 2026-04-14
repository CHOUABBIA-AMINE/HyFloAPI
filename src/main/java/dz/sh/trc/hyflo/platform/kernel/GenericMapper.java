/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: GenericMapper
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 04-14-2025
 *
 *	@Type		: Interface
 *	@Layer		: Template
 *	@Package	: Platform / kernel
 *
 **/

package dz.sh.trc.hyflo.platform.kernel;

import java.util.List;
import java.util.Set;

/**
 * Generic contract for entity ↔ DTO mapping.
 * Designed for HyFlo modular architecture (Flow, Pipeline, AI, etc.)
 *
 * E  = Entity
 * D  = DTO
 * ID = Identifier type
 */
public interface GenericMapper<E, D, ID> {

    /**
     * Convert Entity → DTO
     */
    D toDto(E entity);

    /**
     * Convert DTO → Entity
     */
    E toEntity(D dto);

    /**
     * Convert Entity List → DTO List
     */
    List<D> toDtoList(List<E> entities);

    /**
     * Convert DTO List → Entity List
     */
    List<E> toEntityList(List<D> dtos);

    /**
     * Convert Entity Set → DTO Set
     * Useful for relationships (FlowSteps, Pipelines, etc.)
     */
    Set<D> toDtoSet(Set<E> entities);

    /**
     * Convert DTO Set → Entity Set
     */
    Set<E> toEntitySet(Set<D> dtos);
}