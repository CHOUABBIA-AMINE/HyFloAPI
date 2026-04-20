package dz.sh.trc.hyflo.core.flow.monitoring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowThresholdService;

@RestController
@RequestMapping("/api/v1/flow/thresholds")
public class FlowThresholdController extends BaseController<CreateFlowThresholdRequest, UpdateFlowThresholdRequest, FlowThresholdResponse, FlowThresholdSummary> {

    public FlowThresholdController(FlowThresholdService service) {
        super(service);
    }

    @Override
    protected Page<FlowThresholdSummary> performSearch(String search, Pageable pageable) {
        return null;
    }
}
