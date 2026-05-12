package dz.sh.trc.hyflo.core.flow.workflow.service;

import dz.sh.trc.hyflo.core.flow.workflow.dto.request.WorkflowActionDTO;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowInstanceResponse;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowTransitionDTO;

import java.util.List;

public interface WorkflowService {
    WorkflowInstanceResponse initiateWorkflow(String targetTypeCode, Long initiatorEmployeeId);
    WorkflowInstanceResponse transitionState(Long instanceId, WorkflowActionDTO actionDTO);
    List<WorkflowTransitionDTO> getWorkflowHistory(Long instanceId);
    WorkflowInstanceResponse getById(Long instanceId);
}
