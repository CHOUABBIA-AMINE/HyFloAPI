/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowReadingService
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-01-2026 - Integrated generic notification system
 * 	@UpdatedOn	: 01-27-2026 - Added validate and reject methods
 * 	@UpdatedOn	: 02-07-2026 - Added 6 operational monitoring methods
 * 	@UpdatedOn	: 02-07-2026 - Refactored to use proper DTOs instead of Map<String, Object>
 * 	@UpdatedOn	: 02-10-2026 - Extracted monitoring methods to FlowMonitoringService
 * 	@UpdatedOn	: 02-10-2026 - Refactored to use FlowReadingIdentifierBuilder utility
 * 	@UpdatedOn	: 02-10-2026 - Extracted workflow methods to FlowReadingWorkflowService (SRP)
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Core
 *
 * 	@Description: Service for flow reading CRUD operations.
 * 	              Now focused exclusively on data management (no workflow logic).
 *
 * 	@Refactoring: Workflow methods extracted to FlowReadingWorkflowService
 * 	              
 * 	              Single Responsibility Principle (SRP) applied:
 * 	              - FlowReadingService: CRUD operations (create, read, update, delete, queries)
 * 	              - FlowReadingWorkflowService: Workflow transitions (validate, reject)
 * 	              
 * 	              Benefits:
 * 	              - Clear separation of concerns
 * 	              - Easier testing (mock workflow dependencies separately)
 * 	              - Better maintainability (CRUD logic isolated)
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for flow reading CRUD operations.
 * 
 * This service handles:
 * - Create, Read, Update, Delete operations
 * - Query operations (by pipeline, slot, date range, status)
 * - Paginated queries
 * - Business validation (duplicate detection)
 * - Event publishing for creation (ReadingSubmittedEvent)
 * 
 * Workflow operations (validate, reject) are in FlowReadingWorkflowService.
 * Monitoring/analytics operations are in intelligence module.
 * 
 * Follows Single Responsibility Principle:
 * - This service: Data management (CRUD + queries)
 * - FlowReadingWorkflowService: State transitions (validate, reject)
 * - FlowMonitoringService: Analytics (pending, overdue, statistics)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowReadingService extends GenericService<FlowReading, FlowReadingDTO, Long> {

    private final FlowReadingRepository flowReadingRepository;

    //private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected JpaRepository<FlowReading, Long> getRepository() {
        return flowReadingRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowReading";
    }

    @Override
    protected FlowReadingDTO toDTO(FlowReading entity) {
        return FlowReadingDTO.fromEntity(entity);
    }

    @Override
    protected FlowReading toEntity(FlowReadingDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowReading entity, FlowReadingDTO dto) {
        dto.updateEntity(entity);
    }

    /**
     * Lifecycle hook: Called after a reading is created
     * 
     * Publishes ReadingSubmittedEvent to notify all validators.
     * Uses the generic notification system.
     */
    /*@Override
    protected void afterCreate(FlowReading reading) {
        String readingIdentifier = buildReadingIdentifier(reading);
        String recordedBy = reading.getRecordedBy() != null 
            ? reading.getRecordedBy().getLastNameLt() + " " + reading.getRecordedBy().getFirstNameLt()
            : "Unknown";
        
        // Use factory method to create properly configured event
        ReadingSubmittedEvent event = ReadingSubmittedEvent.create(
                reading.getId(),
                recordedBy,
                readingIdentifier,
                LocalDateTime.now().format(DATETIME_FORMATTER)
        );
        
        // Publish event - GenericNotificationEventListener will handle it
        //publishEvent(event);
        log.debug("Published ReadingSubmittedEvent for reading ID: {}", reading.getId());
    }*/

    // ========== CRUD OPERATIONS ==========

    @Override
    @Transactional
    public FlowReadingDTO create(FlowReadingDTO dto) {
        log.info("Creating flow reading: pipelineId={}, recordedAt={}", 
                 dto.getPipelineId(), dto.getRecordedAt());
        
        // Business validation: Prevent duplicate readings
        if (flowReadingRepository.existsByPipelineIdAndRecordedAt(
                dto.getPipelineId(), dto.getRecordedAt())) {
            throw new BusinessValidationException(
                "Flow reading for this pipeline and timestamp already exists");
        }
        
        // Parent class will call afterCreate() hook automatically
        return super.create(dto);
    }

    // ========== QUERY OPERATIONS ==========

    /**
     * Get all flow readings without pagination
     * 
     * @return List of all flow reading DTOs
     */
    public List<FlowReadingDTO> getAll() {
        log.debug("Getting all flow readings without pagination");
        return flowReadingRepository.findAll().stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find readings by pipeline
     * 
     * @param pipelineId Pipeline ID
     * @return List of reading DTOs for the pipeline
     */
    public List<FlowReadingDTO> findByPipeline(Long pipelineId) {
        log.debug("Finding flow readings by pipeline id: {}", pipelineId);
        return flowReadingRepository.findByPipelineId(pipelineId).stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find readings by reading slot
     * 
     * @param readingSlotId Slot ID
     * @return List of reading DTOs for the slot
     */
    public List<FlowReadingDTO> findByReadingSlot(Long readingSlotId) {
        log.debug("Finding flow readings by slot id: {}", readingSlotId);
        return flowReadingRepository.findByReadingSlotId(readingSlotId).stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find readings by time range
     * 
     * @param startTime Start timestamp
     * @param endTime End timestamp
     * @return List of reading DTOs in time range
     */
    public List<FlowReadingDTO> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding flow readings by time range: {} to {}", startTime, endTime);
        return flowReadingRepository.findByRecordedAtBetween(startTime, endTime).stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find readings by pipeline and reading date range
     * 
     * @param pipelineId Pipeline ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of reading DTOs
     */
    public List<FlowReadingDTO> findByPipelineAndReadingDateRange(
            Long pipelineId, LocalDate startDate, LocalDate endDate) {
        log.debug("Finding flow readings by pipeline {} and date range: {} to {}", 
                  pipelineId, startDate, endDate);
        return flowReadingRepository.findByPipelineIdAndReadingDateBetween(pipelineId, startDate, endDate).stream()
                					.map(FlowReadingDTO::fromEntity)
                					.collect(Collectors.toList());
    }

    /**
     * Find readings by pipeline, slot, and date range
     * 
     * @param pipelineId Pipeline ID
     * @param readingSlotId Slot ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of reading DTOs
     */
    public List<FlowReadingDTO> findByPipelineAndReadingSlotAndReadingDateRange(
            Long pipelineId, Long readingSlotId, LocalDate startDate, LocalDate endDate) {
        log.debug("Finding flow readings by pipeline {} and slot {} and date range: {} to {}", 
                  pipelineId, readingSlotId, startDate, endDate);
        return flowReadingRepository.findByPipelineIdAndReadingSlotIdAndReadingDateBetween(pipelineId, readingSlotId, startDate, endDate).stream()
                					.map(FlowReadingDTO::fromEntity)
                					.collect(Collectors.toList());
    }

    /**
     * Find readings by pipeline and time range
     * 
     * @param pipelineId Pipeline ID
     * @param startTime Start timestamp
     * @param endTime End timestamp
     * @return List of reading DTOs
     */
    public List<FlowReadingDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding flow readings by pipeline {} and time range: {} to {}", 
                  pipelineId, startTime, endTime);
        return flowReadingRepository.findByPipelineIdAndRecordedAtBetween(
                pipelineId, startTime, endTime).stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find readings by validation status
     * 
     * @param validationStatusId Status ID
     * @return List of reading DTOs with the status
     */
    public List<FlowReadingDTO> findByValidationStatus(Long validationStatusId) {
        log.debug("Finding flow readings by validation status id: {}", validationStatusId);
        return flowReadingRepository.findByValidationStatusId(validationStatusId).stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ========== PAGINATED QUERY OPERATIONS ==========

    /**
     * Find readings by pipeline and time range (paginated)
     * 
     * @param pipelineId Pipeline ID
     * @param startTime Start timestamp
     * @param endTime End timestamp
     * @param pageable Pagination parameters
     * @return Page of reading DTOs
     */
    public Page<FlowReadingDTO> findByPipelineAndTimeRangePaginated(
            Long pipelineId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        log.debug("Finding flow readings (paginated) by pipeline {} and time range: {} to {}", 
                  pipelineId, startTime, endTime);
        return executeQuery(p -> flowReadingRepository.findByPipelineAndTimeRange(
                pipelineId, startTime, endTime, p), pageable);
    }

    /**
     * Find readings by pipeline and reading date range (paginated)
     * 
     * @param pipelineId Pipeline ID
     * @param startDate Start date
     * @param endDate End date
     * @param pageable Pagination parameters
     * @return Page of reading DTOs
     */
    public Page<FlowReadingDTO> findByPipelineAndReadingDateRangePaginated(
            Long pipelineId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Finding flow readings (paginated) by pipeline {} and date range: {} to {}", 
                  pipelineId, startDate, endDate);
        return executeQuery(p -> flowReadingRepository.findByPipelineAndReadingDateRange(
                pipelineId, startDate, endDate, p), pageable);
    }

    /**
     * Find readings by pipeline, slot, and date range (paginated)
     * 
     * @param pipelineId Pipeline ID
     * @param readingSlotId Slot ID
     * @param startDate Start date
     * @param endDate End date
     * @param pageable Pagination parameters
     * @return Page of reading DTOs
     */
    public Page<FlowReadingDTO> findByPipelineAndReadingSlotAndReadingDateRangePaginated(
            Long pipelineId, Long readingSlotId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Finding flow readings (paginated) by pipeline {} and and reading slot {} and date range: {} to {}", 
                  pipelineId, readingSlotId, startDate, endDate);
        return executeQuery(p -> flowReadingRepository.findByPipelineAndReadingSlotAndReadingDateRange(
                pipelineId, readingSlotId, startDate, endDate, p), pageable);
    }

    /**
     * Find readings by pipeline and validation status (paginated)
     * 
     * @param pipelineId Pipeline ID
     * @param statusId Status ID
     * @param pageable Pagination parameters
     * @return Page of reading DTOs
     */
    public Page<FlowReadingDTO> findByPipelineAndValidationStatus(
            Long pipelineId, Long statusId, Pageable pageable) {
        log.debug("Finding flow readings by pipeline {} and validation status {}", 
                  pipelineId, statusId);
        return executeQuery(p -> flowReadingRepository.findByPipelineAndValidationStatus(
                pipelineId, statusId, p), pageable);
    }

    /**
     * Find readings by validation status (paginated)
     * 
     * @param statusId Status ID
     * @param pageable Pagination parameters
     * @return Page of reading DTOs
     */
    public Page<FlowReadingDTO> findByValidationStatus(Long statusId, Pageable pageable) {
        log.debug("Finding flow readings by validation status {}", statusId);
        return executeQuery(p -> flowReadingRepository.findByValidationStatus(statusId, p), pageable);
    }

    /**
     * Find latest readings by pipeline (paginated)
     * 
     * @param pipelineId Pipeline ID
     * @param pageable Pagination parameters
     * @return Page of latest reading DTOs
     */
    public Page<FlowReadingDTO> findLatestByPipeline(Long pipelineId, Pageable pageable) {
        log.debug("Finding latest flow readings for pipeline: {}", pipelineId);
        return executeQuery(p -> flowReadingRepository.findLatestByPipeline(pipelineId, p), pageable);
    }

    // ========== HELPER METHODS ==========

    /**
     * Build a human-readable identifier for a reading
     * 
     * Uses FlowReadingIdentifierBuilder utility for standardized format.
     * Falls back to legacy format if utility fails.
     * 
     * @param reading FlowReading entity
     * @return Formatted identifier string (e.g., "PL-001-20260210-S01")
     */
    /*private String buildReadingIdentifier(FlowReading reading) {
        if (reading == null) {
            return "Unknown Reading";
        }
        
        try {
            // Use utility for standardized identifier format
            return FlowReadingIdentifierBuilder.buildIdentifier(
                reading.getPipeline(),
                reading.getReadingDate(),
                reading.getReadingSlot()
            );
        } catch (Exception e) {
            // Fallback to legacy format if utility fails
            log.warn("Failed to build identifier using utility, falling back to legacy format", e);
            
            StringBuilder identifier = new StringBuilder();
            
            if (reading.getPipeline() != null && reading.getPipeline().getName() != null) {
                identifier.append(reading.getPipeline().getName());
            } else if (reading.getPipeline() != null) {
                identifier.append("Pipeline #").append(reading.getPipeline().getId());
            } else {
                identifier.append("Reading");
            }
            
            if (reading.getRecordedAt() != null) {
                identifier.append(" at ")
                         .append(reading.getRecordedAt().format(DATETIME_FORMATTER));
            }
            
            if (reading.getReadingSlot() != null) {
                if (reading.getReadingSlot().getDesignationEn() != null) {
                    identifier.append(" (").append(reading.getReadingSlot().getDesignationEn()).append(")");
                } else if (reading.getReadingSlot().getDesignationFr() != null) {
                    identifier.append(" (").append(reading.getReadingSlot().getDesignationFr()).append(")");
                }
            }
            
            return identifier.toString();
        }
    }*/

    // ========== MIGRATION NOTES ==========

    /**
     * ⚠️ WORKFLOW METHODS MOVED
     * 
     * The following methods were moved to FlowReadingWorkflowService:
     * - validate(Long id, Long validatedById)
     * - reject(Long id, Long rejectedById, String rejectionReason)
     * 
     * Use FlowReadingWorkflowService for workflow operations.
     * This service is now focused exclusively on CRUD operations.
     * 
     * Example migration:
     * OLD: flowReadingService.validate(readingId, validatorId)
     * NEW: flowReadingWorkflowService.validate(readingId, validatorId)
     */
}
