package dz.sh.trc.hyflo.core.flow.workflow.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.flow.workflow.dto.request.CreateWorkflowStateRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.request.UpdateWorkflowStateRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowStateResponse;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowStateSummary;
import dz.sh.trc.hyflo.core.flow.workflow.model.WorkflowState;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface WorkflowStateMapper extends BaseMapper<CreateWorkflowStateRequest, UpdateWorkflowStateRequest, WorkflowStateResponse, WorkflowStateSummary, WorkflowState> {
}