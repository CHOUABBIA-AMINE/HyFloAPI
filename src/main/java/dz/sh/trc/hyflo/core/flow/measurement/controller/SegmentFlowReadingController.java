package dz.sh.trc.hyflo.core.flow.measurement.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateSegmentFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateSegmentFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.SegmentFlowReadingResponse;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.SegmentFlowReadingSummary;
import dz.sh.trc.hyflo.core.flow.measurement.model.SegmentFlowReading;
import dz.sh.trc.hyflo.core.flow.measurement.service.SegmentFlowReadingService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/v1/flow/segment-flow-readings")
@Tag(name = "SegmentFlowReading API", description = "Endpoints for SegmentFlowReading")
public class SegmentFlowReadingController extends BaseController<CreateSegmentFlowReadingRequest, UpdateSegmentFlowReadingRequest, SegmentFlowReadingResponse, SegmentFlowReadingSummary> {
    public SegmentFlowReadingController(SegmentFlowReadingService service) { super(service); }
    @Override
    protected Page<SegmentFlowReadingSummary> performSearch(String search, Pageable pageable) { throw new UnsupportedOperationException("Search not implemented"); }
}