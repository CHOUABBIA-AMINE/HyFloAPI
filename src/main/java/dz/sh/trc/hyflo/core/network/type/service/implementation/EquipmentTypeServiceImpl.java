package dz.sh.trc.hyflo.core.network.type.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateEquipmentTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateEquipmentTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.EquipmentTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.EquipmentTypeSummary;
import dz.sh.trc.hyflo.core.network.type.mapper.EquipmentTypeMapper;
import dz.sh.trc.hyflo.core.network.type.model.EquipmentType;
import dz.sh.trc.hyflo.core.network.type.repository.EquipmentTypeRepository;
import dz.sh.trc.hyflo.core.network.type.service.EquipmentTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class EquipmentTypeServiceImpl extends AbstractCrudService<CreateEquipmentTypeRequest, UpdateEquipmentTypeRequest, EquipmentTypeResponse, EquipmentTypeSummary, EquipmentType> implements EquipmentTypeService {

    public EquipmentTypeServiceImpl(EquipmentTypeRepository repository, EquipmentTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<EquipmentType> getEntityClass() {
        return EquipmentType.class;
    }
}