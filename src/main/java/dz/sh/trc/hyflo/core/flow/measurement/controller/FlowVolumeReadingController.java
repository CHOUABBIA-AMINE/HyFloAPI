package dz.sh.trc.hyflo.core.flow.measurement.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateFlowVolumeReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateFlowVolumeReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowVolumeReadingResponse;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowVolumeReadingSummary;
import dz.sh.trc.hyflo.core.flow.measurement.service.FlowVolumeReadingService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/v1/flow/flow-volume-readings")
@Tag(name = "FlowVolumeReading API", description = "Endpoints for FlowVolumeReading")
public class FlowVolumeReadingController extends BaseController<CreateFlowVolumeReadingRequest, UpdateFlowVolumeReadingRequest, FlowVolumeReadingResponse, FlowVolumeReadingSummary> {
    public FlowVolumeReadingController(FlowVolumeReadingService service) { super(service); }
    @Override
    protected Page<FlowVolumeReadingSummary> performSearch(String search, Pageable pageable) { throw new UnsupportedOperationException("Search not implemented"); }
}