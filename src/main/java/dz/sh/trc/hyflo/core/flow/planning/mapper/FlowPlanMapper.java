package dz.sh.trc.hyflo.core.flow.planning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import dz.sh.trc.hyflo.core.flow.planning.model.FlowPlan;
import dz.sh.trc.hyflo.core.flow.planning.dto.*;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;
import java.util.List;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface FlowPlanMapper extends BaseMapper<CreateFlowPlanRequest, UpdateFlowPlanRequest, FlowPlanResponse, FlowPlanSummary, FlowPlan> {
    
    @Mapping(target="pipeline", ignore=true)
    @Mapping(target="status", ignore=true)
    @Mapping(target="approvedBy", ignore=true)
    @Mapping(target="submittedBy", ignore=true)
    @Mapping(target="revisedFrom", ignore=true)
    FlowPlan toEntity(CreateFlowPlanRequest request);

    @Mapping(target="pipelineId", source="pipeline.id")
    @Mapping(target="statusId", source="status.id")
    @Mapping(target="approvedById", source="approvedBy.id")
    @Mapping(target="submittedById", source="submittedBy.id")
    @Mapping(target="revisedFromId", source="revisedFrom.id")
    @Mapping(target="pipelineName", source="pipeline.name")
    @Mapping(target="statusName", source="status.designationFr")

    FlowPlanResponse toResponse(FlowPlan entity);

    @Mapping(target="pipelineName", source="pipeline.name")
    @Mapping(target="statusName", source="status.designationFr")
    FlowPlanSummary toSummary(FlowPlan entity);
    
    List<FlowPlanResponse> toResponseList(List<FlowPlan> entities);

    @Mapping(target="pipeline", ignore=true)
    @Mapping(target="status", ignore=true)
    @Mapping(target="approvedBy", ignore=true)
    @Mapping(target="submittedBy", ignore=true)
    @Mapping(target="revisedFrom", ignore=true)
    void updateEntityFromRequest(UpdateFlowPlanRequest request, @MappingTarget FlowPlan entity);
}
