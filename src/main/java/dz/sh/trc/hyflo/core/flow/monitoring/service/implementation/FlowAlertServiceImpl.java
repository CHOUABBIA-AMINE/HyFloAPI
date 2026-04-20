package dz.sh.trc.hyflo.core.flow.monitoring.service.implementation;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.core.flow.measurement.model.FlowReading;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.core.flow.monitoring.mapper.FlowAlertMapper;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowAlert;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowThreshold;
import dz.sh.trc.hyflo.core.flow.monitoring.repository.FlowAlertRepository;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowAlertService;
import dz.sh.trc.hyflo.core.flow.reference.model.AlertStatus;
import dz.sh.trc.hyflo.core.flow.reference.repository.AlertStatusRepository;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;
import dz.sh.trc.hyflo.platform.audit.service.AuditService;
import dz.sh.trc.hyflo.platform.event.WorkflowTransitionEvent;

@Service
public class FlowAlertServiceImpl extends AbstractCrudService<CreateFlowAlertRequest, UpdateFlowAlertRequest, FlowAlertResponse, FlowAlertSummary, FlowAlert> implements FlowAlertService {

    private final AlertStatusRepository alertStatusRepository;
    private final AuditService auditService;

    public FlowAlertServiceImpl(FlowAlertRepository repository,
                                FlowAlertMapper mapper,
                                ReferenceResolver referenceResolver,
                                ApplicationEventPublisher eventPublisher,
                                AlertStatusRepository alertStatusRepository,
                                AuditService auditService) {
        super(repository, mapper, referenceResolver, eventPublisher);
        this.alertStatusRepository = alertStatusRepository;
        this.auditService = auditService;
    }

    @Override
    protected Class<FlowAlert> getEntityClass() {
        return FlowAlert.class;
    }

    @Override
    protected void beforeCreate(CreateFlowAlertRequest request, FlowAlert entity) {
        // Resolve mandatory FK — threshold is NOT NULL in DB schema
        if (request.getThresholdId() == null) {
            throw new IllegalArgumentException("thresholdId is mandatory for FlowAlert creation");
        }
        entity.setThreshold(referenceResolver.resolve(request.getThresholdId(), FlowThreshold.class));

        // Resolve optional FK
        if (request.getFlowReadingId() != null) {
            entity.setFlowReading(referenceResolver.resolve(request.getFlowReadingId(), FlowReading.class));
        }

        // Auto-set initial status to ACTIVE (per A3 decision)
        AlertStatus activeStatus = alertStatusRepository.findByCode("ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("AlertStatus 'ACTIVE' not found. Please seed reference data."));
        entity.setStatus(activeStatus);

        // Event for alert trigger
        eventPublisher.publishEvent(WorkflowTransitionEvent.builder()
                .entityId(null) // ID not yet generated
                .entityType("FLOW_ALERT")
                .fromState(null)
                .toState("ACTIVE")
                .action("TRIGGER")
                .actorEmployeeId(null) // triggered by system threshold
                .build());

        // Audit log
        auditService.logWorkflow("FlowAlert", entity.getId(), null, "ACTIVE", "TRIGGER", null);
    }

    @Override
    @Transactional
    public void acknowledgeAlert(Long id, AcknowledgeAlertRequest request) {
        FlowAlert alert = findEntityById(id);

        // Guard: can only acknowledge an ACTIVE alert
        requireAlertStatus(alert, "ACTIVE", "acknowledge");

        alert.setAcknowledgedAt(LocalDateTime.now());
        if (request.getAcknowledgedByEmployeeId() != null) {
            alert.setAcknowledgedBy(referenceResolver.resolve(request.getAcknowledgedByEmployeeId(), Employee.class));
        }

        // Transition status: ACTIVE → ACKNOWLEDGED
        AlertStatus acknowledgedStatus = alertStatusRepository.findByCode("ACKNOWLEDGED")
                .orElseThrow(() -> new ResourceNotFoundException("AlertStatus 'ACKNOWLEDGED' not found"));
        alert.setStatus(acknowledgedStatus);

        repository.save(alert);

        // Event for alert acknowledgment
        eventPublisher.publishEvent(WorkflowTransitionEvent.builder()
                .entityId(alert.getId())
                .entityType("FLOW_ALERT")
                .fromState("ACTIVE")
                .toState("ACKNOWLEDGED")
                .action("ACKNOWLEDGE")
                .actorEmployeeId(request.getAcknowledgedByEmployeeId())
                .build());

        // Audit log
        auditService.logWorkflow("FlowAlert", alert.getId(), "ACTIVE", "ACKNOWLEDGED", "ACKNOWLEDGE", request.getAcknowledgedByEmployeeId());
    }

    @Override
    @Transactional
    public void resolveAlert(Long id, ResolveAlertRequest request) {
        FlowAlert alert = findEntityById(id);

        // Guard: can only resolve an ACKNOWLEDGED alert
        requireAlertStatus(alert, "ACKNOWLEDGED", "resolve");

        alert.setResolvedAt(LocalDateTime.now());
        alert.setResolutionNotes(request.getResolutionNotes());
        if (request.getResolvedByEmployeeId() != null) {
            alert.setResolvedBy(referenceResolver.resolve(request.getResolvedByEmployeeId(), Employee.class));
        }

        // Transition status: ACKNOWLEDGED → RESOLVED
        AlertStatus resolvedStatus = alertStatusRepository.findByCode("RESOLVED")
                .orElseThrow(() -> new ResourceNotFoundException("AlertStatus 'RESOLVED' not found"));
        alert.setStatus(resolvedStatus);

        repository.save(alert);

        // Event for alert resolution
        eventPublisher.publishEvent(WorkflowTransitionEvent.builder()
                .entityId(alert.getId())
                .entityType("FLOW_ALERT")
                .fromState("ACKNOWLEDGED")
                .toState("RESOLVED")
                .action("RESOLVE")
                .actorEmployeeId(request.getResolvedByEmployeeId())
                .build());

        // Audit log
        auditService.logWorkflow("FlowAlert", alert.getId(), "ACKNOWLEDGED", "RESOLVED", "RESOLVE", request.getResolvedByEmployeeId());
    }

    /**
     * Guard: ensures the alert's status matches the expected code before allowing the operation.
     */
    private void requireAlertStatus(FlowAlert alert, String expectedCode, String operation) {
        String currentCode = alert.getStatus() != null ? alert.getStatus().getCode() : "UNKNOWN";
        if (!expectedCode.equals(currentCode)) {
            throw new IllegalStateException(
                    "Cannot " + operation + " alert (id=" + alert.getId() + "): " +
                    "current status is '" + currentCode + "', expected '" + expectedCode + "'");
        }
    }
}
