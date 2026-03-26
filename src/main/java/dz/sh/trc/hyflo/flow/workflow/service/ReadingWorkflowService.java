/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ReadingWorkflowService
 *  @CreatedOn  : 02-10-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 21/22: v2 mapper, WorkflowInstance truth, derived generation
 *  @UpdatedOn  : 03-26-2026 — H1: removed deprecated validate() stub and FlowReadingDTO.fromEntity() path
 *
 *  @Type       : Class
 *  @Layer      : Service
 *  @Package    : Flow / Workflow
 *
 *  @Description: Orchestrates flow reading lifecycle transitions.
 *                WorkflowInstance is the source of truth for state.
 *                ValidationStatus is maintained as a compatibility projection.
 *                Returns FlowReadingReadDto — NEVER FlowReadingDTO (deprecated legacy).
 *
 *  Phase 3 — Commit 21 + Commit 22
 *  Phase H1 — validate() stub fully removed; no more DTO self-mapping in any path.
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.core.dto.DerivedFlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.mapper.FlowReadingMapper;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.flow.core.service.SegmentDistributionService;
import dz.sh.trc.hyflo.flow.workflow.event.ReadingRejectedEvent;
import dz.sh.trc.hyflo.flow.workflow.event.ReadingValidatedEvent;
import dz.sh.trc.hyflo.flow.workflow.model.WorkflowInstance;
import dz.sh.trc.hyflo.flow.workflow.repository.WorkflowInstanceRepository;
import dz.sh.trc.hyflo.flow.workflow.util.FlowReadingIdentifierBuilder;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Orchestrates flow reading lifecycle transitions.
 *
 * WorkflowInstance is the source of truth for state.
 * ValidationStatus is maintained as a compatibility projection only.
 *
 * Returns FlowReadingReadDto exclusively — no legacy FlowReadingDTO.fromEntity().
 *
 * State transitions managed:
 *   SUBMITTED → APPROVED  (approve)
 *   SUBMITTED → REJECTED  (reject)
 *   REJECTED  → SUBMITTED (resubmit — stub for future Phase 4+)
 *
 * Commit 22 integration:
 *   SegmentDistributionService.generateDerivedReadings() is triggered inside approve()
 *   AFTER reading is saved and event is published.
 *   Generation failure is non-fatal — approval remains authoritative.
 *
 * H1: validate() deprecated stub removed. All callers must use approve().
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadingWorkflowService {

    private final FlowReadingRepository flowReadingRepository;
    private final ValidationStatusRepository validationStatusRepository;
    private final WorkflowInstanceRepository workflowInstanceRepository;
    private final EmployeeRepository employeeRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SegmentDistributionService segmentDistributionService;

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // =====================================================================
    //  APPROVE (Commit 21) — sole approval entry point
    // =====================================================================

    /**
     * Approve a submitted flow reading.
     *
     * Workflow transition: SUBMITTED → APPROVED
     *
     * Side effects:
     * - WorkflowInstance.lastActorId and updatedAt updated (source of truth)
     * - ValidationStatus updated to APPROVED (compatibility projection)
     * - ReadingValidatedEvent published
     * - SegmentDistributionService.generateDerivedReadings() triggered (Commit 22)
     *
     * @param id          Reading ID
     * @param approvedById Employee ID of the approver
     * @return FlowReadingReadDto with APPROVED status
     */
    @Transactional
    public FlowReadingReadDto approve(Long id, Long approvedById) {
        log.info("Approving flow reading ID: {} by employee ID: {}", id, approvedById);

        FlowReading reading = requireReading(id);
        Employee approver = requireEmployee(approvedById);

        guardAgainstInvalidTransition(reading, "APPROVED");

        // Update WorkflowInstance — source of truth
        if (reading.getWorkflowInstance() != null) {
            WorkflowInstance wf = workflowInstanceRepository
                    .findById(reading.getWorkflowInstance().getId())
                    .orElse(reading.getWorkflowInstance());
            wf.setLastActorId(approvedById);
            wf.setUpdatedAt(LocalDateTime.now());
            workflowInstanceRepository.save(wf);
        }

        // Compatibility projection — ValidationStatus
        ValidationStatus approvedStatus = validationStatusRepository.findByCode("APPROVED")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Validation status APPROVED not configured."));

        reading.setValidationStatus(approvedStatus);
        reading.setValidatedBy(approver);
        reading.setValidatedAt(LocalDateTime.now());

        FlowReading saved = flowReadingRepository.save(reading);
        log.info("Flow reading ID: {} approved by employee ID: {}", id, approvedById);

        // Publish event
        String originalRecorder = buildActorName(saved.getRecordedBy());
        eventPublisher.publishEvent(ReadingValidatedEvent.create(
                saved.getId(),
                buildActorName(approver),
                originalRecorder,
                buildReadingIdentifier(saved),
                null,
                LocalDateTime.now().format(DATETIME_FORMATTER)));

        // Commit 22 — Trigger derived reading generation AFTER approval
        // Non-fatal: generation failure never rolls back the approval
        try {
            List<DerivedFlowReadingReadDto> derived =
                    segmentDistributionService.generateDerivedReadings(saved);
            log.info("Generated {} derived readings for approved reading ID: {}",
                    derived.size(), saved.getId());
        } catch (Exception e) {
            log.error("Derived reading generation failed for reading ID: {}. "
                    + "Approval remains valid. Manual regeneration may be needed.",
                    saved.getId(), e);
        }

        return FlowReadingMapper.toReadDto(saved);
    }

    // =====================================================================
    //  REJECT
    // =====================================================================

    /**
     * Reject a submitted flow reading.
     *
     * Workflow transition: SUBMITTED → REJECTED
     *
     * @param id              Reading ID
     * @param rejectedById    Employee ID of the rejector
     * @param rejectionReason Reason for rejection (appended to audit notes)
     * @return FlowReadingReadDto with REJECTED status
     */
    @Transactional
    public FlowReadingReadDto reject(Long id, Long rejectedById, String rejectionReason) {
        log.info("Rejecting flow reading ID: {} by employee ID: {} with reason: {}",
                id, rejectedById, rejectionReason);

        FlowReading reading = requireReading(id);
        Employee rejector = requireEmployee(rejectedById);

        guardAgainstInvalidTransition(reading, "REJECTED");

        // Update WorkflowInstance — source of truth
        if (reading.getWorkflowInstance() != null) {
            WorkflowInstance wf = workflowInstanceRepository
                    .findById(reading.getWorkflowInstance().getId())
                    .orElse(reading.getWorkflowInstance());
            wf.setLastActorId(rejectedById);
            wf.setUpdatedAt(LocalDateTime.now());
            workflowInstanceRepository.save(wf);
        }

        // Compatibility projection
        ValidationStatus rejectedStatus = validationStatusRepository.findByCode("REJECTED")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Validation status REJECTED not configured."));

        reading.setValidationStatus(rejectedStatus);
        reading.setValidatedBy(rejector);
        reading.setValidatedAt(LocalDateTime.now());

        // Append rejection audit trail to notes
        String rejectionNote = String.format(
                "\n\n=== REJECTION ===\nRejected by: %s %s (%s)\nDate: %s\nReason: %s",
                rejector.getFirstNameLt(),
                rejector.getLastNameLt(),
                rejector.getRegistrationNumber(),
                LocalDateTime.now(),
                rejectionReason);
        reading.setNotes(reading.getNotes() != null
                ? reading.getNotes() + rejectionNote : rejectionNote.trim());

        FlowReading saved = flowReadingRepository.save(reading);
        log.info("Flow reading ID: {} rejected by employee ID: {}", id, rejectedById);

        // No derived reading action on rejection (Commit 22 rule)
        eventPublisher.publishEvent(ReadingRejectedEvent.create(
                saved.getId(),
                buildActorName(rejector),
                buildActorName(saved.getRecordedBy()),
                buildReadingIdentifier(saved),
                rejectionReason,
                LocalDateTime.now().format(DATETIME_FORMATTER)));

        return FlowReadingMapper.toReadDto(saved);
    }

    // =====================================================================
    //  Guards and Helpers
    // =====================================================================

    /**
     * Guard against invalid state transitions.
     * APPROVED readings cannot be re-approved or directly rejected.
     * Use resubmit workflow (future) to move from APPROVED back into SUBMITTED.
     */
    private void guardAgainstInvalidTransition(FlowReading reading, String targetStateCode) {
        if (reading.getValidationStatus() == null) return;
        String current = reading.getValidationStatus().getCode();
        if ("APPROVED".equalsIgnoreCase(current) && "APPROVED".equalsIgnoreCase(targetStateCode)) {
            throw new IllegalStateException(
                    "FlowReading ID=" + reading.getId() + " is already APPROVED.");
        }
        if ("APPROVED".equalsIgnoreCase(current) && "REJECTED".equalsIgnoreCase(targetStateCode)) {
            throw new IllegalStateException(
                    "Cannot reject an already APPROVED reading. Use resubmit workflow.");
        }
    }

    private FlowReading requireReading(Long id) {
        return flowReadingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowReading not found: " + id));
    }

    private Employee requireEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + id));
    }

    private String buildActorName(Employee employee) {
        if (employee == null) return "Unknown";
        return employee.getLastNameLt() + " " + employee.getFirstNameLt();
    }

    private String buildReadingIdentifier(FlowReading reading) {
        try {
            return FlowReadingIdentifierBuilder.buildIdentifier(
                    reading.getPipeline(), reading.getReadingDate(), reading.getReadingSlot());
        } catch (Exception e) {
            log.warn("Identifier build failed for reading ID: {}, using fallback", reading.getId(), e);
            return "Reading#" + reading.getId();
        }
    }
}
