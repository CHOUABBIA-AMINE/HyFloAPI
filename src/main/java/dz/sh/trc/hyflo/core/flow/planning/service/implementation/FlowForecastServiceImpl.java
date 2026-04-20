package dz.sh.trc.hyflo.core.flow.planning.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;
import dz.sh.trc.hyflo.core.flow.planning.model.FlowForecast;
import dz.sh.trc.hyflo.core.flow.planning.dto.*;
import dz.sh.trc.hyflo.core.flow.planning.mapper.FlowForecastMapper;
import dz.sh.trc.hyflo.core.flow.planning.repository.FlowForecastRepository;
import dz.sh.trc.hyflo.core.flow.planning.service.FlowForecastService;

@Service
public class FlowForecastServiceImpl extends AbstractCrudService<CreateFlowForecastRequest, UpdateFlowForecastRequest, FlowForecastResponse, FlowForecastSummary, FlowForecast> implements FlowForecastService {

    public FlowForecastServiceImpl(FlowForecastRepository repository, FlowForecastMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<FlowForecast> getEntityClass() {
        return FlowForecast.class;
    }
}
