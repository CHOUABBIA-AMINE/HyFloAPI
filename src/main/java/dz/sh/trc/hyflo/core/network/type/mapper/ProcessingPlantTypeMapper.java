package dz.sh.trc.hyflo.core.network.type.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.type.dto.request.CreateProcessingPlantTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateProcessingPlantTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProcessingPlantTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProcessingPlantTypeSummary;
import dz.sh.trc.hyflo.core.network.type.model.ProcessingPlantType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface ProcessingPlantTypeMapper extends BaseMapper<CreateProcessingPlantTypeRequest, UpdateProcessingPlantTypeRequest, ProcessingPlantTypeResponse, ProcessingPlantTypeSummary, ProcessingPlantType> {
}