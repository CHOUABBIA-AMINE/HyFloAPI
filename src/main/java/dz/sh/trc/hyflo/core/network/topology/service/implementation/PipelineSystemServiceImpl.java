package dz.sh.trc.hyflo.core.network.topology.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreatePipelineSystemRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdatePipelineSystemRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSystemResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSystemSummary;
import dz.sh.trc.hyflo.core.network.topology.mapper.PipelineSystemMapper;
import dz.sh.trc.hyflo.core.network.topology.model.PipelineSystem;
import dz.sh.trc.hyflo.core.network.topology.repository.PipelineSystemRepository;
import dz.sh.trc.hyflo.core.network.topology.service.PipelineSystemService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class PipelineSystemServiceImpl extends AbstractCrudService<CreatePipelineSystemRequest, UpdatePipelineSystemRequest, PipelineSystemResponse, PipelineSystemSummary, PipelineSystem> implements PipelineSystemService {

    public PipelineSystemServiceImpl(PipelineSystemRepository repository, PipelineSystemMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<PipelineSystem> getEntityClass() {
        return PipelineSystem.class;
    }
}