package dz.sh.trc.hyflo.core.flow.monitoring.service.implementation;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.core.flow.measurement.model.FlowReading;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.core.flow.monitoring.mapper.FlowIncidentMapper;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowAlert;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowIncident;
import dz.sh.trc.hyflo.core.flow.monitoring.repository.FlowIncidentRepository;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowIncidentService;
import dz.sh.trc.hyflo.core.flow.reference.model.EventStatus;
import dz.sh.trc.hyflo.core.flow.reference.model.Severity;
import dz.sh.trc.hyflo.core.flow.reference.repository.EventStatusRepository;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.core.network.topology.model.Infrastructure;
import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import dz.sh.trc.hyflo.platform.audit.service.AuditService;
import dz.sh.trc.hyflo.platform.event.WorkflowTransitionEvent;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class FlowIncidentServiceImpl extends AbstractCrudService<CreateFlowIncidentRequest, UpdateFlowIncidentRequest, FlowIncidentResponse, FlowIncidentSummary, FlowIncident> implements FlowIncidentService {

    private final EventStatusRepository eventStatusRepository;
    private final AuditService auditService;

    public FlowIncidentServiceImpl(FlowIncidentRepository repository,
                                   FlowIncidentMapper mapper,
                                   ReferenceResolver referenceResolver,
                                   ApplicationEventPublisher eventPublisher,
                                   EventStatusRepository eventStatusRepository,
                                   AuditService auditService) {
        super(repository, mapper, referenceResolver, eventPublisher);
        this.eventStatusRepository = eventStatusRepository;
        this.auditService = auditService;
    }

    @Override
    protected Class<FlowIncident> getEntityClass() {
        return FlowIncident.class;
    }

    @Override
    protected void beforeCreate(CreateFlowIncidentRequest request, FlowIncident entity) {
        entity.setInfrastructure(referenceResolver.resolve(request.getInfrastructureId(), Infrastructure.class));
        entity.setReportedBy(referenceResolver.resolve(request.getReportedById(), Employee.class));
        
        if (request.getSeverityId() != null) {
            entity.setSeverity(referenceResolver.resolve(request.getSeverityId(), Severity.class));
        }
        if (request.getRelatedReadingId() != null) {
            entity.setRelatedReading(referenceResolver.resolve(request.getRelatedReadingId(), FlowReading.class));
        }
        if (request.getRelatedAlertId() != null) {
            entity.setRelatedAlert(referenceResolver.resolve(request.getRelatedAlertId(), FlowAlert.class));
        }

        // Auto-set initial status to OPEN (or ACTIVE)
        EventStatus initialStatus = eventStatusRepository.findByCode("OPEN")
                .orElseGet(() -> eventStatusRepository.findByCode("ACTIVE")
                        .orElseThrow(() -> new ResourceNotFoundException("EventStatus 'OPEN' or 'ACTIVE' not found")));
        entity.setStatus(initialStatus);
        
        if (entity.getEventTimestamp() == null) {
            entity.setEventTimestamp(LocalDateTime.now());
        }
    }

    @Override
    @Transactional
    public void investigateIncident(Long id, InvestigateIncidentRequest request) {
        FlowIncident incident = findEntityById(id);

        // Guard: can only investigate an OPEN incident
        requireIncidentStatus(incident, "OPEN");

        incident.setStartTime(LocalDateTime.now());
        incident.setDescription(incident.getDescription() + "\n--- Investigation Notes ---\n" + request.investigationNotes());
        
        // Transition status: OPEN -> INVESTIGATING
        EventStatus status = eventStatusRepository.findByCode("INVESTIGATING")
                .orElseThrow(() -> new ResourceNotFoundException("EventStatus 'INVESTIGATING' not found"));
        incident.setStatus(status);

        repository.save(incident);

        // Audit & Event
        auditService.logWorkflow("FlowIncident", id, "OPEN", "INVESTIGATING", "INVESTIGATE", request.investigatorEmployeeId());
        publishTransitionEvent(id, "OPEN", "INVESTIGATING", "INVESTIGATE", request.investigatorEmployeeId());
    }

    @Override
    @Transactional
    public void resolveIncident(Long id, ResolveIncidentRequest request) {
        FlowIncident incident = findEntityById(id);

        // Guard: can only resolve an INVESTIGATING incident
        requireIncidentStatus(incident, "INVESTIGATING");

        incident.setEndTime(LocalDateTime.now());
        incident.setActionTaken(request.correctiveActionTaken());
        incident.setDescription(incident.getDescription() + "\n--- Resolution Notes ---\n" + request.resolutionNotes());
        
        // Transition status: INVESTIGATING -> RESOLVED
        EventStatus status = eventStatusRepository.findByCode("RESOLVED")
                .orElseThrow(() -> new ResourceNotFoundException("EventStatus 'RESOLVED' not found"));
        incident.setStatus(status);

        repository.save(incident);

        // Audit & Event
        auditService.logWorkflow("FlowIncident", id, "INVESTIGATING", "RESOLVED", "RESOLVE", request.resolvedByEmployeeId());
        publishTransitionEvent(id, "INVESTIGATING", "RESOLVED", "RESOLVE", request.resolvedByEmployeeId());
    }

    private void requireIncidentStatus(FlowIncident incident, String expectedCode) {
        String currentCode = incident.getStatus() != null ? incident.getStatus().getCode() : "UNKNOWN";
        if (!expectedCode.equals(currentCode)) {
            throw new IllegalStateException("Cannot transition incident: current status is " + currentCode + ", expected " + expectedCode);
        }
    }

    private void publishTransitionEvent(Long id, String from, String to, String action, Long actorId) {
        eventPublisher.publishEvent(WorkflowTransitionEvent.builder()
                .entityId(id)
                .entityType("FLOW_INCIDENT")
                .fromState(from)
                .toState(to)
                .action(action)
                .actorEmployeeId(actorId)
                .build());
    }
}
