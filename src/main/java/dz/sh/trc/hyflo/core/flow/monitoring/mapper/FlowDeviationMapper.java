package dz.sh.trc.hyflo.core.flow.monitoring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowDeviation;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;
import java.util.List;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface FlowDeviationMapper extends BaseMapper<CreateFlowDeviationRequest, UpdateFlowDeviationRequest, FlowDeviationResponse, FlowDeviationSummary, FlowDeviation> {
    
    @Mapping(target="pipeline.id", source="pipelineId")
    @Mapping(target="reading.id", source="readingId")
    @Mapping(target="plan.id", source="planId")

    FlowDeviation toEntity(CreateFlowDeviationRequest request);

    @Mapping(target="pipelineId", source="pipeline.id")
    @Mapping(target="readingId", source="reading.id")
    @Mapping(target="planId", source="plan.id")

    FlowDeviationResponse toResponse(FlowDeviation entity);

    FlowDeviationSummary toSummary(FlowDeviation entity);
    
    List<FlowDeviationResponse> toResponseList(List<FlowDeviation> entities);

    @Mapping(target="pipeline.id", source="pipelineId")
    @Mapping(target="reading.id", source="readingId")
    @Mapping(target="plan.id", source="planId")

    void updateEntityFromRequest(UpdateFlowDeviationRequest request, @MappingTarget FlowDeviation entity);
}
