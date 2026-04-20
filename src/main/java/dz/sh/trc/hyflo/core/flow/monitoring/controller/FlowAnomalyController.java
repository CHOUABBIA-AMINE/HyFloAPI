package dz.sh.trc.hyflo.core.flow.monitoring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowAnomalyService;

@RestController
@RequestMapping("/api/v1/flow/anomalies")
public class FlowAnomalyController extends BaseController<CreateFlowAnomalyRequest, UpdateFlowAnomalyRequest, FlowAnomalyResponse, FlowAnomalySummary> {

    public FlowAnomalyController(FlowAnomalyService service) {
        super(service);
    }

    @PostMapping("/{id}/analyze")
    @Operation(summary = "Start anomaly analysis")
    public ResponseEntity<Void> analyze(@PathVariable Long id, @RequestBody @Valid AnalyzeAnomalyRequest request) {
        ((FlowAnomalyService)service).analyzeAnomaly(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/fix")
    @Operation(summary = "Mark anomaly as fixed")
    public ResponseEntity<Void> fix(@PathVariable Long id, @RequestBody @Valid FixAnomalyRequest request) {
        ((FlowAnomalyService)service).fixAnomaly(id, request);
        return ResponseEntity.ok().build();
    }

    @Override
    protected Page<FlowAnomalySummary> performSearch(String search, Pageable pageable) {
        return null;
    }
}
