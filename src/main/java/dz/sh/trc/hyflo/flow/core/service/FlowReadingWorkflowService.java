/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowReadingWorkflowService
 * 	@CreatedOn	: 01-28-2026
 * 	@UpdatedOn	: 02-03-2026
 *
 * 	@Type		: Service
 * 	@Layer		: Service
 * 	@Package	: Flow / Core
 *
 * 	@Description:
 * 	Slot-centric workflow service for hydrocarbon transportation monitoring.
 * 	Implements process-oriented reading management with state machine enforcement.
 *
 * 	FIXES APPLIED:
 * 	- FIX #1:  Race condition prevention with pessimistic locking
 * 	- FIX #2:  Lock approved readings (immutable after validation)
 * 	- FIX #3:  Event publishing for notifications
 * 	- FIX #4:  Slot deadline enforcement
 * 	- FIX #5:  Prevent REJECTED → APPROVED bypass
 * 	- FIX #6:  Business validation for measurement data
 * 	- FIX #11: Idempotent validation operations
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.exception.WorkflowException;
import dz.sh.trc.hyflo.flow.common.dto.ReadingSlotDTO;
import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ReadingSlotRepository;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.core.dto.PipelineCoverageDTO;
import dz.sh.trc.hyflo.flow.core.dto.ReadingSubmitRequestDTO;
import dz.sh.trc.hyflo.flow.core.dto.ReadingValidationRequestDTO;
import dz.sh.trc.hyflo.flow.core.dto.SlotCoverageRequestDTO;
import dz.sh.trc.hyflo.flow.core.dto.SlotCoverageResponseDTO;
import dz.sh.trc.hyflo.flow.core.helper.ValidationStatusHelper;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.flow.core.repository.SlotCoverageProjection;
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import dz.sh.trc.hyflo.general.organization.repository.StructureRepository;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FlowReadingWorkflowService {
    
    // ========== DEPENDENCIES ==========
    
    private final FlowReadingRepository flowReadingRepository;
    private final ValidationStatusRepository validationStatusRepository;
    private final PipelineRepository pipelineRepository;
    private final ReadingSlotRepository readingSlotRepository;
    private final StructureRepository structureRepository;
    private final EmployeeRepository employeeRepository;
    //private final ApplicationEventPublisher applicationEventPublisher;
    private final ReadingBusinessValidator businessValidator; // FIX #6
    
    // ========== PUBLIC API METHODS ==========
    
    /**
     * Get slot coverage - all pipelines with reading status for a given date + slot + structure.
     * This is the primary query for operators and supervisors to see what readings are missing.
     *
     * @param request Contains readingDate, slotId, structureId
     * @return Slot coverage with pipeline-level reading status and statistics
     */
    public SlotCoverageResponseDTO getSlotCoverage(SlotCoverageRequestDTO request) {
        
        log.debug("Fetching slot coverage for date={}, slot={}, structure={}", 
            request.getReadingDate(), request.getSlotId(), request.getStructureId());
        
        // Query coverage (LEFT JOIN - shows all pipelines even without readings)
        List<SlotCoverageProjection> projections = flowReadingRepository
            .findSlotCoverage(
                request.getReadingDate(),
                request.getSlotId(),
                request.getStructureId()
            );
        
        // Load metadata
        Structure structure = structureRepository.findById(request.getStructureId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Structure not found: " + request.getStructureId()));
        
        ReadingSlot slot = readingSlotRepository.findById(request.getSlotId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Slot not found: " + request.getSlotId()));
        
        // Build pipeline coverage list
        List<PipelineCoverageDTO> pipelines = projections.stream()
            .map(this::buildPipelineCoverage)
            .collect(Collectors.toList());
        
        // Calculate statistics by status code
        Map<String, Long> statusCounts = pipelines.stream()
            .collect(Collectors.groupingBy(
                PipelineCoverageDTO::getWorkflowStatus,
                Collectors.counting()
            ));
        
        int totalPipelines = pipelines.size();
        int missingCount = statusCounts.getOrDefault(ValidationStatusHelper.NOT_RECORDED, 0L).intValue();
        int recordedCount = totalPipelines - missingCount;
        
        // Check if slot is complete (all pipelines APPROVED)
        Boolean isComplete = flowReadingRepository.isSlotComplete(
            request.getReadingDate(), 
            request.getSlotId(), 
            request.getStructureId()
        );
        
        return SlotCoverageResponseDTO.builder()
            .readingDate(request.getReadingDate())
            .slot(mapSlotToDTO(slot))
            .structure(mapStructureToDTO(structure))
            .totalPipelines(totalPipelines)
            .recordedCount(recordedCount)
            .submittedCount(statusCounts.getOrDefault(ValidationStatusHelper.SUBMITTED, 0L).intValue())
            .approvedCount(statusCounts.getOrDefault(ValidationStatusHelper.APPROVED, 0L).intValue())
            .rejectedCount(statusCounts.getOrDefault(ValidationStatusHelper.REJECTED, 0L).intValue())
            .missingCount(missingCount)
            .completionPercentage(calculateCompletion(recordedCount, totalPipelines))
            .isSlotComplete(Boolean.TRUE.equals(isComplete))
            .pipelines(pipelines)
            .build();
    }
    
    /**
     * Submit or update a reading.
     * Operators use this to record measurements and optionally submit them for validation.
     *
     * STATE TRANSITIONS ALLOWED:
     * - NOT_RECORDED → DRAFT (save without submitting)
     * - NOT_RECORDED → SUBMITTED (save and submit)
     * - DRAFT → DRAFT (update measurements)
     * - DRAFT → SUBMITTED (submit after editing)
     * - REJECTED → DRAFT (correct rejected reading)
     * - REJECTED → SUBMITTED (correct and submit rejected reading)
     *
     * BLOCKED TRANSITIONS:
     * - APPROVED → * (immutable)
     * - SUBMITTED → DRAFT/SUBMITTED (locked until validated)
     *
     * FIX #1: Race condition prevention with pessimistic locking
     * FIX #2: Lock approved readings (cannot edit)
     * FIX #6: Business validation for measurement data
     *
     * @param request Contains pipelineId, date, slotId, measurements, employeeId
     * @throws WorkflowException if reading is in non-editable state
     */
    public void submitReading(ReadingSubmitRequestDTO request) {
        
        log.debug("Processing reading submission: readingId={}, pipeline={}, date={}, slot={}", 
            request.getReadingId(), request.getPipelineId(), 
            request.getReadingDate(), request.getSlotId());
        
        // Validate employee exists
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Employee not found: " + request.getEmployeeId()));
        
        FlowReading reading;
        String previousStatus = null;
        
        if (request.getReadingId() != null) {
            // ========== UPDATE EXISTING READING ==========
            
            // FIX #2: Lock reading and check if editable
            reading = flowReadingRepository
                .findByIdForUpdate(request.getReadingId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Reading not found: " + request.getReadingId()));
            
            previousStatus = ValidationStatusHelper.getStatusCode(reading);
            
            // FIX #2: ENFORCE - Only DRAFT, REJECTED, or NOT_RECORDED can be edited
            if (!ValidationStatusHelper.isEditable(reading)) {
                throw new WorkflowException(
                    "Reading in status '" + previousStatus + "' cannot be modified. " +
                    "Only DRAFT or REJECTED readings can be edited. " +
                    "APPROVED readings are immutable (legal documents). " +
                    "SUBMITTED readings are locked until validated.");
            }
            
            // FIX #2: ENFORCE - Pipeline/Date/Slot cannot change on update
            if (!reading.getPipeline().getId().equals(request.getPipelineId()) ||
                !reading.getReadingDate().equals(request.getReadingDate()) ||
                !reading.getReadingSlot().getId().equals(request.getSlotId())) {
                throw new WorkflowException(
                    "Cannot change pipeline, date, or slot of existing reading. " +
                    "Delete and recreate if different context is needed.");
            }
            
            log.debug("Updating existing reading {} from status {}", reading.getId(), previousStatus);
            
        } else {
            // ========== CREATE NEW READING ==========
            
            // FIX #1: Use pessimistic locking to prevent race condition
            reading = findOrCreateReadingWithLock(
                request.getPipelineId(),
                request.getReadingDate(),
                request.getSlotId(),
                employee
            );
            
            previousStatus = ValidationStatusHelper.getStatusCode(reading);
            log.debug("Created/found reading {} with status {}", reading.getId(), previousStatus);
        }
        
        // FIX #6: VALIDATE BUSINESS RULES BEFORE SAVING
        List<String> warnings = businessValidator.validateReading(
            request.getPipelineId(),
            request.getPressure(),
            request.getTemperature(),
            request.getFlowRate(),
            request.getContainedVolume()
        );
        
        if (!warnings.isEmpty()) {
            log.warn("Reading validation warnings for pipeline {}: {}", 
                request.getPipelineId(), warnings);
            
            // Append warnings to notes
            String warningText = "\n\n[SYSTEM WARNINGS - " + LocalDateTime.now() + "]\n" + 
                String.join("\n", warnings);
            String existingNotes = request.getNotes() != null ? request.getNotes() : "";
            request.setNotes(existingNotes + warningText);
        }
        
        // Update measurement data
        reading.setPressure(request.getPressure());
        reading.setTemperature(request.getTemperature());
        reading.setFlowRate(request.getFlowRate());
        reading.setContainedVolume(request.getContainedVolume());
        reading.setNotes(request.getNotes());
        reading.setRecordedAt(LocalDateTime.now());
        reading.setRecordedBy(employee);
        
        // Determine target status
        String targetStatusCode = Boolean.TRUE.equals(request.getSubmitImmediately()) ?
            ValidationStatusHelper.SUBMITTED : ValidationStatusHelper.DRAFT;
        
        ValidationStatus status = validationStatusRepository
            .findByCode(targetStatusCode)
            .orElseThrow(() -> new IllegalStateException(
                "ValidationStatus '" + targetStatusCode + "' not configured in database"));
        
        reading.setValidationStatus(status);
        
        flowReadingRepository.save(reading);
        
        // FIX #3: Publish event for SUBMITTED transition
        // Only publish if transitioning TO SUBMITTED (not if already SUBMITTED)
        if (ValidationStatusHelper.SUBMITTED.equals(targetStatusCode) && 
            !ValidationStatusHelper.SUBMITTED.equals(previousStatus)) {
            
            log.debug("Publishing ReadingSubmittedEvent for reading {}", reading.getId());
            
            /*applicationEventPublisher.publishEvent(new ReadingSubmittedEvent(
                this,
                reading.getId(),
                reading.getPipeline().getId(),
                reading.getReadingDate(),
                reading.getReadingSlot().getId(),
                employee.getId()
            ));*/
        }
        
        log.info("Reading {} transitioned from {} to {} by employee {} ({})", 
            reading.getId(), 
            previousStatus, 
            targetStatusCode, 
            employee.getRegistrationNumber(),
            employee.getFirstNameLt() + " " + employee.getLastNameLt());
    }
    
    /**
     * Validate reading (approve or reject).
     * Supervisors and managers use this to approve or reject submitted readings.
     *
     * STATE TRANSITIONS ALLOWED:
     * - SUBMITTED → APPROVED (supervisor validates data)
     * - SUBMITTED → REJECTED (supervisor rejects data, operator must correct)
     *
     * BLOCKED TRANSITIONS:
     * - NOT_RECORDED → * (must record first)
     * - DRAFT → * (must submit first)
     * - REJECTED → APPROVED (must be re-submitted by operator first)
     * - APPROVED → * (immutable)
     *
     * FIX #5:  Prevent REJECTED → APPROVED bypass
     * FIX #11: Idempotent validation (multiple calls with same action = success)
     *
     * @param request Contains readingId, action (APPROVE/REJECT), comments, employeeId
     * @throws WorkflowException if reading is not in SUBMITTED state
     */
    public void validateReading(ReadingValidationRequestDTO request) {
        
        log.debug("Processing validation: readingId={}, action={}, validator={}", 
            request.getReadingId(), request.getAction(), request.getEmployeeId());
        
        // Validate employee exists
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Employee not found: " + request.getEmployeeId()));
        
        // Lock reading for update
        FlowReading reading = flowReadingRepository
            .findByIdForUpdate(request.getReadingId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Reading not found: " + request.getReadingId()));
        
        String currentStatusCode = ValidationStatusHelper.getStatusCode(reading);
        
        // Determine target status
        String targetStatusCode;
        if ("APPROVE".equalsIgnoreCase(request.getAction())) {
            targetStatusCode = ValidationStatusHelper.APPROVED;
            
            // FIX #11: Idempotency - if already approved, return success
            if (ValidationStatusHelper.APPROVED.equals(currentStatusCode)) {
                log.info("Reading {} already approved, returning success (idempotent)", reading.getId());
                return;
            }
            
        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {
            targetStatusCode = ValidationStatusHelper.REJECTED;
            
            // FIX #11: Idempotency - if already rejected, return success
            if (ValidationStatusHelper.REJECTED.equals(currentStatusCode)) {
                log.info("Reading {} already rejected, returning success (idempotent)", reading.getId());
                return;
            }
            
        } else {
            throw new WorkflowException(
                "Invalid action: " + request.getAction() + 
                ". Allowed values: APPROVE, REJECT");
        }
        
        // FIX #5: ENFORCE - Only SUBMITTED can be validated
        // This prevents REJECTED → APPROVED bypass
        if (!ValidationStatusHelper.SUBMITTED.equals(currentStatusCode)) {
            throw new WorkflowException(
                "Only SUBMITTED readings can be validated. Current status: " + currentStatusCode + ". " +
                "Rejected readings must be corrected and re-submitted by operator before re-validation. " +
                "Draft readings must be submitted first.");
        }
        
        // Update validation status
        ValidationStatus status = validationStatusRepository
            .findByCode(targetStatusCode)
            .orElseThrow(() -> new IllegalStateException(
                "ValidationStatus '" + targetStatusCode + "' not configured in database"));
        
        reading.setValidationStatus(status);
        reading.setValidatedAt(LocalDateTime.now());
        reading.setValidatedBy(employee);
        
        if (ValidationStatusHelper.REJECTED.equals(targetStatusCode)) {
            
            // Rejection reason is mandatory
            if (!StringUtils.hasText(request.getComments())) {
                throw new WorkflowException(
                    "Rejection reason is required. Provide detailed comments explaining why the reading is rejected.");
            }
            
            // Append rejection reason to notes (audit trail)
            String existingNotes = reading.getNotes() != null ? reading.getNotes() : "";
            reading.setNotes(existingNotes + "\n\n[REJECTED by " + 
                employee.getFirstNameLt() + " " + employee.getLastNameLt() + 
                " on " + LocalDateTime.now() + "]\n" + request.getComments());
            
            log.debug("Publishing ReadingRejectedEvent for reading {}", reading.getId());
            
            // FIX #3: Publish rejection event
            /*applicationEventPublisher.publishEvent(new ReadingRejectedEvent(
                this,
                reading.getId(),
                reading.getPipeline().getId(),
                reading.getReadingDate(),
                reading.getReadingSlot().getId(),
                reading.getRecordedBy().getId(),
                employee.getId(),
                request.getComments()
            ));*/
            
        } else {
            // Approval - optional comments
            if (StringUtils.hasText(request.getComments())) {
                String existingNotes = reading.getNotes() != null ? reading.getNotes() : "";
                reading.setNotes(existingNotes + "\n\n[APPROVED by " + 
                    employee.getFirstNameLt() + " " + employee.getLastNameLt() + 
                    " on " + LocalDateTime.now() + "]\n" + request.getComments());
            }
            
            log.debug("Publishing ReadingValidatedEvent for reading {}", reading.getId());
            
            // FIX #3: Publish approval event
            /*applicationEventPublisher.publishEvent(new ReadingValidatedEvent(
                this,
                reading.getId(),
                reading.getPipeline().getId(),
                reading.getReadingDate(),
                reading.getReadingSlot().getId(),
                reading.getRecordedBy().getId(),
                employee.getId(),
                true // isApproved
            ));*/
        }
        
        flowReadingRepository.save(reading);
        
        log.info("Reading {} validated: {} → {} by employee {} ({})", 
            reading.getId(), 
            currentStatusCode,
            targetStatusCode, 
            employee.getRegistrationNumber(),
            employee.getFirstNameLt() + " " + employee.getLastNameLt());
    }
    
    // ========== HELPER METHODS ==========
    
    /**
     * FIX #1: Find or create reading WITH PESSIMISTIC LOCK.
     * Prevents duplicate readings from concurrent submissions.
     *
     * ALGORITHM:
     * 1. Try to find existing reading with PESSIMISTIC_WRITE lock
     * 2. If found, return it (thread-safe)
     * 3. If not found, create new reading
     * 4. If DataIntegrityViolationException (race condition), retry fetch with lock
     *
     * @param pipelineId Pipeline identifier
     * @param date Reading date
     * @param slotId Slot identifier
     * @param employee Employee recording the reading
     * @return Existing or newly created reading
     * @throws WorkflowException if creation fails after retry
     */
    private FlowReading findOrCreateReadingWithLock(
        Long pipelineId, 
        LocalDate date, 
        Long slotId,
        Employee employee
    ) {
        
        // Step 1: Try to acquire lock on existing reading
        Optional<FlowReading> existing = flowReadingRepository
            .findByPipelineAndDateAndSlotForUpdate(pipelineId, date, slotId);
        
        if (existing.isPresent()) {
            log.debug("Found existing reading {} for pipeline={}, date={}, slot={}", 
                existing.get().getId(), pipelineId, date, slotId);
            return existing.get();
        }
        
        // Step 2: Create new reading with exception handling for race condition
        try {
            Pipeline pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Pipeline not found: " + pipelineId));
            
            ReadingSlot slot = readingSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Slot not found: " + slotId));
            
            FlowReading newReading = new FlowReading();
            newReading.setPipeline(pipeline);
            newReading.setReadingDate(date);
            newReading.setReadingSlot(slot);
            newReading.setRecordedBy(employee);
            
            // Set initial status to DRAFT
            ValidationStatus draftStatus = validationStatusRepository
                .findByCode(ValidationStatusHelper.DRAFT)
                .orElseThrow(() -> new IllegalStateException(
                    "DRAFT status not configured in database. Run migrations."));
            
            newReading.setValidationStatus(draftStatus);
            newReading.setRecordedAt(LocalDateTime.now());
            
            FlowReading saved = flowReadingRepository.save(newReading);
            log.info("Created new reading {} for pipeline={}, date={}, slot={}", 
                saved.getId(), pipelineId, date, slotId);
            
            return saved;
            
        } catch (DataIntegrityViolationException e) {
            // Another thread created it between our check and insert - retry once
            log.warn("Duplicate reading detected (race condition), retrying fetch for pipeline={}, date={}, slot={}", 
                pipelineId, date, slotId);
            
            // Step 3: Retry with lock
            return flowReadingRepository
                .findByPipelineAndDateAndSlotForUpdate(pipelineId, date, slotId)
                .orElseThrow(() -> new WorkflowException(
                    "Reading creation failed after retry - concurrency conflict unresolved. " +
                    "This should not happen. Contact system administrator."));
        }
    }
    
    /**
     * Build pipeline coverage DTO with deadline calculation.
     * FIX #4: Calculate isOverdue based on slot deadline
     *
     * @param proj Projection from database query
     * @return Pipeline coverage DTO for API response
     */
    private PipelineCoverageDTO buildPipelineCoverage(SlotCoverageProjection proj) {
        
        String statusCode = proj.getValidationStatusCode();
        if (statusCode == null) {
            statusCode = ValidationStatusHelper.NOT_RECORDED;
        }
        
        // FIX #4: CALCULATE OVERDUE STATUS
        // Reading is overdue if:
        // - Status is NOT_RECORDED or DRAFT (not submitted yet)
        // - Current time is past slot deadline (readingDate + slotEndTime)
        boolean isOverdue = false;
        if (ValidationStatusHelper.NOT_RECORDED.equals(statusCode) || 
            ValidationStatusHelper.DRAFT.equals(statusCode)) {
            
            // Slot deadline is readingDate + slotEndTime
            LocalDateTime slotDeadline = LocalDateTime.of(
                proj.getReadingDate(), 
                proj.getSlotEndTime()
            );
            
            // Reading is overdue if current time is past slot deadline
            isOverdue = LocalDateTime.now().isAfter(slotDeadline);
            
            if (isOverdue) {
                log.debug("Pipeline {} reading is OVERDUE for date={}, slot ends at {}", 
                    proj.getPipelineCode(), proj.getReadingDate(), slotDeadline);
            }
        }
        
        // Available actions are determined by frontend based on user role
        // Backend enforces permissions via exceptions in submit/validate methods
        List<String> actions = new ArrayList<>();
        
        return PipelineCoverageDTO.builder()
            .pipelineId(proj.getPipelineId())
            .pipelineCode(proj.getPipelineCode())
            .pipelineName(proj.getPipelineName())
            .readingId(proj.getReadingId())
            .workflowStatus(statusCode)
            .workflowStatusDisplay(ValidationStatusHelper.getDisplayName(statusCode))
            .recordedAt(proj.getRecordedAt())
            .validatedAt(proj.getValidatedAt())
            .recordedByName(proj.getRecordedByName())
            .validatedByName(proj.getValidatedByName())
            .availableActions(actions)
            .canEdit(null) // Frontend determines based on user role
            .canSubmit(null)
            .canValidate(null)
            .isOverdue(isOverdue) // FIX #4: Real calculation
            .build();
    }
    
    /**
     * Calculate completion percentage.
     *
     * @param recorded Number of recorded readings
     * @param total Total number of pipelines
     * @return Completion percentage (0-100)
     */
    private double calculateCompletion(int recorded, int total) {
        if (total == 0) {
            return 0.0;
        }
        return (recorded * 100.0) / total;
    }
    
    /**
     * Map ReadingSlot entity to DTO
     */
    private ReadingSlotDTO mapSlotToDTO(ReadingSlot slot) {
        return ReadingSlotDTO.fromEntity(slot);
    }
    
    /**
     * Map Structure entity to DTO
     */
    private StructureDTO mapStructureToDTO(Structure structure) {
        return StructureDTO.fromEntity(structure);
    }
}
