package dz.sh.trc.hyflo.core.flow.measurement.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.FlowReadingFilterDTO;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowReadingResponse;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowReadingSummary;
import dz.sh.trc.hyflo.core.flow.measurement.service.FlowReadingService;
import dz.sh.trc.hyflo.core.flow.workflow.dto.request.WorkflowActionDTO;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/flow/readings")
@Tag(name = "FlowReading API", description = "Endpoints for Flow Readings and Validation Workflows")
public class FlowReadingController extends BaseController<CreateFlowReadingRequest, UpdateFlowReadingRequest, FlowReadingResponse, FlowReadingSummary> {

    private final FlowReadingService flowService;

    public FlowReadingController(FlowReadingService service) {
        super(service);
        this.flowService = service;
    }

    @Override
    protected Page<FlowReadingSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<FlowReadingResponse> submit(@PathVariable Long id, @Valid @RequestBody WorkflowActionDTO action) {
        return ResponseEntity.ok(flowService.submitReading(id, action));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<FlowReadingResponse> validate(@PathVariable Long id, @Valid @RequestBody WorkflowActionDTO action) {
        return ResponseEntity.ok(flowService.validateReading(id, action));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<FlowReadingResponse> approve(@PathVariable Long id, @Valid @RequestBody WorkflowActionDTO action) {
        return ResponseEntity.ok(flowService.approveReading(id, action));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<FlowReadingResponse> reject(@PathVariable Long id, @Valid @RequestBody WorkflowActionDTO action) {
        return ResponseEntity.ok(flowService.rejectReading(id, action));
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<FlowReadingSummary>> filter(@RequestBody FlowReadingFilterDTO filter, Pageable pageable) {
        return ResponseEntity.ok(flowService.findByFilter(filter, pageable));
    }
}