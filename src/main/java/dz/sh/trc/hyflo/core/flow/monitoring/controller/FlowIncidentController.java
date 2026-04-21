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
import dz.sh.trc.hyflo.core.flow.monitoring.service.FlowIncidentService;

@RestController
@RequestMapping("/flow/incidents")
public class FlowIncidentController extends BaseController<CreateFlowIncidentRequest, UpdateFlowIncidentRequest, FlowIncidentResponse, FlowIncidentSummary> {

    public FlowIncidentController(FlowIncidentService service) {
        super(service);
    }

    @PostMapping("/{id}/investigate")
    @Operation(summary = "Start incident investigation")
    public ResponseEntity<Void> investigate(@PathVariable Long id, @RequestBody @Valid InvestigateIncidentRequest request) {
        ((FlowIncidentService)service).investigateIncident(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/resolve")
    @Operation(summary = "Resolve incident")
    public ResponseEntity<Void> resolve(@PathVariable Long id, @RequestBody @Valid ResolveIncidentRequest request) {
        ((FlowIncidentService)service).resolveIncident(id, request);
        return ResponseEntity.ok().build();
    }

    @Override
    protected Page<FlowIncidentSummary> performSearch(String search, Pageable pageable) {
        return null;
    }
}
