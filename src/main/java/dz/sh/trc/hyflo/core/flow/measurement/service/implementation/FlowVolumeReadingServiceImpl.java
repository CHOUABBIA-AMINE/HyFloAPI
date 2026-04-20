package dz.sh.trc.hyflo.core.flow.measurement.service.implementation;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateFlowVolumeReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateFlowVolumeReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowVolumeReadingResponse;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowVolumeReadingSummary;
import dz.sh.trc.hyflo.core.flow.measurement.mapper.FlowVolumeReadingMapper;
import dz.sh.trc.hyflo.core.flow.measurement.model.FlowVolumeReading;
import dz.sh.trc.hyflo.core.flow.measurement.repository.FlowVolumeReadingRepository;
import dz.sh.trc.hyflo.core.flow.measurement.service.FlowVolumeReadingService;
import dz.sh.trc.hyflo.core.flow.type.model.OperationType;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.core.network.common.model.Product;
import dz.sh.trc.hyflo.core.network.topology.model.Infrastructure;
import dz.sh.trc.hyflo.platform.event.WorkflowTransitionEvent;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class FlowVolumeReadingServiceImpl extends AbstractCrudService<CreateFlowVolumeReadingRequest, UpdateFlowVolumeReadingRequest, FlowVolumeReadingResponse, FlowVolumeReadingSummary, FlowVolumeReading> implements FlowVolumeReadingService {

    public FlowVolumeReadingServiceImpl(FlowVolumeReadingRepository repository, 
                                        FlowVolumeReadingMapper mapper, 
                                        ReferenceResolver referenceResolver, 
                                        ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<FlowVolumeReading> getEntityClass() {
        return FlowVolumeReading.class;
    }

    @Override
    protected void beforeCreate(CreateFlowVolumeReadingRequest request, FlowVolumeReading entity) {
        entity.setInfrastructure(referenceResolver.resolve(request.infrastructureId(), Infrastructure.class));
        entity.setProduct(referenceResolver.resolve(request.productId(), Product.class));
        entity.setType(referenceResolver.resolve(request.typeId(), OperationType.class));
        entity.setRecordedBy(referenceResolver.resolve(request.recordedById(), Employee.class));
        
        if (entity.getRecordedAt() == null) {
            entity.setRecordedAt(LocalDateTime.now());
        }

        // Event for volume reading creation
        eventPublisher.publishEvent(WorkflowTransitionEvent.builder()
                .entityId(null) // ID not yet generated
                .entityType("FLOW_VOLUME_READING")
                .fromState(null)
                .toState("RECORDED")
                .action("CREATE")
                .actorEmployeeId(request.recordedById())
                .build());
    }

    @Override
    protected void beforeUpdate(UpdateFlowVolumeReadingRequest request, FlowVolumeReading entity) {
        if (request.typeId() != null) {
            entity.setType(referenceResolver.resolve(request.typeId(), OperationType.class));
        }
    }
}