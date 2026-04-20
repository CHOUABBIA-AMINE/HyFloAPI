package dz.sh.trc.hyflo.core.flow.monitoring.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowDeviation;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.core.flow.monitoring.mapper.FlowDeviationMapper;
import dz.sh.trc.hyflo.core.flow.monitoring.repository.FlowDeviationRepository;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowDeviationService;

@Service
public class FlowDeviationServiceImpl extends AbstractCrudService<CreateFlowDeviationRequest, UpdateFlowDeviationRequest, FlowDeviationResponse, FlowDeviationSummary, FlowDeviation> implements FlowDeviationService {

    public FlowDeviationServiceImpl(FlowDeviationRepository repository, FlowDeviationMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<FlowDeviation> getEntityClass() {
        return FlowDeviation.class;
    }
}
