package dz.sh.trc.hyflo.core.flow.planning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import dz.sh.trc.hyflo.core.flow.planning.model.FlowForecast;
import dz.sh.trc.hyflo.core.flow.planning.dto.*;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;
import java.util.List;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface FlowForecastMapper extends BaseMapper<CreateFlowForecastRequest, UpdateFlowForecastRequest, FlowForecastResponse, FlowForecastSummary, FlowForecast> {
    
    @Mapping(target="infrastructure.id", source="infrastructureId")
    @Mapping(target="product.id", source="productId")
    @Mapping(target="operationType.id", source="operationTypeId")
    @Mapping(target="supervisor.id", source="supervisorId")

    FlowForecast toEntity(CreateFlowForecastRequest request);

    @Mapping(target="infrastructureId", source="infrastructure.id")
    @Mapping(target="productId", source="product.id")
    @Mapping(target="operationTypeId", source="operationType.id")
    @Mapping(target="supervisorId", source="supervisor.id")
    @Mapping(target="infrastructureCode", source="infrastructure.code")
    @Mapping(target="productName", source="product.designationFr")
    @Mapping(target="operationTypeName", source="operationType.designationFr")

    FlowForecastResponse toResponse(FlowForecast entity);

    FlowForecastSummary toSummary(FlowForecast entity);
    
    List<FlowForecastResponse> toResponseList(List<FlowForecast> entities);

    @Mapping(target="infrastructure.id", source="infrastructureId")
    @Mapping(target="product.id", source="productId")
    @Mapping(target="operationType.id", source="operationTypeId")
    @Mapping(target="supervisor.id", source="supervisorId")

    void updateEntityFromRequest(UpdateFlowForecastRequest request, @MappingTarget FlowForecast entity);
}
