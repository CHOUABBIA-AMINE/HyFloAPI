package dz.sh.trc.hyflo.core.flow.monitoring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowAnomalyService;

@RestController
@RequestMapping("/api/v1/flow/anomalies")
public class FlowAnomalyController extends BaseController<CreateFlowAnomalyRequest, UpdateFlowAnomalyRequest, FlowAnomalyResponse, FlowAnomalySummary, dz.sh.trc.hyflo.core.flow.monitoring.model.FlowAnomaly, Long> {

    public FlowAnomalyController(FlowAnomalyService service) {
        super(service);
    }

    @Override
    protected Page<FlowAnomalySummary> performSearch(String search, Pageable pageable) {
        return null;
    }
}
