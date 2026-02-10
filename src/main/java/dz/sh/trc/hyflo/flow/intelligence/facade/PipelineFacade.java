/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineFacade
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-10-2026 - Created during Phase 2 refactoring
 * 	@UpdatedOn	: 02-10-2026 - Phase 2: Return DTOs instead of entities
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
 * 	@Refactoring: Phase 2 (Enhancement) - Changed return types from entities
 * 	              to DTOs to fully decouple intelligence layer from entity structure.
 * 	              
 * 	              Benefits:
 * 	              - Prevents lazy loading exceptions (all data pre-loaded)
 * 	              - Removes entity dependency from intelligence services
 * 	              - Clear contract via DTO interface
 * 	              - Enables future caching at DTO level
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Facade providing intelligence module with controlled access to pipeline data.
 * 
 * This facade serves three purposes:
 * 1. Keeps repository access logic centralized (single point of access)
 * 2. Enforces module boundaries (intelligence → facade → network/core repositories)
 * 3. Converts entities to DTOs to decouple intelligence layer from entity structure
 * 
 * All methods are read-only (@Transactional(readOnly = true)) since
 * intelligence module only queries data, never modifies it.
 * 
 * Phase 2 Enhancement:
 * - All methods now return DTOs instead of entities
 * - Entity-to-DTO conversion happens within facade
 * - Intelligence services work exclusively with DTOs
 * 
 * Note on Entity Import:
 * - This facade imports Pipeline entity (network.core.model.Pipeline)
 * - This is acceptable: facade is the BOUNDARY layer between entity and DTO worlds
 * - Entity never leaves facade scope (immediately converted to DTO)
 * - Intelligence services never see or import entities
 * 
 * Future enhancements:
 * - Add caching layer (@Cacheable) for frequently accessed pipelines
 * - Add projection support for optimized queries (select only needed fields)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PipelineFacade {

    private final PipelineRepository pipelineRepository;

    // ========== BASIC QUERY METHODS ==========

    /**
     * Find pipeline by ID with all details as DTO.
     * 
     * Used by: PipelineIntelligenceService.getOverview()
     * Purpose: Get pipeline specifications for asset DTO construction
     * 
     * REFACTORED (Phase 2): Now returns PipelineDTO instead of Pipeline entity.
     * All entity-to-DTO conversion happens within facade, not in service layer.
     * 
     * Benefits:
     * - Intelligence service no longer depends on Pipeline entity
     * - Prevents lazy loading issues (DTO has all data pre-loaded)
     * - Clear contract: facade provides data transfer objects
     * 
     * Future Enhancement: Add @Cacheable("pipelines")
     * Rationale: Pipeline specs rarely change, safe to cache for hours
     * 
     * @param pipelineId Pipeline ID
     * @return PipelineDTO with full details or empty if not found
     */
    public Optional<PipelineDTO> findById(Long pipelineId) {
        log.debug("Finding pipeline by ID: {}", pipelineId);
        return pipelineRepository.findById(pipelineId)
                .map(PipelineDTO::fromEntity);
    }

    /**
     * Check if pipeline exists.
     * 
     * Used by: PipelineIntelligenceService.getSlotCoverage(), getReadingsTimeSeries()
     * Purpose: Validate pipeline existence before processing requests
     * 
     * Note: This method still returns boolean (no DTO conversion needed)
     * 
     * @param pipelineId Pipeline ID
     * @return true if pipeline exists, false otherwise
     */
    public boolean existsById(Long pipelineId) {
        log.debug("Checking if pipeline exists: {}", pipelineId);
        return pipelineRepository.existsById(pipelineId);
    }

    /**
     * Find all pipelines by manager/structure ID as DTOs.
     * 
     * Used by: Future monitoring aggregations
     * Purpose: Get all pipelines managed by a structure for bulk operations
     * 
     * REFACTORED (Phase 2): Now returns List<PipelineDTO> instead of List<Pipeline>.
     * 
     * @param managerId Structure (organization unit) ID
     * @return List of pipeline DTOs managed by the structure
     */
    public List<PipelineDTO> findByManagerId(Long managerId) {
        log.debug("Finding pipelines by manager ID: {}", managerId);
        return pipelineRepository.findByManagerId(managerId)
                .stream()
                .map(PipelineDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ========== COMPLETED ENHANCEMENTS ==========

    /**
     * ✅ COMPLETED: Facade now returns DTOs instead of entities
     * 
     * Previously planned enhancement is now implemented.
     * All methods convert entities to DTOs before returning.
     * 
     * Architecture Note:
     * - This facade imports Pipeline entity (necessary for conversion)
     * - Entity is used ONLY within facade scope
     * - All public methods return DTOs
     * - Intelligence services remain entity-free
     */

    /**
     * TODO: Add caching layer
     * 
     * Future improvement: Add Spring Cache annotations for performance
     * 
     * Example:
     * @Cacheable(value = "pipelines", key = "#pipelineId")
     * public Optional<PipelineDTO> findById(Long pipelineId) { ... }
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
     * - DTO caching is safer than entity caching (no lazy loading issues)
     */
}
