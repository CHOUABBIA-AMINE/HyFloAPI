package dz.sh.trc.hyflo.core.general.organization.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.general.organization.dto.request.CreateStructureRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.request.UpdateStructureRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.response.StructureResponse;
import dz.sh.trc.hyflo.core.general.organization.dto.response.StructureSummary;
import dz.sh.trc.hyflo.core.general.organization.mapper.StructureMapper;
import dz.sh.trc.hyflo.core.general.organization.model.Structure;
import dz.sh.trc.hyflo.core.general.organization.repository.StructureRepository;
import dz.sh.trc.hyflo.core.general.organization.service.StructureService;
import dz.sh.trc.hyflo.core.general.type.model.StructureType;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class StructureServiceImpl extends AbstractCrudService<CreateStructureRequest, UpdateStructureRequest, StructureResponse, StructureSummary, Structure, Long> implements StructureService {

    public StructureServiceImpl(StructureRepository repository, StructureMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Structure> getEntityClass() {
        return Structure.class;
    }

    @Override
    protected void beforeCreate(CreateStructureRequest request, Structure entity) {
        entity.setStructureType(referenceResolver.resolve(request.structureTypeId(), StructureType.class));
        if (request.parentStructureId() != null) {
            entity.setParentStructure(referenceResolver.resolve(request.parentStructureId(), Structure.class));
        }
    }

    @Override
    protected void beforeUpdate(UpdateStructureRequest request, Structure entity) {
        if (request.structureTypeId() != null) {
            entity.setStructureType(referenceResolver.resolve(request.structureTypeId(), StructureType.class));
        }
        if (request.parentStructureId() != null) {
            entity.setParentStructure(referenceResolver.resolve(request.parentStructureId(), Structure.class));
        }
    }
}
