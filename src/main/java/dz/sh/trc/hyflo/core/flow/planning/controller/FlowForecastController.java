package dz.sh.trc.hyflo.core.flow.planning.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import dz.sh.trc.hyflo.core.flow.planning.dto.*;
import dz.sh.trc.hyflo.core.flow.planning.service.FlowForecastService;

@RestController
@RequestMapping("/api/v1/flow/planning/forecasts")
public class FlowForecastController extends BaseController<CreateFlowForecastRequest, UpdateFlowForecastRequest, FlowForecastResponse, FlowForecastSummary> {

    public FlowForecastController(FlowForecastService service) {
        super(service);
    }

    @Override
    protected Page<FlowForecastSummary> performSearch(String search, Pageable pageable) {
        return null;
    }
}
