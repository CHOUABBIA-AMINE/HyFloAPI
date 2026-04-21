package dz.sh.trc.hyflo.core.network.type.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateProcessingPlantTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateProcessingPlantTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProcessingPlantTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProcessingPlantTypeSummary;
import dz.sh.trc.hyflo.core.network.type.service.ProcessingPlantTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/network/processing-plant-types")
@Tag(name = "ProcessingPlantType API", description = "Endpoints for managing ProcessingPlantType")
public class ProcessingPlantTypeController extends BaseController<CreateProcessingPlantTypeRequest, UpdateProcessingPlantTypeRequest, ProcessingPlantTypeResponse, ProcessingPlantTypeSummary> {

    public ProcessingPlantTypeController(ProcessingPlantTypeService service) {
        super(service);
    }

    @Override
    protected Page<ProcessingPlantTypeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}