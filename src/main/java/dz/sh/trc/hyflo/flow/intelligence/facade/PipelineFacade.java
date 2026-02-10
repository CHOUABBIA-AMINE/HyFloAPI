/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineFacade
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-10-2026 - Created during Phase 2 refactoring
 *
 * 	@Type		: Class
 * 	@Layer		: Facade
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: Facade providing intelligence module with controlled access
 * 	              to pipeline data from network/core module.
 *
 * 	@Pattern: Facade Pattern - Wraps PipelineRepository to provide simplified
 * 	          interface and enforce module boundaries.
 *
 * 	@Refactoring: Phase 2 - Created to eliminate direct PipelineRepository
 * 	              access from PipelineIntelligenceService.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Facade providing intelligence module with controlled access to pipeline data.
 * 
 * This facade serves two purposes:
 * 1. Keeps repository access logic centralized (single point of access)
 * 2. Enforces module boundaries (intelligence → facade → network/core repositories)
 * 
 * All methods are read-only (@Transactional(readOnly = true)) since
 * intelligence module only queries data, never modifies it.
 * 
 * Future enhancements:
 * - Add caching layer (@Cacheable) for frequently accessed pipelines
 * - Return DTOs instead of entities to fully decouple from network module
 * - Add projection support for optimized queries
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PipelineFacade {

    private final PipelineRepository pipelineRepository;

    // ========== BASIC QUERY METHODS ==========

    /**
     * Find pipeline by ID with all details
     * 
     * Used by: PipelineIntelligenceService.getOverview()
     * Purpose: Get pipeline specifications for asset DTO construction
     * 
     * Future Enhancement: Add @Cacheable("pipelines")
     * Rationale: Pipeline specs rarely change, safe to cache for hours
     * 
     * @param pipelineId Pipeline ID
     * @return Pipeline entity with full details or empty if not found
     */
    public Optional<Pipeline> findById(Long pipelineId) {
        log.debug("Finding pipeline by ID: {}", pipelineId);
        return pipelineRepository.findById(pipelineId);
    }

    /**
     * Check if pipeline exists
     * 
     * Used by: PipelineIntelligenceService.getSlotCoverage(), getReadingsTimeSeries()
     * Purpose: Validate pipeline existence before processing requests
     * 
     * @param pipelineId Pipeline ID
     * @return true if pipeline exists, false otherwise
     */
    public boolean existsById(Long pipelineId) {
        log.debug("Checking if pipeline exists: {}", pipelineId);
        return pipelineRepository.existsById(pipelineId);
    }

    /**
     * Find all pipelines by manager/structure ID
     * 
     * Used by: Future monitoring aggregations
     * Purpose: Get all pipelines managed by a structure for bulk operations
     * 
     * @param managerId Structure (organization unit) ID
     * @return List of pipelines managed by the structure
     */
    public List<Pipeline> findByManagerId(Long managerId) {
        log.debug("Finding pipelines by manager ID: {}", managerId);
        return pipelineRepository.findByManagerId(managerId);
    }

    // ========== FUTURE ENHANCEMENTS ==========

    /**
     * TODO: Add DTO conversion methods
     * 
     * Future improvement: Have facade return DTOs instead of entities
     * to fully decouple intelligence services from network entity structure.
     * 
     * Example:
     * public Optional<PipelineDTO> findByIdAsDTO(Long pipelineId) {
     *     return findById(pipelineId).map(PipelineDTO::fromEntity);
     * }
     * 
     * Benefits:
     * - Prevents lazy loading issues (all data pre-loaded in DTO)
     * - Removes entity dependency from intelligence layer
     * - Clear contract via DTO interface
     * - Can optimize with projections (only select needed fields)
     */

    /**
     * TODO: Add caching layer
     * 
     * Future improvement: Add Spring Cache annotations for performance
     * 
     * Example:
     * @Cacheable(value = "pipelines", key = "#pipelineId")
     * public Optional<Pipeline> findById(Long pipelineId) { ... }
     * 
     * Configuration:
     * - Cache expiration: 24 hours (pipeline specs rarely change)
     * - Cache size: 1000 entries max
     * - Eviction: LRU (Least Recently Used)
     * 
     * Benefits:
     * - Reduces database load for frequently accessed pipelines
     * - Improves response time for overview requests
     * - Especially valuable for dashboard queries
     */
}
