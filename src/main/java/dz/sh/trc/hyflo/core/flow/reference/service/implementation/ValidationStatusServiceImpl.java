package dz.sh.trc.hyflo.core.flow.reference.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateValidationStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateValidationStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ValidationStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ValidationStatusSummary;
import dz.sh.trc.hyflo.core.flow.reference.mapper.ValidationStatusMapper;
import dz.sh.trc.hyflo.core.flow.reference.model.ValidationStatus;
import dz.sh.trc.hyflo.core.flow.reference.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.core.flow.reference.service.ValidationStatusService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class ValidationStatusServiceImpl extends AbstractCrudService<CreateValidationStatusRequest, UpdateValidationStatusRequest, ValidationStatusResponse, ValidationStatusSummary, ValidationStatus> implements ValidationStatusService {

    public ValidationStatusServiceImpl(ValidationStatusRepository repository, ValidationStatusMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<ValidationStatus> getEntityClass() {
        return ValidationStatus.class;
    }
}