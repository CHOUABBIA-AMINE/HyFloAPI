package dz.sh.trc.hyflo.core.flow.workflow.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.flow.workflow.dto.request.CreateWorkflowTargetTypeRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.request.UpdateWorkflowTargetTypeRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowTargetTypeResponse;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowTargetTypeSummary;
import dz.sh.trc.hyflo.core.flow.workflow.model.WorkflowTargetType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface WorkflowTargetTypeMapper extends BaseMapper<CreateWorkflowTargetTypeRequest, UpdateWorkflowTargetTypeRequest, WorkflowTargetTypeResponse, WorkflowTargetTypeSummary, WorkflowTargetType> {
}