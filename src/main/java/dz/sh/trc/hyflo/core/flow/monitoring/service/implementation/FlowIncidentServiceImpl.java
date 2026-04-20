package dz.sh.trc.hyflo.core.flow.monitoring.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowIncident;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.core.flow.monitoring.mapper.FlowIncidentMapper;
import dz.sh.trc.hyflo.core.flow.monitoring.repository.FlowIncidentRepository;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowIncidentService;

@Service
public class FlowIncidentServiceImpl extends AbstractCrudService<CreateFlowIncidentRequest, UpdateFlowIncidentRequest, FlowIncidentResponse, FlowIncidentSummary, FlowIncident> implements FlowIncidentService {

    public FlowIncidentServiceImpl(FlowIncidentRepository repository, FlowIncidentMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<FlowIncident> getEntityClass() {
        return FlowIncident.class;
    }
}
