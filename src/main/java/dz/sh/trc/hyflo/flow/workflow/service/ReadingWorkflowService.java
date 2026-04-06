/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ReadingWorkflowService
 *  @CreatedOn  : 02-10-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 21/22: v2 mapper, WorkflowInstance truth, derived generation
 *  @UpdatedOn  : 03-26-2026 — H1: Remove deprecated validate() stub
 *  @UpdatedOn  : 03-26-2026 — H4: Use asyncGenerateDerivedReadings() for non-blocking approval
 *  @UpdatedOn  : 03-26-2026 — H7: Add Micrometer counters for production observability
 *  @UpdatedOn  : 03-26-2026 — fix: setLastActorId → setLastActor(Employee)
 *
 *  @Type       : Class
 *  @Layer      : Service
 *  @Package    : Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowReadingMapper;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.flow.workflow.event.ReadingRejectedEvent;
import dz.sh.trc.hyflo.flow.workflow.event.ReadingValidatedEvent;
import dz.sh.trc.hyflo.flow.workflow.model.WorkflowInstance;
import dz.sh.trc.hyflo.flow.workflow.repository.WorkflowInstanceRepository;
import dz.sh.trc.hyflo.flow.workflow.util.FlowReadingIdentifierBuilder;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadingWorkflowService {

    private final FlowReadingRepository      flowReadingRepository;
    private final ValidationStatusRepository  validationStatusRepository;
    private final WorkflowInstanceRepository  workflowInstanceRepository;
    private final EmployeeRepository          employeeRepository;
    private final ApplicationEventPublisher   eventPublisher;
    private final SegmentDistributionService  segmentDistributionService;
    private final MeterRegistry              meterRegistry;

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Counter approvedCounter;
    private Counter rejectedCounter;
    private Counter derivedSuccessCounter;
    private Counter derivedFailureCounter;

    @PostConstruct
    void initMetrics() {
        approvedCounter      = meterRegistry.counter("reading.approved",          "module", "workflow");
        rejectedCounter      = meterRegistry.counter("reading.rejected",          "module", "workflow");
        derivedSuccessCounter = meterRegistry.counter("derived.generation.success", "module", "workflow");
        derivedFailureCounter = meterRegistry.counter("derived.generation.failure", "module", "workflow");
    }

    // =====================================================================
    //  APPROVE
    // =====================================================================

    @Transactional
    public FlowReadingReadDTO approve(Long id, Long approvedById) {
        log.info("Approving flow reading ID: {} by employee ID: {}", id, approvedById);

        FlowReading reading  = requireReading(id);
        Employee    approver = requireEmployee(approvedById);

        guardAgainstInvalidTransition(reading, "APPROVED");

        // Update WorkflowInstance — source of truth
        if (reading.getWorkflowInstance() != null) {
            WorkflowInstance wf = workflowInstanceRepository
                    .findById(reading.getWorkflowInstance().getId())
                    .orElse(reading.getWorkflowInstance());
            wf.setLastActor(approver);           // Employee FK — not a raw Long
            wf.setUpdatedAt(LocalDateTime.now());
            workflowInstanceRepository.save(wf);
        }

        ValidationStatus approvedStatus = validationStatusRepository.findByCode("APPROVED")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Validation status APPROVED not configured."));

        reading.setValidationStatus(approvedStatus);
        reading.setValidatedBy(approver);
        reading.setValidatedAt(LocalDateTime.now());

        FlowReading saved = flowReadingRepository.save(reading);
        log.info("Flow reading ID: {} approved by employee ID: {}", id, approvedById);

        approvedCounter.increment();

        eventPublisher.publishEvent(ReadingValidatedEvent.create(
                saved.getId(),
                buildActorName(approver),
                buildActorName(saved.getRecordedBy()),
                buildReadingIdentifier(saved),
                null,
                LocalDateTime.now().format(DATETIME_FORMATTER)));

        segmentDistributionService.asyncGenerateDerivedReadings(saved)
                .thenRun(() -> {
                    derivedSuccessCounter.increment();
                    log.debug("[METRICS] derived.generation.success for reading ID: {}", saved.getId());
                })
                .exceptionally(ex -> {
                    derivedFailureCounter.increment();
                    log.debug("[METRICS] derived.generation.failure for reading ID: {}", saved.getId());
                    return null;
                });

        return FlowReadingMapper.toReadDTO(saved);
    }

    // =====================================================================
    //  REJECT
    // =====================================================================

    @Transactional
    public FlowReadingReadDTO reject(Long id, Long rejectedById, String rejectionReason) {
        log.info("Rejecting flow reading ID: {} by employee ID: {} reason: {}",
                id, rejectedById, rejectionReason);

        FlowReading reading  = requireReading(id);
        Employee    rejector = requireEmployee(rejectedById);

        guardAgainstInvalidTransition(reading, "REJECTED");

        if (reading.getWorkflowInstance() != null) {
            WorkflowInstance wf = workflowInstanceRepository
                    .findById(reading.getWorkflowInstance().getId())
                    .orElse(reading.getWorkflowInstance());
            wf.setLastActor(rejector);           // Employee FK — not a raw Long
            wf.setUpdatedAt(LocalDateTime.now());
            workflowInstanceRepository.save(wf);
        }

        ValidationStatus rejectedStatus = validationStatusRepository.findByCode("REJECTED")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Validation status REJECTED not configured."));

        reading.setValidationStatus(rejectedStatus);
        reading.setValidatedBy(rejector);
        reading.setValidatedAt(LocalDateTime.now());

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

        rejectedCounter.increment();

        eventPublisher.publishEvent(ReadingRejectedEvent.create(
                saved.getId(),
                buildActorName(rejector),
                buildActorName(saved.getRecordedBy()),
                buildReadingIdentifier(saved),
                rejectionReason,
                LocalDateTime.now().format(DATETIME_FORMATTER)));

        return FlowReadingMapper.toReadDTO(saved);
    }

    // =====================================================================
    //  Guards and Helpers
    // =====================================================================

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
            log.warn("Identifier build failed for reading ID: {}, using fallback",
                    reading.getId(), e);
            return "Reading#" + reading.getId();
        }
    }
}
