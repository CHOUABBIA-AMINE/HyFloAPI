package dz.sh.trc.hyflo.core.flow.type.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.type.dto.request.CreateOperationTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.request.UpdateOperationTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.response.OperationTypeResponse;
import dz.sh.trc.hyflo.core.flow.type.dto.response.OperationTypeSummary;
import dz.sh.trc.hyflo.core.flow.type.mapper.OperationTypeMapper;
import dz.sh.trc.hyflo.core.flow.type.model.OperationType;
import dz.sh.trc.hyflo.core.flow.type.repository.OperationTypeRepository;
import dz.sh.trc.hyflo.core.flow.type.service.OperationTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class OperationTypeServiceImpl extends AbstractCrudService<CreateOperationTypeRequest, UpdateOperationTypeRequest, OperationTypeResponse, OperationTypeSummary, OperationType, Long> implements OperationTypeService {

    public OperationTypeServiceImpl(OperationTypeRepository repository, OperationTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<OperationType> getEntityClass() {
        return OperationType.class;
    }
}