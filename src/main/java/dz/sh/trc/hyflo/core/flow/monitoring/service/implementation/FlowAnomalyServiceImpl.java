package dz.sh.trc.hyflo.core.flow.monitoring.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowAnomaly;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.core.flow.monitoring.mapper.FlowAnomalyMapper;
import dz.sh.trc.hyflo.core.flow.monitoring.repository.FlowAnomalyRepository;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowAnomalyService;

@Service
public class FlowAnomalyServiceImpl extends AbstractCrudService<CreateFlowAnomalyRequest, UpdateFlowAnomalyRequest, FlowAnomalyResponse, FlowAnomalySummary, FlowAnomaly> implements FlowAnomalyService {

    public FlowAnomalyServiceImpl(FlowAnomalyRepository repository, FlowAnomalyMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<FlowAnomaly> getEntityClass() {
        return FlowAnomaly.class;
    }
}
