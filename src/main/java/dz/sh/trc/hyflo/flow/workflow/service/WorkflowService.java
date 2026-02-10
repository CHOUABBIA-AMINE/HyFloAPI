/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: FlowReadingWorkflowService
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-10-2026 - Extracted from FlowReadingService (SRP refactoring)
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Core
 *
 * 	@Description: Service handling flow reading workflow state transitions.
 * 	              Extracted from FlowReadingService to follow Single Responsibility Principle.
 *
 * 	@Refactoring: Extracted from FlowReadingService
 * 	              
 * 	              Rationale (SRP - Single Responsibility Principle):
 * 	              - FlowReadingService: CRUD operations (create, read, update, delete, queries)
 * 	              - FlowReadingWorkflowService: Workflow transitions (validate, reject, submit)
 * 	              
 * 	              Benefits:
 * 	              - Clear separation of concerns
 * 	              - Easier testing (mock workflow dependencies separately)
 * 	              - Better maintainability (workflow logic isolated)
 * 	              - Future-proof (room for additional workflow states like PENDING, REVIEWING)
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.common.util.FlowReadingIdentifierBuilder;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.core.event.ReadingRejectedEvent;
import dz.sh.trc.hyflo.flow.core.event.ReadingValidatedEvent;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service handling flow reading workflow state transitions.
 * 
 * This service manages:
 * - Validation workflow (validate readings)
 * - Rejection workflow (reject invalid readings)
 * - Event publishing for workflow transitions
 * 
 * Separated from FlowReadingService to follow Single Responsibility Principle:
 * - FlowReadingService: CRUD operations
 * - FlowReadingWorkflowService: Workflow state transitions
 * 
 * Future enhancements:
 * - Add PENDING → REVIEWING transition
 * - Add resubmission workflow (REJECTED → SUBMITTED)
 * - Add bulk validation operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WorkflowService {

    private final FlowReadingRepository flowReadingRepository;
    private final ValidationStatusRepository validationStatusRepository;
    private final EmployeeRepository employeeRepository;
    private final ApplicationEventPublisher eventPublisher;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ========== VALIDATION WORKFLOW METHODS ==========

    /**
     * Validate a flow reading
     * 
     * Updates the reading status to VALIDATED and records validator information.
     * Publishes ReadingValidatedEvent via generic notification system.
     * 
     * Workflow transition: SUBMITTED → VALIDATED
     * 
     * @param id Reading ID
     * @param validatedById Employee ID of the validator
     * @return Updated reading DTO with VALIDATED status
     * @throws ResourceNotFoundException if reading, status, or employee not found
     */
    @Transactional
    public FlowReadingDTO validate(Long id, Long validatedById) {
        log.info("Validating flow reading {} by employee {}", id, validatedById);
        
        FlowReading reading = flowReadingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Flow reading with ID %d not found", id)));
        
        String originalRecorder = reading.getRecordedBy() != null 
                ? reading.getRecordedBy().getLastNameLt() + " " + reading.getRecordedBy().getFirstNameLt()
                : "Unknown";
        
        ValidationStatus validatedStatus = validationStatusRepository.findByCode("VALIDATED")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "VALIDATED status not found in database. Please configure validation statuses."));
        
        Employee validator = employeeRepository.findById(validatedById)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Employee with ID %d not found", validatedById)));
        
        // Update reading
        reading.setValidationStatus(validatedStatus);
        reading.setValidatedBy(validator);
        reading.setValidatedAt(LocalDateTime.now());
        
        FlowReading saved = flowReadingRepository.save(reading);
        log.info("Successfully validated flow reading {} by employee {} ({})", 
                 id, validator.getRegistrationNumber(), 
                 validator.getLastNameLt() + " " + validator.getFirstNameLt());
        
        // Publish validation event using generic notification system
        String readingIdentifier = buildReadingIdentifier(saved);
        String validatorUsername = validator != null 
            ? validator.getLastNameLt() + " " + validator.getFirstNameLt() 
            : validator.getRegistrationNumber();
        
        ReadingValidatedEvent event = ReadingValidatedEvent.create(
                saved.getId(),
                validatorUsername,
                originalRecorder,
                readingIdentifier,
                null, // Optional comment - can be extended
                LocalDateTime.now().format(DATETIME_FORMATTER)
        );
        
        eventPublisher.publishEvent(event);
        log.debug("Published ReadingValidatedEvent for reading ID: {}", saved.getId());
        
        return FlowReadingDTO.fromEntity(saved);
    }

    /**
     * Reject a flow reading
     * 
     * Updates the reading status to REJECTED and records rejection information.
     * Appends rejection reason to reading notes for audit trail.
     * Publishes ReadingRejectedEvent via generic notification system.
     * 
     * Workflow transition: SUBMITTED → REJECTED
     * 
     * @param id Reading ID
     * @param rejectedById Employee ID of the rejector
     * @param rejectionReason Reason for rejection
     * @return Updated reading DTO with REJECTED status
     * @throws EntityNotFoundException if reading, status, or employee not found
     */
    @Transactional
    public FlowReadingDTO reject(Long id, Long rejectedById, String rejectionReason) {
        log.info("Rejecting flow reading {} by employee {} with reason: {}", 
                 id, rejectedById, rejectionReason);
        
        FlowReading reading = flowReadingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Flow reading with ID %d not found", id)));
        
        String originalRecorder = reading.getRecordedBy() != null 
                ? reading.getRecordedBy().getLastNameLt() + " " + reading.getRecordedBy().getFirstNameLt()
                : "Unknown";
        
        ValidationStatus rejectedStatus = validationStatusRepository.findByCode("REJECTED")
                .orElseThrow(() -> new EntityNotFoundException(
                        "REJECTED status not found in database. Please configure validation statuses."));
        
        Employee rejector = employeeRepository.findById(rejectedById)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Employee with ID %d not found", rejectedById)));
        
        // Update reading
        reading.setValidationStatus(rejectedStatus);
        reading.setValidatedBy(rejector);
        reading.setValidatedAt(LocalDateTime.now());
        
        // Append rejection reason to notes for audit trail
        String existingNotes = reading.getNotes();
        String rejectionNote = String.format("\n\n=== REJECTION ===\nRejected by: %s %s (%s)\nDate: %s\nReason: %s",
                rejector.getFirstNameLt(), 
                rejector.getLastNameLt(),
                rejector.getRegistrationNumber(),
                LocalDateTime.now(),
                rejectionReason);
        reading.setNotes(existingNotes != null ? existingNotes + rejectionNote : rejectionNote.trim());
        
        FlowReading saved = flowReadingRepository.save(reading);
        log.info("Successfully rejected flow reading {} by employee {} ({})", 
                 id, rejector.getRegistrationNumber(), 
                 rejector.getFirstNameLt() + " " + rejector.getLastNameLt());
        
        // Publish rejection event using generic notification system
        String readingIdentifier = buildReadingIdentifier(saved);
        String rejectorUsername = rejector != null 
            ? rejector.getLastNameLt() + " " + rejector.getFirstNameLt()
            : rejector.getRegistrationNumber();
        
        ReadingRejectedEvent event = ReadingRejectedEvent.create(
                saved.getId(),
                rejectorUsername,
                originalRecorder,
                readingIdentifier,
                rejectionReason,
                LocalDateTime.now().format(DATETIME_FORMATTER)
        );
        
        eventPublisher.publishEvent(event);
        log.debug("Published ReadingRejectedEvent for reading ID: {}", saved.getId());
        
        return FlowReadingDTO.fromEntity(saved);
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
    private String buildReadingIdentifier(FlowReading reading) {
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
    }

    // ========== FUTURE ENHANCEMENTS ==========

    /**
     * TODO: Add resubmission workflow
     * 
     * Allow rejected readings to be resubmitted after corrections:
     * - Transition: REJECTED → SUBMITTED
     * - Reset validation fields (validatedBy, validatedAt)
     * - Append resubmission note with corrections made
     * - Publish ReadingResubmittedEvent
     * 
     * @param id Reading ID
     * @param resubmittedById Employee ID resubmitting
     * @param corrections Description of corrections made
     * @return Updated reading DTO with SUBMITTED status
     */
    // @Transactional
    // public FlowReadingDTO resubmit(Long id, Long resubmittedById, String corrections) { ... }

    /**
     * TODO: Add bulk validation
     * 
     * Validate multiple readings in one transaction:
     * - Useful for validators approving a batch
     * - Single database transaction for atomicity
     * - Publish single event with list of validated readings
     * 
     * @param readingIds List of reading IDs
     * @param validatedById Validator employee ID
     * @return List of validated reading DTOs
     */
    // @Transactional
    // public List<FlowReadingDTO> validateBatch(List<Long> readingIds, Long validatedById) { ... }
}
