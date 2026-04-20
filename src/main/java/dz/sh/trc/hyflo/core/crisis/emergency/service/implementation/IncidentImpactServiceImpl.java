package dz.sh.trc.hyflo.core.crisis.emergency.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.CreateIncidentImpactRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.UpdateIncidentImpactRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.response.IncidentImpactResponse;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.summary.IncidentImpactSummary;
import dz.sh.trc.hyflo.core.crisis.emergency.mapper.IncidentImpactMapper;
import dz.sh.trc.hyflo.core.crisis.emergency.model.IncidentImpact;
import dz.sh.trc.hyflo.core.crisis.emergency.repository.IncidentImpactRepository;
import dz.sh.trc.hyflo.core.crisis.emergency.service.IncidentImpactService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class IncidentImpactServiceImpl extends AbstractCrudService<CreateIncidentImpactRequest, UpdateIncidentImpactRequest, IncidentImpactResponse, IncidentImpactSummary, IncidentImpact> implements IncidentImpactService {

    public IncidentImpactServiceImpl(IncidentImpactRepository repository, IncidentImpactMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<IncidentImpact> getEntityClass() {
        return IncidentImpact.class;
    }
}
