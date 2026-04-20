package dz.sh.trc.hyflo.core.flow.workflow.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.workflow.dto.request.CreateWorkflowTargetTypeRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.request.UpdateWorkflowTargetTypeRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowTargetTypeResponse;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowTargetTypeSummary;
import dz.sh.trc.hyflo.core.flow.workflow.model.WorkflowTargetType;
import dz.sh.trc.hyflo.core.flow.workflow.service.WorkflowTargetTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/flow/workflow-target-types")
@Tag(name = "WorkflowTargetType API", description = "Endpoints for managing WorkflowTargetType")
public class WorkflowTargetTypeController extends BaseController<CreateWorkflowTargetTypeRequest, UpdateWorkflowTargetTypeRequest, WorkflowTargetTypeResponse, WorkflowTargetTypeSummary> {
    public WorkflowTargetTypeController(WorkflowTargetTypeService service) { super(service); }
    @Override
    protected Page<WorkflowTargetTypeSummary> performSearch(String search, Pageable pageable) { throw new UnsupportedOperationException("Search not implemented"); }
}