package dz.sh.trc.hyflo.core.flow.monitoring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowDeviationService;

@RestController
@RequestMapping("/flow/deviations")
public class FlowDeviationController extends BaseController<CreateFlowDeviationRequest, UpdateFlowDeviationRequest, FlowDeviationResponse, FlowDeviationSummary> {

    public FlowDeviationController(FlowDeviationService service) {
        super(service);
    }

    @Override
    protected Page<FlowDeviationSummary> performSearch(String search, Pageable pageable) {
        return null;
    }
}
