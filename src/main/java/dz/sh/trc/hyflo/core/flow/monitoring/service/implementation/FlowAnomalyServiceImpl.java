package dz.sh.trc.hyflo.core.flow.monitoring.service.implementation;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.core.flow.measurement.model.FlowReading;
import dz.sh.trc.hyflo.core.flow.measurement.model.SegmentFlowReading;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.AnalyzeAnomalyRequest;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.CreateFlowAnomalyRequest;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.FixAnomalyRequest;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.FlowAnomalyResponse;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.FlowAnomalySummary;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.UpdateFlowAnomalyRequest;
import dz.sh.trc.hyflo.core.flow.monitoring.mapper.FlowAnomalyMapper;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowAnomaly;
import dz.sh.trc.hyflo.core.flow.monitoring.repository.FlowAnomalyRepository;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowAnomalyService;
import dz.sh.trc.hyflo.core.flow.reference.model.EventStatus;
import dz.sh.trc.hyflo.core.flow.reference.repository.EventStatusRepository;
import dz.sh.trc.hyflo.core.network.topology.model.PipelineSegment;
import dz.sh.trc.hyflo.core.system.audit.service.AuditService;
import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import dz.sh.trc.hyflo.platform.event.WorkflowTransitionEvent;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class FlowAnomalyServiceImpl extends AbstractCrudService<CreateFlowAnomalyRequest, UpdateFlowAnomalyRequest, FlowAnomalyResponse, FlowAnomalySummary, FlowAnomaly> implements FlowAnomalyService {

    private final EventStatusRepository eventStatusRepository;
    private final AuditService auditService;

    public FlowAnomalyServiceImpl(FlowAnomalyRepository repository,
                                  FlowAnomalyMapper mapper,
                                  ReferenceResolver referenceResolver,
                                  ApplicationEventPublisher eventPublisher,
                                  EventStatusRepository eventStatusRepository,
                                  AuditService auditService) {
        super(repository, mapper, referenceResolver, eventPublisher);
        this.eventStatusRepository = eventStatusRepository;
        this.auditService = auditService;
    }

    @Override
    protected Class<FlowAnomaly> getEntityClass() {
        return FlowAnomaly.class;
    }

    @Override
    protected void beforeCreate(CreateFlowAnomalyRequest request, FlowAnomaly entity) {
        if (request.getReadingId() != null) {
            entity.setReading(referenceResolver.resolve(request.getReadingId(), FlowReading.class));
        }
        if (request.getSegmentFlowReadingId() != null) {
            entity.setSegmentFlowReading(referenceResolver.resolve(request.getSegmentFlowReadingId(), SegmentFlowReading.class));
        }
        if (request.getPipelineSegmentId() != null) {
            entity.setPipelineSegment(referenceResolver.resolve(request.getPipelineSegmentId(), PipelineSegment.class));
        }

        // Auto-set initial status
        EventStatus status = eventStatusRepository.findByCode("DETECTED")
                .orElseGet(() -> eventStatusRepository.findByCode("ACTIVE")
                        .orElseThrow(() -> new ResourceNotFoundException("EventStatus 'DETECTED' or 'ACTIVE' not found")));
        entity.setStatus(status);
        
        if (entity.getDetectedAt() == null) {
            entity.setDetectedAt(LocalDateTime.now());
        }
    }

    @Override
    @Transactional
    public void analyzeAnomaly(Long id, AnalyzeAnomalyRequest request) {
        FlowAnomaly anomaly = findEntityById(id);

        requireAnomalyStatus(anomaly, "DETECTED");

        anomaly.setExplanation(anomaly.getExplanation() + "\n--- Analysis Notes ---\n" + request.analysisNotes());
        
        EventStatus status = eventStatusRepository.findByCode("ANALYZING")
                .orElseThrow(() -> new ResourceNotFoundException("EventStatus 'ANALYZING' not found"));
        anomaly.setStatus(status);

        repository.save(anomaly);

        auditService.logWorkflow("FlowAnomaly", id, "DETECTED", "ANALYZING", "ANALYZE", request.analystEmployeeId());
        publishTransitionEvent(id, "DETECTED", "ANALYZING", "ANALYZE", request.analystEmployeeId());
    }

    @Override
    @Transactional
    public void fixAnomaly(Long id, FixAnomalyRequest request) {
        FlowAnomaly anomaly = findEntityById(id);

        requireAnomalyStatus(anomaly, "ANALYZING");

        anomaly.setExplanation(anomaly.getExplanation() + "\n--- Fix Description ---\n" + request.fixDescription());
        
        EventStatus status = eventStatusRepository.findByCode("FIXED")
                .orElseThrow(() -> new ResourceNotFoundException("EventStatus 'FIXED' not found"));
        anomaly.setStatus(status);

        repository.save(anomaly);

        auditService.logWorkflow("FlowAnomaly", id, "ANALYZING", "FIXED", "FIX", request.fixedByEmployeeId());
        publishTransitionEvent(id, "ANALYZING", "FIXED", "FIX", request.fixedByEmployeeId());
    }

    private void requireAnomalyStatus(FlowAnomaly anomaly, String expectedCode) {
        String currentCode = anomaly.getStatus() != null ? anomaly.getStatus().getCode() : "UNKNOWN";
        if (!expectedCode.equals(currentCode)) {
            throw new IllegalStateException("Cannot transition anomaly: current status is " + currentCode + ", expected " + expectedCode);
        }
    }

    private void publishTransitionEvent(Long id, String from, String to, String action, Long actorId) {
        eventPublisher.publishEvent(WorkflowTransitionEvent.builder()
                .entityId(id)
                .entityType("FLOW_ANOMALY")
                .fromState(from)
                .toState(to)
                .action(action)
                .actorEmployeeId(actorId)
                .build());
    }
}
