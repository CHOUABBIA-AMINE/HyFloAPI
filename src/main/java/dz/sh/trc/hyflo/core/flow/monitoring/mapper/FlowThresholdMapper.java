package dz.sh.trc.hyflo.core.flow.monitoring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowThreshold;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;
import java.util.List;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface FlowThresholdMapper extends BaseMapper<CreateFlowThresholdRequest, UpdateFlowThresholdRequest, FlowThresholdResponse, FlowThresholdSummary, FlowThreshold> {
    
    @Mapping(target="pipeline.id", source="pipelineId")

    FlowThreshold toEntity(CreateFlowThresholdRequest request);

    @Mapping(target="pipelineId", source="pipeline.id")
    @Mapping(target="pipelineName", source="pipeline.name")

    FlowThresholdResponse toResponse(FlowThreshold entity);

    FlowThresholdSummary toSummary(FlowThreshold entity);
    
    List<FlowThresholdResponse> toResponseList(List<FlowThreshold> entities);

    @Mapping(target="pipeline.id", source="pipelineId")

    void updateEntityFromRequest(UpdateFlowThresholdRequest request, @MappingTarget FlowThreshold entity);
}
