package dz.sh.trc.hyflo.core.general.type.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.general.type.dto.request.CreateStructureTypeRequest;
import dz.sh.trc.hyflo.core.general.type.dto.request.UpdateStructureTypeRequest;
import dz.sh.trc.hyflo.core.general.type.dto.response.StructureTypeResponse;
import dz.sh.trc.hyflo.core.general.type.dto.response.StructureTypeSummary;
import dz.sh.trc.hyflo.core.general.type.mapper.StructureTypeMapper;
import dz.sh.trc.hyflo.core.general.type.model.StructureType;
import dz.sh.trc.hyflo.core.general.type.repository.StructureTypeRepository;
import dz.sh.trc.hyflo.core.general.type.service.StructureTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class StructureTypeServiceImpl extends AbstractCrudService<CreateStructureTypeRequest, UpdateStructureTypeRequest, StructureTypeResponse, StructureTypeSummary, StructureType> implements StructureTypeService {

    public StructureTypeServiceImpl(StructureTypeRepository repository, StructureTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<StructureType> getEntityClass() {
        return StructureType.class;
    }
}
