package dz.sh.trc.hyflo.core.network.topology.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreatePipelineSegmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdatePipelineSegmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSegmentResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSegmentSummary;
import dz.sh.trc.hyflo.core.network.topology.service.PipelineSegmentService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/network/pipeline-segments")
@Tag(name = "PipelineSegment API", description = "Endpoints for managing PipelineSegment")
public class PipelineSegmentController extends BaseController<CreatePipelineSegmentRequest, UpdatePipelineSegmentRequest, PipelineSegmentResponse, PipelineSegmentSummary> {

    public PipelineSegmentController(PipelineSegmentService service) {
        super(service);
    }

    @Override
    protected Page<PipelineSegmentSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}