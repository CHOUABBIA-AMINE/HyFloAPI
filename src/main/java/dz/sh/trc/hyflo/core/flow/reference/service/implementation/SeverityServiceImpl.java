package dz.sh.trc.hyflo.core.flow.reference.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateSeverityRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateSeverityRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.SeverityResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.SeveritySummary;
import dz.sh.trc.hyflo.core.flow.reference.mapper.SeverityMapper;
import dz.sh.trc.hyflo.core.flow.reference.model.Severity;
import dz.sh.trc.hyflo.core.flow.reference.repository.SeverityRepository;
import dz.sh.trc.hyflo.core.flow.reference.service.SeverityService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class SeverityServiceImpl extends AbstractCrudService<CreateSeverityRequest, UpdateSeverityRequest, SeverityResponse, SeveritySummary, Severity, Long> implements SeverityService {

    public SeverityServiceImpl(SeverityRepository repository, SeverityMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Severity> getEntityClass() {
        return Severity.class;
    }
}