package dz.sh.trc.hyflo.core.network.topology.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateProcessingPlantRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateProcessingPlantRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProcessingPlantResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProcessingPlantSummary;
import dz.sh.trc.hyflo.core.network.topology.model.ProcessingPlant;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface ProcessingPlantMapper extends BaseMapper<CreateProcessingPlantRequest, UpdateProcessingPlantRequest, ProcessingPlantResponse, ProcessingPlantSummary, ProcessingPlant> {
}