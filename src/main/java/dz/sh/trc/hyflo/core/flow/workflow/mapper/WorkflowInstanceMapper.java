package dz.sh.trc.hyflo.core.flow.workflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowInstanceResponse;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowInstanceSummary;
import dz.sh.trc.hyflo.core.flow.workflow.model.WorkflowInstance;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkflowInstanceMapper {

    @Mapping(target = "targetTypeId", source = "targetType.id")
    @Mapping(target = "targetTypeDesignationFr", source = "targetType.designationFr")
    @Mapping(target = "currentStateId", source = "currentState.id")
    @Mapping(target = "currentStateDesignationFr", source = "currentState.designationFr")
    @Mapping(target = "initiatedById", source = "initiatedBy.id")
    @Mapping(target = "initiatedByName", source = "initiatedBy.fullNameLt") // Simplified for now
    @Mapping(target = "lastActorId", source = "lastActor.id")
    @Mapping(target = "lastActorName", source = "lastActor.fullNameLt") // Simplified for now
    WorkflowInstanceResponse toResponse(WorkflowInstance entity);

    @Mapping(target = "targetTypeDesignationFr", source = "targetType.designationFr")
    @Mapping(target = "currentStateDesignationFr", source = "currentState.designationFr")
    @Mapping(target = "initiatedByName", source = "initiatedBy.fullNameLt")
    WorkflowInstanceSummary toSummary(WorkflowInstance entity);
    
    List<WorkflowInstanceResponse> toResponseList(List<WorkflowInstance> entities);
}
