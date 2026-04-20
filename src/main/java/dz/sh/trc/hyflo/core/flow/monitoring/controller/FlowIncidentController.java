package dz.sh.trc.hyflo.core.flow.monitoring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowIncidentService;

@RestController
@RequestMapping("/api/v1/flow/incidents")
public class FlowIncidentController extends BaseController<CreateFlowIncidentRequest, UpdateFlowIncidentRequest, FlowIncidentResponse, FlowIncidentSummary, dz.sh.trc.hyflo.core.flow.monitoring.model.FlowIncident, Long> {

    public FlowIncidentController(FlowIncidentService service) {
        super(service);
    }

    @Override
    protected Page<FlowIncidentSummary> performSearch(String search, Pageable pageable) {
        return null;
    }
}
