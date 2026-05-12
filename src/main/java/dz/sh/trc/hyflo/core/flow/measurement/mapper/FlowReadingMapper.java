package dz.sh.trc.hyflo.core.flow.measurement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowReadingResponse;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowReadingSummary;
import dz.sh.trc.hyflo.core.flow.measurement.model.FlowReading;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface FlowReadingMapper extends BaseMapper<CreateFlowReadingRequest, UpdateFlowReadingRequest, FlowReadingResponse, FlowReadingSummary, FlowReading> {

    @Override
    @Mapping(target = "pipeline", ignore = true)
    @Mapping(target = "validationStatus", ignore = true)
    @Mapping(target = "workflowInstance", ignore = true)
    @Mapping(target = "dataSource", ignore = true)
    @Mapping(target = "readingSlot", ignore = true)
    @Mapping(target = "recordedBy", ignore = true)
    @Mapping(target = "validatedBy", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "validatedAt", ignore = true)
    @Mapping(target = "recordedAt", ignore = true)
    FlowReading toEntity(CreateFlowReadingRequest dto);

    @Override
    @Mapping(target = "pipelineId", source = "pipeline.id")
    @Mapping(target = "pipelineName", source = "pipeline.name")
    @Mapping(target = "validationStatusId", source = "validationStatus.id")
    @Mapping(target = "validationStatusName", source = "validationStatus.designationFr")
    @Mapping(target = "workflowInstanceId", source = "workflowInstance.id")
    @Mapping(target = "dataSourceId", source = "dataSource.id")
    @Mapping(target = "dataSourceName", source = "dataSource.designationFr")
    @Mapping(target = "readingSlotId", source = "readingSlot.id")
    @Mapping(target = "recordedById", source = "recordedBy.id")
    @Mapping(target = "validatedById", source = "validatedBy.id")
    FlowReadingResponse toResponse(FlowReading entity);

    @Override
    @Mapping(target = "pipelineName", source = "pipeline.name")
    @Mapping(target = "validationStatusName", source = "validationStatus.designationFr")
    FlowReadingSummary toSummary(FlowReading entity);
}