package dz.sh.trc.hyflo.core.network.topology.service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateProcessingPlantRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateProcessingPlantRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProcessingPlantResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProcessingPlantSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface ProcessingPlantService extends BaseService<CreateProcessingPlantRequest, UpdateProcessingPlantRequest, ProcessingPlantResponse, ProcessingPlantSummary> {
}