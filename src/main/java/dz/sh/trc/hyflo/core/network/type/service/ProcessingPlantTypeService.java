package dz.sh.trc.hyflo.core.network.type.service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateProcessingPlantTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateProcessingPlantTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProcessingPlantTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProcessingPlantTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface ProcessingPlantTypeService extends BaseService<CreateProcessingPlantTypeRequest, UpdateProcessingPlantTypeRequest, ProcessingPlantTypeResponse, ProcessingPlantTypeSummary> {
}