/**
 *
 * 	@Author		: CHOUABBIA Amine (original)
 * 	@UpdatedBy	: CHOUABBIA Amine
 *
 * 	@Name		: FlowReadingFacade
 * 	@CreatedOn	: 02-07-2026
 * 	@UpdatedOn	: 02-10-2026 - Added monitoring methods (Phase 1 refactoring)
 * 	@UpdatedOn	: 02-10-2026 - Phase 2: Return DTOs instead of entities
 *
 * 	@Type		: Class
 * 	@Layer		: Facade
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: Facade providing intelligence module with controlled access
 * 	              to flow readings and reading slots.
 *
 * 	@Pattern: Facade Pattern - Wraps FlowReadingRepository and 
 * 	          ReadingSlotRepository to provide simplified interface
 * 	          and enforce module boundaries.
 *
 * 	@Refactoring: Phase 1 - Added monitoring query methods to eliminate
 * 	              direct FlowReadingRepository access from FlowMonitoringService.
 *
 * 	@Refactoring: Phase 2 - Changed all return types from entities to DTOs
 * 	              to fully decouple intelligence layer from entity structure.
 * 	              
 * 	              Benefits:
 * 	              - Prevents lazy loading exceptions (all data pre-loaded in DTO)
 * 	              - Removes entity dependency from intelligence services
 * 	              - Clear contract via DTO interface
 * 	              - Enables future caching at DTO level
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ReadingSlotRepository;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Facade providing intelligence module with controlled access to flow readings
 * and reading slots, wrapping FlowReadingRepository and ReadingSlotRepository.
 *
 * This facade serves three purposes:
 * 1. Keeps repository access logic centralized (single point of access)
 * 2. Enforces module boundaries (intelligence → facade → core repositories)
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
 * - This facade imports FlowReading entity (core.model.FlowReading)
 * - This is acceptable: facade is the BOUNDARY layer between entity and DTO worlds
 * - Entity never leaves facade scope (immediately converted to DTO)
 * - Intelligence services never see or import entities
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FlowReadingFacade {

    private final FlowReadingRepository flowReadingRepository;
    private final ReadingSlotRepository readingSlotRepository;
    private final ValidationStatusRepository validationStatusRepository;

    // ========== BASIC QUERY METHODS (Refactored to return DTOs) ==========

    /**
     * Find latest reading for a specific pipeline as DTO.
     * 
     * Used by: PipelineIntelligenceService.getOverview()
     * Purpose: Get current measurements for dashboard display
     * 
     * REFACTORED (Phase 2): Now returns Optional<FlowReadingDTO> instead of Optional<FlowReading>.
     * Entity-to-DTO conversion happens within facade.
     * 
     * @param pipelineId Pipeline ID
     * @return Latest reading DTO or empty if no readings exist
     */
    public Optional<FlowReadingDTO> findLatestByPipeline(Long pipelineId) {
        log.debug("Finding latest flow reading for pipeline: {}", pipelineId);
        return flowReadingRepository.findTopByPipelineIdOrderByRecordedAtDesc(pipelineId)
                .map(FlowReadingDTO::fromEntity);
    }

    /**
     * Find all readings for a specific pipeline and date as DTOs.
     * 
     * Used by: PipelineIntelligenceService.getOverview(), getSlotCoverage()
     * Purpose: Calculate slot coverage for a single day
     * 
     * REFACTORED (Phase 2): Now returns List<FlowReadingDTO> instead of List<FlowReading>.
     * 
     * @param pipelineId Pipeline ID
     * @param readingDate Date to query
     * @return List of reading DTOs for that date (max 12, one per slot)
     */
    public List<FlowReadingDTO> findByPipelineAndDate(Long pipelineId, LocalDate readingDate) {
        log.debug("Finding flow readings for pipeline {} on date {}", pipelineId, readingDate);
        return flowReadingRepository.findByPipelineIdAndReadingDate(pipelineId, readingDate)
                .stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find readings within date range ordered chronologically as DTOs.
     * 
     * Used by: PipelineIntelligenceService.getReadingsTimeSeries()
     * Purpose: Generate time-series charts (pressure, temperature, flow rate trends)
     * 
     * REFACTORED (Phase 2): Now returns List<FlowReadingDTO> instead of List<FlowReading>.
     * 
     * @param pipelineId Pipeline ID
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return Chronologically ordered reading DTOs
     */
    public List<FlowReadingDTO> findByPipelineAndDateRangeOrdered(
            Long pipelineId, LocalDate startDate, LocalDate endDate) {
        log.debug("Finding ordered flow readings for pipeline {} between {} and {}", 
                  pipelineId, startDate, endDate);
        return flowReadingRepository
                .findByPipelineIdAndReadingDateBetweenOrderByReadingDateAscRecordedAtAsc(
                        pipelineId, startDate, endDate)
                .stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all reading slots ordered by display order.
     * 
     * Used by: Multiple intelligence services
     * Purpose: Iterate through all 12 slots for coverage calculations
     * 
     * Note: ReadingSlot is a reference entity (like enum), so returning entities is acceptable.
     * These are not domain entities with lazy loading concerns.
     * 
     * @return All 12 reading slots (S01-S12) ordered by displayOrder
     */
    public List<ReadingSlot> findAllSlotsOrdered() {
        log.debug("Finding all reading slots ordered by display order");
        return readingSlotRepository.findAllByOrderByDisplayOrder();
    }

    // ========== MONITORING QUERY METHODS (Refactored to return DTOs) ==========

    /**
     * Find readings pending validation by structure as DTOs.
     * 
     * Returns paginated list of readings in SUBMITTED status awaiting validation.
     * Used for "Pending Validations" dashboard view.
     * 
     * Added in Phase 1 refactoring to eliminate direct repository access
     * from FlowMonitoringService.
     * 
     * REFACTORED (Phase 2): Now returns Page<FlowReadingDTO> instead of Page<FlowReading>.
     * 
     * Used by: FlowMonitoringService.findPendingValidationsByStructure()
     * 
     * @param structureId Structure (organization unit) ID
     * @param pageable Pagination parameters
     * @return Paginated reading DTOs awaiting validation
     */
    public Page<FlowReadingDTO> findPendingValidationsByStructure(
            Long structureId, Pageable pageable) {
        log.debug("Finding pending validations for structure: {}", structureId);
        
        // Fetch SUBMITTED status entity
        ValidationStatus submittedStatus = validationStatusRepository.findByCode("SUBMITTED")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SUBMITTED validation status not found in database. " +
                        "Please ensure reference data is properly initialized."));
        
        // Delegate to repository and convert to DTOs
        Page<FlowReading> entityPage = flowReadingRepository.findByStructureAndValidationStatus(
                structureId, submittedStatus.getId(), pageable);
        
        List<FlowReadingDTO> dtoList = entityPage.getContent()
                .stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }

    /**
     * Find overdue readings by structure as DTOs.
     * 
     * Returns paginated list of readings past their slot deadline and not yet validated.
     * A reading is overdue if: current_time > (reading_date + slot_end_time)
     * 
     * Added in Phase 1 refactoring to centralize monitoring queries in facade.
     * 
     * REFACTORED (Phase 2): Now returns Page<FlowReadingDTO> instead of Page<FlowReading>.
     * 
     * Used by: FlowMonitoringService.findOverdueReadingsByStructure()
     * 
     * @param structureId Structure ID
     * @param asOfDate Date to check for overdue readings (usually today)
     * @param currentDateTime Current timestamp for deadline comparison
     * @param pageable Pagination parameters
     * @return Paginated overdue reading DTOs
     */
    public Page<FlowReadingDTO> findOverdueReadingsByStructure(
            Long structureId, 
            LocalDate asOfDate, 
            LocalDateTime currentDateTime, 
            Pageable pageable) {
        log.debug("Finding overdue readings for structure: {} as of date: {} at time: {}", 
                  structureId, asOfDate, currentDateTime);
        
        // Delegate to repository and convert to DTOs
        // Repository uses JPQL FUNCTION('TIMESTAMP', ...) to combine date + time
        Page<FlowReading> entityPage = flowReadingRepository.findOverdueReadingsByStructure(
                structureId, asOfDate, currentDateTime, pageable);
        
        List<FlowReadingDTO> dtoList = entityPage.getContent()
                .stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }

    // ========== COMPLETED ENHANCEMENTS ==========

    /**
     * ✅ COMPLETED: Facade now returns DTOs instead of entities
     * 
     * Previously planned enhancement is now implemented.
     * All methods convert entities to DTOs before returning.
     * 
     * Benefits achieved:
     * - Intelligence services no longer depend on entity structure
     * - Prevents lazy loading issues (DTOs have all data pre-loaded)
     * - Clear contract via DTO interface
     * - Enables future caching at DTO level
     * 
     * Architecture Note:
     * - This facade imports FlowReading entity (necessary for conversion)
     * - Entity is used ONLY within facade scope
     * - All public methods return DTOs
     * - Intelligence services remain entity-free
     */
}
