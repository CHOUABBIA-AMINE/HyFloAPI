/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdQueryService
 *  @CreatedOn  : Phase 5 — Commit 35
 *
 *  @Type       : Interface
 *  @Layer      : Service (Query)
 *  @Package    : Flow / Core
 *
 *  @Description: Query contract for FlowThreshold read operations.
 *                Returns only FlowThresholdDTO — never raw entities.
 *                No write operations permitted in implementations.
 *                Covers paginated listing, pipeline-scoped queries,
 *                structure-scoped queries, code search, and threshold
 *                coverage statistics (pipelines without thresholds).
 *
 *  Phase 5 — Commit 35
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import dz.sh.trc.hyflo.flow.core.dto.FlowThresholdDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Query contract for FlowThreshold read operations.
 *
 * Returns only FlowThresholdDTO — never raw entities.
 * No write operations permitted in implementations.
 *
 * Phase 5 — Commit 35
 */
public interface FlowThresholdQueryService {

    /**
     * Get a threshold by its unique identifier.
     *
     * @throws dz.sh.trc.hyflo.exception.ResourceNotFoundException if not found
     */
    FlowThresholdDTO getThresholdById(Long id);

    /**
     * Get all thresholds with pagination and optional sort field and direction.
     *
     * @param page          page number (0-indexed)
     * @param size          page size
     * @param sortBy        field name to sort by (default: id)
     * @param sortDirection ASC or DESC
     */
    Page<FlowThresholdDTO> getAllThresholds(int page, int size, String sortBy, String sortDirection);

    /**
     * Get all active thresholds with pagination.
     */
    Page<FlowThresholdDTO> getActiveThresholds(int page, int size);

    /**
     * Get all threshold versions (active and historical) for a pipeline.
     */
    List<FlowThresholdDTO> getThresholdsByPipelineId(Long pipelineId);

    /**
     * Get the currently active threshold for a pipeline, if any.
     * Returns empty Optional if no active threshold is configured.
     */
    Optional<FlowThresholdDTO> getActiveThresholdByPipelineId(Long pipelineId);

    /**
     * Get all active thresholds for pipelines managed by a structure
     * (station, terminal, or manifold).
     */
    List<FlowThresholdDTO> getActiveThresholdsByStructureId(Long structureId);

    /**
     * Search thresholds by exact pipeline code.
     */
    List<FlowThresholdDTO> searchByPipelineCode(String pipelineCode);

    /**
     * Search thresholds by pipeline code LIKE pattern (e.g., "OZ%").
     */
    List<FlowThresholdDTO> searchByPipelineCodePattern(String pattern);

    /**
     * Get all active thresholds without pagination.
     * Used for configuration exports and monitoring snapshots.
     */
    List<FlowThresholdDTO> getAllActiveThresholds();

    /**
     * Get all inactive (historical) thresholds without pagination.
     * Used for audit and configuration history review.
     */
    List<FlowThresholdDTO> getAllInactiveThresholds();

    /**
     * Get pipeline IDs that have no threshold configured (active or inactive).
     * Used for threshold coverage gap analysis.
     */
    List<Long> getPipelinesWithoutThresholds();

    /**
     * Get pipeline IDs that have no ACTIVE threshold configured.
     * Pipelines in this list may have historical thresholds but no current operating envelope.
     */
    List<Long> getPipelinesWithoutActiveThresholds();

    /**
     * Count pipelines with no threshold configured at all.
     */
    long countPipelinesWithoutThresholds();

    /**
     * Count total currently active thresholds across all pipelines.
     */
    long countActiveThresholds();

    /**
     * Check whether a pipeline has an active threshold.
     *
     * @return true if an active threshold exists for the given pipeline
     */
    boolean hasActiveThreshold(Long pipelineId);
}
