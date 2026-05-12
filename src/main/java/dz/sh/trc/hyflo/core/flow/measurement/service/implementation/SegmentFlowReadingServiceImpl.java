package dz.sh.trc.hyflo.core.flow.measurement.service.implementation;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateSegmentFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateSegmentFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.SegmentFlowReadingResponse;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.SegmentFlowReadingSummary;
import dz.sh.trc.hyflo.core.flow.measurement.mapper.SegmentFlowReadingMapper;
import dz.sh.trc.hyflo.core.flow.measurement.model.FlowReading;
import dz.sh.trc.hyflo.core.flow.measurement.model.SegmentFlowReading;
import dz.sh.trc.hyflo.core.flow.measurement.repository.SegmentFlowReadingRepository;
import dz.sh.trc.hyflo.core.flow.measurement.service.SegmentFlowReadingService;
import dz.sh.trc.hyflo.core.network.topology.model.PipelineSegment;
import dz.sh.trc.hyflo.platform.event.WorkflowTransitionEvent;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class SegmentFlowReadingServiceImpl extends AbstractCrudService<CreateSegmentFlowReadingRequest, UpdateSegmentFlowReadingRequest, SegmentFlowReadingResponse, SegmentFlowReadingSummary, SegmentFlowReading> implements SegmentFlowReadingService {

    public SegmentFlowReadingServiceImpl(SegmentFlowReadingRepository repository, 
                                         SegmentFlowReadingMapper mapper, 
                                         ReferenceResolver referenceResolver, 
                                         ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<SegmentFlowReading> getEntityClass() {
        return SegmentFlowReading.class;
    }

    @Override
    protected void beforeCreate(CreateSegmentFlowReadingRequest request, SegmentFlowReading entity) {
        entity.setPipelineSegment(referenceResolver.resolve(request.pipelineSegmentId(), PipelineSegment.class));
        
        if (request.sourceReadingId() != null) {
            entity.setSourceReading(referenceResolver.resolve(request.sourceReadingId(), FlowReading.class));
        }
        
        if (entity.getCalculatedAt() == null) {
            entity.setCalculatedAt(LocalDateTime.now());
        }

        // Event for segment reading calculation
        eventPublisher.publishEvent(WorkflowTransitionEvent.builder()
                .entityId(null)
                .entityType("SEGMENT_FLOW_READING")
                .fromState(null)
                .toState("CALCULATED")
                .action("CALCULATE")
                .actorEmployeeId(null) // calculated by system
                .build());
    }
}