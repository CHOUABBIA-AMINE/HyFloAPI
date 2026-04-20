package dz.sh.trc.hyflo.core.crisis.emergency.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.CreateIncidentRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.UpdateIncidentRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.response.IncidentResponse;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.summary.IncidentSummary;
import dz.sh.trc.hyflo.core.crisis.emergency.mapper.IncidentMapper;
import dz.sh.trc.hyflo.core.crisis.emergency.model.Incident;
import dz.sh.trc.hyflo.core.crisis.emergency.repository.IncidentRepository;
import dz.sh.trc.hyflo.core.crisis.emergency.service.IncidentService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class IncidentServiceImpl extends AbstractCrudService<CreateIncidentRequest, UpdateIncidentRequest, IncidentResponse, IncidentSummary, Incident> implements IncidentService {

    public IncidentServiceImpl(IncidentRepository repository, IncidentMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Incident> getEntityClass() {
        return Incident.class;
    }
}
