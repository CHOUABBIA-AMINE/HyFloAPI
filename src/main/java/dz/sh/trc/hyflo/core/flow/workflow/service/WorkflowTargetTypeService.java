package dz.sh.trc.hyflo.core.flow.workflow.service;

import dz.sh.trc.hyflo.core.flow.workflow.dto.request.CreateWorkflowTargetTypeRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.request.UpdateWorkflowTargetTypeRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowTargetTypeResponse;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowTargetTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface WorkflowTargetTypeService extends BaseService<CreateWorkflowTargetTypeRequest, UpdateWorkflowTargetTypeRequest, WorkflowTargetTypeResponse, WorkflowTargetTypeSummary> {
}