package dz.sh.trc.hyflo.core.network.topology.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreatePipelineRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdatePipelineRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSummary;
import dz.sh.trc.hyflo.core.network.topology.model.Pipeline;
import dz.sh.trc.hyflo.core.network.topology.service.PipelineService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/pipelines")
@Tag(name = "Pipeline API", description = "Endpoints for managing pipelines")
public class PipelineController extends BaseController<CreatePipelineRequest, UpdatePipelineRequest, PipelineResponse, PipelineSummary> {

    public PipelineController(PipelineService service) {
        super(service);
    }

    @Override
    protected Page<PipelineSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}
