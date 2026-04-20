package dz.sh.trc.hyflo.core.flow.measurement.service.implementation;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateSegmentFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateSegmentFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.SegmentFlowReadingResponse;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.SegmentFlowReadingSummary;
import dz.sh.trc.hyflo.core.flow.measurement.mapper.SegmentFlowReadingMapper;
import dz.sh.trc.hyflo.core.flow.measurement.model.SegmentFlowReading;
import dz.sh.trc.hyflo.core.flow.measurement.repository.SegmentFlowReadingRepository;
import dz.sh.trc.hyflo.core.flow.measurement.service.SegmentFlowReadingService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;
@Service
public class SegmentFlowReadingServiceImpl extends AbstractCrudService<CreateSegmentFlowReadingRequest, UpdateSegmentFlowReadingRequest, SegmentFlowReadingResponse, SegmentFlowReadingSummary, SegmentFlowReading> implements SegmentFlowReadingService {
    public SegmentFlowReadingServiceImpl(SegmentFlowReadingRepository repository, SegmentFlowReadingMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }
    @Override
    protected Class<SegmentFlowReading> getEntityClass() { return SegmentFlowReading.class; }

    @Override
    protected void beforeCreate(dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateSegmentFlowReadingRequest request, SegmentFlowReading entity) {
        entity.setPipelineSegment(referenceResolver.resolve(request.pipelineSegmentId(), dz.sh.trc.hyflo.core.network.topology.model.PipelineSegment.class));
        if(request.sourceReadingId() != null) entity.setSourceReading(referenceResolver.resolve(request.sourceReadingId(), dz.sh.trc.hyflo.core.flow.measurement.model.FlowReading.class));
        entity.setCalculatedAt(java.time.LocalDateTime.now());
    }
}