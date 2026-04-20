package dz.sh.trc.hyflo.core.flow.monitoring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowAnomaly;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;
import java.util.List;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface FlowAnomalyMapper extends BaseMapper<CreateFlowAnomalyRequest, UpdateFlowAnomalyRequest, FlowAnomalyResponse, FlowAnomalySummary, FlowAnomaly> {
    
    @Mapping(target="reading.id", source="readingId")
    @Mapping(target="segmentFlowReading.id", source="segmentFlowReadingId")
    @Mapping(target="pipelineSegment.id", source="pipelineSegmentId")

    FlowAnomaly toEntity(CreateFlowAnomalyRequest request);

    @Mapping(target="readingId", source="reading.id")
    @Mapping(target="segmentFlowReadingId", source="segmentFlowReading.id")
    @Mapping(target="pipelineSegmentId", source="pipelineSegment.id")

    FlowAnomalyResponse toResponse(FlowAnomaly entity);

    FlowAnomalySummary toSummary(FlowAnomaly entity);
    
    List<FlowAnomalyResponse> toResponseList(List<FlowAnomaly> entities);

    @Mapping(target="reading.id", source="readingId")
    @Mapping(target="segmentFlowReading.id", source="segmentFlowReadingId")
    @Mapping(target="pipelineSegment.id", source="pipelineSegmentId")

    void updateEntityFromRequest(UpdateFlowAnomalyRequest request, @MappingTarget FlowAnomaly entity);
}
