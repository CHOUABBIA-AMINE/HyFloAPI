package dz.sh.trc.hyflo.core.flow.monitoring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowAlert;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;
import java.util.List;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface FlowAlertMapper extends BaseMapper<CreateFlowAlertRequest, UpdateFlowAlertRequest, FlowAlertResponse, FlowAlertSummary, FlowAlert> {
    
    @Mapping(target="threshold", ignore=true)
    @Mapping(target="flowReading", ignore=true)
    @Mapping(target="status", ignore=true)
    @Mapping(target="acknowledgedBy", ignore=true)
    @Mapping(target="resolvedBy", ignore=true)
    FlowAlert toEntity(CreateFlowAlertRequest request);

    @Mapping(target="thresholdId", source="threshold.id")
    @Mapping(target="flowReadingId", source="flowReading.id")
    @Mapping(target="statusId", source="status.id")
    @Mapping(target="statusName", source="status.designationFr")
    @Mapping(target="acknowledgedById", source="acknowledgedBy.id")
    @Mapping(target="resolvedById", source="resolvedBy.id")
    FlowAlertResponse toResponse(FlowAlert entity);

    @Mapping(target="statusName", source="status.designationFr")
    FlowAlertSummary toSummary(FlowAlert entity);
    
    List<FlowAlertResponse> toResponseList(List<FlowAlert> entities);

    void updateEntityFromRequest(UpdateFlowAlertRequest request, @MappingTarget FlowAlert entity);
}
