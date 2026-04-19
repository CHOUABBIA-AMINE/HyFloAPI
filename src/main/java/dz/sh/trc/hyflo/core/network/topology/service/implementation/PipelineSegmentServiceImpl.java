package dz.sh.trc.hyflo.core.network.topology.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreatePipelineSegmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdatePipelineSegmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSegmentResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSegmentSummary;
import dz.sh.trc.hyflo.core.network.topology.mapper.PipelineSegmentMapper;
import dz.sh.trc.hyflo.core.network.topology.model.PipelineSegment;
import dz.sh.trc.hyflo.core.network.topology.repository.PipelineSegmentRepository;
import dz.sh.trc.hyflo.core.network.topology.service.PipelineSegmentService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class PipelineSegmentServiceImpl extends AbstractCrudService<CreatePipelineSegmentRequest, UpdatePipelineSegmentRequest, PipelineSegmentResponse, PipelineSegmentSummary, PipelineSegment, Long> implements PipelineSegmentService {

    public PipelineSegmentServiceImpl(PipelineSegmentRepository repository, PipelineSegmentMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<PipelineSegment> getEntityClass() {
        return PipelineSegment.class;
    }
}