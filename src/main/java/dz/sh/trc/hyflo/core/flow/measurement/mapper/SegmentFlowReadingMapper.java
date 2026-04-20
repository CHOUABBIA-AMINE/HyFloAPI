package dz.sh.trc.hyflo.core.flow.measurement.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateSegmentFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateSegmentFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.SegmentFlowReadingResponse;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.SegmentFlowReadingSummary;
import dz.sh.trc.hyflo.core.flow.measurement.model.SegmentFlowReading;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;
@Mapper(componentModel = "spring")
public interface SegmentFlowReadingMapper extends BaseMapper<CreateSegmentFlowReadingRequest, UpdateSegmentFlowReadingRequest, SegmentFlowReadingResponse, SegmentFlowReadingSummary, SegmentFlowReading> {
    @Override
    @Mapping(target = "pipelineSegmentId", source = "pipelineSegment.id")
    @Mapping(target = "pipelineSegmentName", source = "pipelineSegment.name")
    @Mapping(target = "sourceReadingId", source = "sourceReading.id")
    @Mapping(target = "validationStatusId", source = "validationStatus.id")
    @Mapping(target = "validationStatusName", source = "validationStatus.designationFr")
    SegmentFlowReadingResponse toResponse(SegmentFlowReading entity);
    
    @Override
    @Mapping(target = "pipelineSegmentName", source = "pipelineSegment.name")
    @Mapping(target = "validationStatusName", source = "validationStatus.designationFr")
    SegmentFlowReadingSummary toSummary(SegmentFlowReading entity);
}