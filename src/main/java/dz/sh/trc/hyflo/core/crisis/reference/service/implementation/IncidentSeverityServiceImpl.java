package dz.sh.trc.hyflo.core.crisis.reference.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.crisis.reference.dto.request.CreateIncidentSeverityRequest;
import dz.sh.trc.hyflo.core.crisis.reference.dto.request.UpdateIncidentSeverityRequest;
import dz.sh.trc.hyflo.core.crisis.reference.dto.response.IncidentSeverityResponse;
import dz.sh.trc.hyflo.core.crisis.reference.dto.summary.IncidentSeveritySummary;
import dz.sh.trc.hyflo.core.crisis.reference.mapper.IncidentSeverityMapper;
import dz.sh.trc.hyflo.core.crisis.reference.model.IncidentSeverity;
import dz.sh.trc.hyflo.core.crisis.reference.repository.IncidentSeverityRepository;
import dz.sh.trc.hyflo.core.crisis.reference.service.IncidentSeverityService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class IncidentSeverityServiceImpl extends AbstractCrudService<CreateIncidentSeverityRequest, UpdateIncidentSeverityRequest, IncidentSeverityResponse, IncidentSeveritySummary, IncidentSeverity> implements IncidentSeverityService {

    public IncidentSeverityServiceImpl(IncidentSeverityRepository repository, IncidentSeverityMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<IncidentSeverity> getEntityClass() {
        return IncidentSeverity.class;
    }
}
