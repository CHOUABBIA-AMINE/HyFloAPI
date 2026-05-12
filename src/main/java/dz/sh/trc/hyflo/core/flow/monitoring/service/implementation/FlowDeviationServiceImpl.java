package dz.sh.trc.hyflo.core.flow.monitoring.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.measurement.model.FlowReading;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.CreateFlowDeviationRequest;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.FlowDeviationResponse;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.FlowDeviationSummary;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.UpdateFlowDeviationRequest;
import dz.sh.trc.hyflo.core.flow.monitoring.mapper.FlowDeviationMapper;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowDeviation;
import dz.sh.trc.hyflo.core.flow.monitoring.repository.FlowDeviationRepository;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowDeviationService;
import dz.sh.trc.hyflo.core.flow.planning.model.FlowPlan;
import dz.sh.trc.hyflo.core.network.topology.model.Pipeline;
import dz.sh.trc.hyflo.platform.event.WorkflowTransitionEvent;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class FlowDeviationServiceImpl extends AbstractCrudService<CreateFlowDeviationRequest, UpdateFlowDeviationRequest, FlowDeviationResponse, FlowDeviationSummary, FlowDeviation> implements FlowDeviationService {

    public FlowDeviationServiceImpl(FlowDeviationRepository repository, 
                                    FlowDeviationMapper mapper, 
                                    ReferenceResolver referenceResolver, 
                                    ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<FlowDeviation> getEntityClass() {
        return FlowDeviation.class;
    }

    @Override
    protected void beforeCreate(CreateFlowDeviationRequest request, FlowDeviation entity) {
        if (request.getPipelineId() != null) {
            entity.setPipeline(referenceResolver.resolve(request.getPipelineId(), Pipeline.class));
        }
        if (request.getReadingId() != null) {
            entity.setReading(referenceResolver.resolve(request.getReadingId(), FlowReading.class));
        }
        if (request.getPlanId() != null) {
            entity.setPlan(referenceResolver.resolve(request.getPlanId(), FlowPlan.class));
        }

        // Logic for deviation calculation if not provided
        if (entity.getDeviationValue() == null && entity.getActualValue() != null && entity.getExpectedValue() != null) {
            entity.setDeviationValue(entity.getExpectedValue().subtract(entity.getActualValue()));
            if (entity.getExpectedValue().compareTo(java.math.BigDecimal.ZERO) != 0) {
                entity.setDeviationPercent(entity.getDeviationValue().divide(entity.getExpectedValue(), 4, java.math.RoundingMode.HALF_UP));
            }
        }

        // Event for deviation detection
        eventPublisher.publishEvent(WorkflowTransitionEvent.builder()
                .entityId(null)
                .entityType("FLOW_DEVIATION")
                .fromState(null)
                .toState("DETECTED")
                .action("DETECT")
                .actorEmployeeId(null) // detected by calculation
                .build());
    }
}
