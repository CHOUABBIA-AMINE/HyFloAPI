package dz.sh.trc.hyflo.core.flow.planning.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.planning.dto.*;
import dz.sh.trc.hyflo.core.flow.planning.service.FlowPlanService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/flow/planning/plans")
@Tag(name = "FlowPlan API", description = "Endpoints for managing pipeline flow plans and approvals")
public class FlowPlanController extends BaseController<CreateFlowPlanRequest, UpdateFlowPlanRequest, FlowPlanResponse, FlowPlanSummary> {

    private final FlowPlanService flowPlanService;

    public FlowPlanController(FlowPlanService service) {
        super(service);
        this.flowPlanService = service;
    }

    @Operation(summary = "Approve a flow plan", description = "Transitions the plan from DRAFT to APPROVED status")
    @PostMapping("/{id}/approve")
    public ResponseEntity<FlowPlanResponse> approvePlan(
            @PathVariable Long id,
            @RequestParam Long approverEmployeeId) {
        return ResponseEntity.ok(flowPlanService.approvePlan(id, approverEmployeeId));
    }

    @Override
    protected Page<FlowPlanSummary> performSearch(String search, Pageable pageable) {
        return null;
    }
}
