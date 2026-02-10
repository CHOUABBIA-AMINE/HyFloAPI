/**
 *
 * 	@Author		: CHOUABBIA Amine (original)
 * 	@UpdatedBy	: CHOUABBIA Amine
 *
 * 	@Name		: FlowReadingFacade
 * 	@CreatedOn	: 02-07-2026
 * 	@UpdatedOn	: 02-10-2026 - Added monitoring methods (Phase 1 refactoring)
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
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ReadingSlotRepository;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Facade providing intelligence module with controlled access to flow readings
 * and reading slots, wrapping FlowReadingRepository and ReadingSlotRepository.
 *
 * This facade serves two purposes:
 * 1. Keeps repository access logic centralized (single point of access)
 * 2. Enforces module boundaries (intelligence → facade → core repositories)
 *
 * All methods are read-only (@Transactional(readOnly = true)) since
 * intelligence module only queries data, never modifies it.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FlowReadingFacade {

    private final FlowReadingRepository flowReadingRepository;
    private final ReadingSlotRepository readingSlotRepository;
    private final ValidationStatusRepository validationStatusRepository;

    // ========== BASIC QUERY METHODS (Original) ==========

    /**
     * Find latest reading for a specific pipeline.
     * 
     * Used by: PipelineIntelligenceService.getOverview()
     * Purpose: Get current measurements for dashboard display
     * 
     * @param pipelineId Pipeline ID
     * @return Latest reading or empty if no readings exist
     */
    public Optional<FlowReading> findLatestByPipeline(Long pipelineId) {
        log.debug("Finding latest flow reading for pipeline: {}", pipelineId);
        return flowReadingRepository.findTopByPipelineIdOrderByRecordedAtDesc(pipelineId);
    }

    /**
     * Find all readings for a specific pipeline and date.
     * 
     * Used by: PipelineIntelligenceService.getOverview(), getSlotCoverage()
     * Purpose: Calculate slot coverage for a single day
     * 
     * @param pipelineId Pipeline ID
     * @param readingDate Date to query
     * @return List of readings for that date (max 12, one per slot)
     */
    public List<FlowReading> findByPipelineAndDate(Long pipelineId, LocalDate readingDate) {
        log.debug("Finding flow readings for pipeline {} on date {}", pipelineId, readingDate);
        return flowReadingRepository.findByPipelineIdAndReadingDate(pipelineId, readingDate);
    }

    /**
     * Find readings within date range ordered chronologically for time-series analysis.
     * 
     * Used by: PipelineIntelligenceService.getReadingsTimeSeries()
     * Purpose: Generate time-series charts (pressure, temperature, flow rate trends)
     * 
     * @param pipelineId Pipeline ID
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return Chronologically ordered readings
     */
    public List<FlowReading> findByPipelineAndDateRangeOrdered(
            Long pipelineId, LocalDate startDate, LocalDate endDate) {
        log.debug("Finding ordered flow readings for pipeline {} between {} and {}", 
                  pipelineId, startDate, endDate);
        return flowReadingRepository
                .findByPipelineIdAndReadingDateBetweenOrderByReadingDateAscRecordedAtAsc(
                        pipelineId, startDate, endDate);
    }

    /**
     * Get all reading slots ordered by display order.
     * 
     * Used by: Multiple intelligence services
     * Purpose: Iterate through all 12 slots for coverage calculations
     * 
     * @return All 12 reading slots (S01-S12) ordered by displayOrder
     */
    public List<ReadingSlot> findAllSlotsOrdered() {
        log.debug("Finding all reading slots ordered by display order");
        return readingSlotRepository.findAllByOrderByDisplayOrder();
    }

    // ========== MONITORING QUERY METHODS (Phase 1 Addition) ==========

    /**
     * Find readings pending validation by structure
     * 
     * Returns paginated list of readings in SUBMITTED status awaiting validation.
     * Used for "Pending Validations" dashboard view.
     * 
     * Added in Phase 1 refactoring to eliminate direct repository access
     * from FlowMonitoringService.
     * 
     * Used by: FlowMonitoringService.findPendingValidationsByStructure()
     * 
     * @param structureId Structure (organization unit) ID
     * @param pageable Pagination parameters
     * @return Paginated readings awaiting validation
     */
    public Page<FlowReading> findPendingValidationsByStructure(
            Long structureId, Pageable pageable) {
        log.debug("Finding pending validations for structure: {}", structureId);
        
        // Fetch SUBMITTED status entity
        ValidationStatus submittedStatus = validationStatusRepository.findByCode("SUBMITTED")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SUBMITTED validation status not found in database. " +
                        "Please ensure reference data is properly initialized."));
        
        // Delegate to repository
        return flowReadingRepository.findByStructureAndValidationStatus(
                structureId, submittedStatus.getId(), pageable);
    }

    /**
     * Find overdue readings by structure
     * 
     * Returns paginated list of readings past their slot deadline and not yet validated.
     * A reading is overdue if: current_time > (reading_date + slot_end_time)
     * 
     * Added in Phase 1 refactoring to centralize monitoring queries in facade.
     * 
     * Used by: FlowMonitoringService.findOverdueReadingsByStructure()
     * 
     * @param structureId Structure ID
     * @param asOfDate Date to check for overdue readings (usually today)
     * @param currentDateTime Current timestamp for deadline comparison
     * @param pageable Pagination parameters
     * @return Paginated overdue readings
     */
    public Page<FlowReading> findOverdueReadingsByStructure(
            Long structureId, 
            LocalDate asOfDate, 
            LocalDateTime currentDateTime, 
            Pageable pageable) {
        log.debug("Finding overdue readings for structure: {} as of date: {} at time: {}", 
                  structureId, asOfDate, currentDateTime);
        
        // Delegate to repository
        // Repository uses JPQL FUNCTION('TIMESTAMP', ...) to combine date + time
        return flowReadingRepository.findOverdueReadingsByStructure(
                structureId, asOfDate, currentDateTime, pageable);
    }

    // ========== FUTURE ENHANCEMENTS ==========

    /**
     * TODO: Add DTO conversion methods
     * 
     * Future improvement: Have facade return DTOs instead of entities
     * to fully decouple intelligence services from core entity structure.
     * 
     * Example:
     * public Page<FlowReadingDTO> findPendingValidationsDTOs(...) {
     *     return findPendingValidationsByStructure(...).map(FlowReadingDTO::fromEntity);
     * }
     * 
     * Benefits:
     * - Prevents lazy loading issues (all data pre-loaded in DTO)
     * - Removes entity dependency from intelligence layer
     * - Clear contract via DTO interface
     */
}
