package dz.sh.trc.hyflo.core.flow.reference.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreatePlanStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdatePlanStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.PlanStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.PlanStatusSummary;
import dz.sh.trc.hyflo.core.flow.reference.mapper.PlanStatusMapper;
import dz.sh.trc.hyflo.core.flow.reference.model.PlanStatus;
import dz.sh.trc.hyflo.core.flow.reference.repository.PlanStatusRepository;
import dz.sh.trc.hyflo.core.flow.reference.service.PlanStatusService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class PlanStatusServiceImpl extends AbstractCrudService<CreatePlanStatusRequest, UpdatePlanStatusRequest, PlanStatusResponse, PlanStatusSummary, PlanStatus, Long> implements PlanStatusService {

    public PlanStatusServiceImpl(PlanStatusRepository repository, PlanStatusMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<PlanStatus> getEntityClass() {
        return PlanStatus.class;
    }
}