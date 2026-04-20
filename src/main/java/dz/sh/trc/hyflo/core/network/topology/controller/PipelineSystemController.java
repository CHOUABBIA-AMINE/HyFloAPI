package dz.sh.trc.hyflo.core.network.topology.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreatePipelineSystemRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdatePipelineSystemRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSystemResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSystemSummary;
import dz.sh.trc.hyflo.core.network.topology.model.PipelineSystem;
import dz.sh.trc.hyflo.core.network.topology.service.PipelineSystemService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/pipeline-systems")
@Tag(name = "PipelineSystem API", description = "Endpoints for managing PipelineSystem")
public class PipelineSystemController extends BaseController<CreatePipelineSystemRequest, UpdatePipelineSystemRequest, PipelineSystemResponse, PipelineSystemSummary> {

    public PipelineSystemController(PipelineSystemService service) {
        super(service);
    }

    @Override
    protected Page<PipelineSystemSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}