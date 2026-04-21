package dz.sh.trc.hyflo.core.flow.workflow.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.workflow.dto.request.CreateWorkflowStateRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.request.UpdateWorkflowStateRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowStateResponse;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowStateSummary;
import dz.sh.trc.hyflo.core.flow.workflow.service.WorkflowStateService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/flow/workflow-states")
@Tag(name = "WorkflowState API", description = "Endpoints for managing WorkflowState")
public class WorkflowStateController extends BaseController<CreateWorkflowStateRequest, UpdateWorkflowStateRequest, WorkflowStateResponse, WorkflowStateSummary> {
    public WorkflowStateController(WorkflowStateService service) { super(service); }
    @Override
    protected Page<WorkflowStateSummary> performSearch(String search, Pageable pageable) { throw new UnsupportedOperationException("Search not implemented"); }
}