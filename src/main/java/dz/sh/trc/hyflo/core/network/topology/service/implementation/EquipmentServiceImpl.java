package dz.sh.trc.hyflo.core.network.topology.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateEquipmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateEquipmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.EquipmentResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.EquipmentSummary;
import dz.sh.trc.hyflo.core.network.topology.mapper.EquipmentMapper;
import dz.sh.trc.hyflo.core.network.topology.model.Equipment;
import dz.sh.trc.hyflo.core.network.topology.repository.EquipmentRepository;
import dz.sh.trc.hyflo.core.network.topology.service.EquipmentService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class EquipmentServiceImpl extends AbstractCrudService<CreateEquipmentRequest, UpdateEquipmentRequest, EquipmentResponse, EquipmentSummary, Equipment, Long> implements EquipmentService {

    public EquipmentServiceImpl(EquipmentRepository repository, EquipmentMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Equipment> getEntityClass() {
        return Equipment.class;
    }
}