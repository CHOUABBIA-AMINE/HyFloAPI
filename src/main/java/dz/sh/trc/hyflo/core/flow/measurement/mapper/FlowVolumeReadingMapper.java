package dz.sh.trc.hyflo.core.flow.measurement.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateFlowVolumeReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateFlowVolumeReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowVolumeReadingResponse;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowVolumeReadingSummary;
import dz.sh.trc.hyflo.core.flow.measurement.model.FlowVolumeReading;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;
@Mapper(componentModel = "spring")
public interface FlowVolumeReadingMapper extends BaseMapper<CreateFlowVolumeReadingRequest, UpdateFlowVolumeReadingRequest, FlowVolumeReadingResponse, FlowVolumeReadingSummary, FlowVolumeReading> {
    @Override
    @Mapping(target = "infrastructureId", source = "infrastructure.id")
    @Mapping(target = "infrastructureName", source = "infrastructure.name")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.designationFr")
    @Mapping(target = "typeId", source = "type.id")
    @Mapping(target = "typeName", source = "type.designationFr")
    @Mapping(target = "recordedById", source = "recordedBy.id")
    @Mapping(target = "validatedById", source = "validatedBy.id")
    @Mapping(target = "validationStatusId", source = "validationStatus.id")
    @Mapping(target = "validationStatusName", source = "validationStatus.designationFr")
    @Mapping(target = "workflowInstanceId", source = "workflowInstance.id")
    FlowVolumeReadingResponse toResponse(FlowVolumeReading entity);
    
    @Override
    @Mapping(target = "infrastructureName", source = "infrastructure.name")
    @Mapping(target = "productName", source = "product.designationFr")
    @Mapping(target = "typeName", source = "type.designationFr")
    @Mapping(target = "validationStatusName", source = "validationStatus.designationFr")
    FlowVolumeReadingSummary toSummary(FlowVolumeReading entity);
}