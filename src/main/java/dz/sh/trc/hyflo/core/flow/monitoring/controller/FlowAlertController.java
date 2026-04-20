package dz.sh.trc.hyflo.core.flow.monitoring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowAlertService;

@RestController
@RequestMapping("/api/v1/flow/alerts")
public class FlowAlertController extends BaseController<CreateFlowAlertRequest, UpdateFlowAlertRequest, FlowAlertResponse, FlowAlertSummary, dz.sh.trc.hyflo.core.flow.monitoring.model.FlowAlert, Long> {

    private final FlowAlertService flowAlertService;

    public FlowAlertController(FlowAlertService service) {
        super(service);
        this.flowAlertService = service;
    }

    @PostMapping("/{id}/acknowledge")
    public ResponseEntity<Void> acknowledgeAlert(@PathVariable Long id, @RequestBody AcknowledgeAlertRequest request) {
        flowAlertService.acknowledgeAlert(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<Void> resolveAlert(@PathVariable Long id, @RequestBody ResolveAlertRequest request) {
        flowAlertService.resolveAlert(id, request);
        return ResponseEntity.ok().build();
    }

    @Override
    protected Page<FlowAlertSummary> performSearch(String search, Pageable pageable) {
        return null;
    }
}
