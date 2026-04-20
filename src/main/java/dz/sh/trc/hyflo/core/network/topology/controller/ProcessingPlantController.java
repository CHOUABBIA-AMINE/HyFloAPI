package dz.sh.trc.hyflo.core.network.topology.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateProcessingPlantRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateProcessingPlantRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProcessingPlantResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProcessingPlantSummary;
import dz.sh.trc.hyflo.core.network.topology.service.ProcessingPlantService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/processing-plants")
@Tag(name = "ProcessingPlant API", description = "Endpoints for managing ProcessingPlant")
public class ProcessingPlantController extends BaseController<CreateProcessingPlantRequest, UpdateProcessingPlantRequest, ProcessingPlantResponse, ProcessingPlantSummary> {

    public ProcessingPlantController(ProcessingPlantService service) {
        super(service);
    }

    @Override
    protected Page<ProcessingPlantSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}