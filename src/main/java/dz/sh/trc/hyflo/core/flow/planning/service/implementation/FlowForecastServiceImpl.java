package dz.sh.trc.hyflo.core.flow.planning.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.planning.dto.CreateFlowForecastRequest;
import dz.sh.trc.hyflo.core.flow.planning.dto.FlowForecastResponse;
import dz.sh.trc.hyflo.core.flow.planning.dto.FlowForecastSummary;
import dz.sh.trc.hyflo.core.flow.planning.dto.UpdateFlowForecastRequest;
import dz.sh.trc.hyflo.core.flow.planning.mapper.FlowForecastMapper;
import dz.sh.trc.hyflo.core.flow.planning.model.FlowForecast;
import dz.sh.trc.hyflo.core.flow.planning.repository.FlowForecastRepository;
import dz.sh.trc.hyflo.core.flow.planning.service.FlowForecastService;
import dz.sh.trc.hyflo.core.flow.type.model.OperationType;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.core.network.common.model.Product;
import dz.sh.trc.hyflo.core.network.topology.model.Infrastructure;
import dz.sh.trc.hyflo.platform.event.WorkflowTransitionEvent;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class FlowForecastServiceImpl extends AbstractCrudService<CreateFlowForecastRequest, UpdateFlowForecastRequest, FlowForecastResponse, FlowForecastSummary, FlowForecast> implements FlowForecastService {

    public FlowForecastServiceImpl(FlowForecastRepository repository, 
                                   FlowForecastMapper mapper, 
                                   ReferenceResolver referenceResolver, 
                                   ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<FlowForecast> getEntityClass() {
        return FlowForecast.class;
    }

    @Override
    protected void beforeCreate(CreateFlowForecastRequest request, FlowForecast entity) {
        entity.setInfrastructure(referenceResolver.resolve(request.getInfrastructureId(), Infrastructure.class));
        entity.setProduct(referenceResolver.resolve(request.getProductId(), Product.class));
        entity.setOperationType(referenceResolver.resolve(request.getOperationTypeId(), OperationType.class));
        
        if (request.getSupervisorId() != null) {
            entity.setSupervisor(referenceResolver.resolve(request.getSupervisorId(), Employee.class));
        }

        // Logic for accuracy calculation if actual is present
        if (entity.getActualVolume() != null && entity.getPredictedVolume() != null && entity.getPredictedVolume().compareTo(java.math.BigDecimal.ZERO) != 0) {
            java.math.BigDecimal diff = entity.getActualVolume().subtract(entity.getPredictedVolume()).abs();
            java.math.BigDecimal accuracyValue = java.math.BigDecimal.valueOf(100).subtract(
                diff.divide(entity.getPredictedVolume(), 4, java.math.RoundingMode.HALF_UP).multiply(java.math.BigDecimal.valueOf(100))
            );
            entity.setAccuracy(accuracyValue);
        }

        // Event for forecast creation
        eventPublisher.publishEvent(WorkflowTransitionEvent.builder()
                .entityId(null)
                .entityType("FLOW_FORECAST")
                .fromState(null)
                .toState("CREATED")
                .action("CREATE")
                .actorEmployeeId(null)
                .build());
    }
}
