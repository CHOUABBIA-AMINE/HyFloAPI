package dz.sh.trc.hyflo.core.flow.workflow.service;

import dz.sh.trc.hyflo.core.flow.workflow.dto.request.CreateWorkflowStateRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.request.UpdateWorkflowStateRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowStateResponse;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowStateSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface WorkflowStateService extends BaseService<CreateWorkflowStateRequest, UpdateWorkflowStateRequest, WorkflowStateResponse, WorkflowStateSummary> {
}