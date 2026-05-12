package dz.sh.trc.hyflo.core.flow.monitoring.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowThreshold;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.core.flow.monitoring.mapper.FlowThresholdMapper;
import dz.sh.trc.hyflo.core.flow.monitoring.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowThresholdService;

@Service
public class FlowThresholdServiceImpl extends AbstractCrudService<CreateFlowThresholdRequest, UpdateFlowThresholdRequest, FlowThresholdResponse, FlowThresholdSummary, FlowThreshold> implements FlowThresholdService {

    public FlowThresholdServiceImpl(FlowThresholdRepository repository, FlowThresholdMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<FlowThreshold> getEntityClass() {
        return FlowThreshold.class;
    }
}
