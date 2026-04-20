package dz.sh.trc.hyflo.core.flow.workflow.controller;

import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowInstanceResponse;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowTransitionDTO;
import dz.sh.trc.hyflo.core.flow.workflow.service.WorkflowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flow/workflow-instances")
@Tag(name = "Workflow API", description = "Endpoints for managing Workflows")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowInstanceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.getById(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<WorkflowTransitionDTO>> getHistory(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.getWorkflowHistory(id));
    }
}
