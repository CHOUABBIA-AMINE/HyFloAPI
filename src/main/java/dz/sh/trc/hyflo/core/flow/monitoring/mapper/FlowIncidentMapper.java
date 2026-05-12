package dz.sh.trc.hyflo.core.flow.monitoring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowIncident;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;
import java.util.List;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface FlowIncidentMapper extends BaseMapper<CreateFlowIncidentRequest, UpdateFlowIncidentRequest, FlowIncidentResponse, FlowIncidentSummary, FlowIncident> {
    
    @Mapping(target="severity.id", source="severityId")
    @Mapping(target="infrastructure.id", source="infrastructureId")
    @Mapping(target="reportedBy.id", source="reportedById")
    @Mapping(target="relatedReading.id", source="relatedReadingId")
    @Mapping(target="relatedAlert.id", source="relatedAlertId")
    @Mapping(target="status.id", source="statusId")

    FlowIncident toEntity(CreateFlowIncidentRequest request);

    @Mapping(target="severityId", source="severity.id")
    @Mapping(target="infrastructureId", source="infrastructure.id")
    @Mapping(target="reportedById", source="reportedBy.id")
    @Mapping(target="relatedReadingId", source="relatedReading.id")
    @Mapping(target="relatedAlertId", source="relatedAlert.id")
    @Mapping(target="statusId", source="status.id")
    @Mapping(target="infrastructureCode", source="infrastructure.code")

    FlowIncidentResponse toResponse(FlowIncident entity);

    FlowIncidentSummary toSummary(FlowIncident entity);
    
    List<FlowIncidentResponse> toResponseList(List<FlowIncident> entities);

    @Mapping(target="severity.id", source="severityId")
    @Mapping(target="infrastructure.id", source="infrastructureId")
    @Mapping(target="reportedBy.id", source="reportedById")
    @Mapping(target="relatedReading.id", source="relatedReadingId")
    @Mapping(target="relatedAlert.id", source="relatedAlertId")
    @Mapping(target="status.id", source="statusId")

    void updateEntityFromRequest(UpdateFlowIncidentRequest request, @MappingTarget FlowIncident entity);
}
