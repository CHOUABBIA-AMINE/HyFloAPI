package dz.sh.trc.hyflo.core.flow.measurement.service.implementation;
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
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;
@Service
public class FlowVolumeReadingServiceImpl extends AbstractCrudService<CreateFlowVolumeReadingRequest, UpdateFlowVolumeReadingRequest, FlowVolumeReadingResponse, FlowVolumeReadingSummary, FlowVolumeReading> implements FlowVolumeReadingService {
    public FlowVolumeReadingServiceImpl(FlowVolumeReadingRepository repository, FlowVolumeReadingMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }
    @Override
    protected Class<FlowVolumeReading> getEntityClass() { return FlowVolumeReading.class; }

    @Override
    protected void beforeCreate(dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateFlowVolumeReadingRequest request, FlowVolumeReading entity) {
        entity.setInfrastructure(referenceResolver.resolve(request.infrastructureId(), dz.sh.trc.hyflo.core.network.topology.model.Infrastructure.class));
        entity.setProduct(referenceResolver.resolve(request.productId(), dz.sh.trc.hyflo.core.network.common.model.Product.class));
        entity.setType(referenceResolver.resolve(request.typeId(), dz.sh.trc.hyflo.core.flow.type.model.OperationType.class));
        entity.setRecordedBy(referenceResolver.resolve(request.recordedById(), dz.sh.trc.hyflo.core.general.organization.model.Employee.class));
        entity.setRecordedAt(java.time.LocalDateTime.now());
    }

    @Override
    protected void beforeUpdate(dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateFlowVolumeReadingRequest request, FlowVolumeReading entity) {
        if(request.typeId() != null) entity.setType(referenceResolver.resolve(request.typeId(), dz.sh.trc.hyflo.core.flow.type.model.OperationType.class));
    }
}