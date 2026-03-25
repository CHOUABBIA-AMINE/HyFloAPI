/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : WorkflowInstanceMapper
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.mapper;

import dz.sh.trc.hyflo.flow.workflow.dto.command.WorkflowInstanceCommandDto;
import dz.sh.trc.hyflo.flow.workflow.dto.query.WorkflowInstanceReadDto;
import dz.sh.trc.hyflo.flow.workflow.model.WorkflowInstance;
import dz.sh.trc.hyflo.flow.workflow.model.WorkflowState;
import dz.sh.trc.hyflo.flow.workflow.model.WorkflowTargetType;
import dz.sh.trc.hyflo.general.organization.model.Employee;

public final class WorkflowInstanceMapper {

    private WorkflowInstanceMapper() {}

    // =====================================================================
    // entity → WorkflowInstanceReadDto
    // =====================================================================

    public static WorkflowInstanceReadDto toReadDto(WorkflowInstance entity) {
        if (entity == null) return null;

        return WorkflowInstanceReadDto.builder()
                .id(entity.getId())
                .targetTypeId(entity.getTargetType() != null
                        ? entity.getTargetType().getId() : null)
                .targetTypeCode(entity.getTargetType() != null
                        ? entity.getTargetType().getCode() : null)
                .currentStateId(entity.getCurrentState() != null
                        ? entity.getCurrentState().getId() : null)
                .currentStateCode(entity.getCurrentState() != null
                        ? entity.getCurrentState().getCode() : null)
                .currentStateLabel(entity.getCurrentState() != null
                        ? entity.getCurrentState().getLabel() : null)
                .startedAt(entity.getStartedAt())
                .completedAt(entity.getCompletedAt())
                .initiatedById(entity.getInitiatedBy() != null
                        ? entity.getInitiatedBy().getId() : null)
                .initiatedByFullName(entity.getInitiatedBy() != null
                        ? buildFullName(entity.getInitiatedBy()) : null)
                .lastActorId(entity.getLastActor() != null
                        ? entity.getLastActor().getId() : null)
                .lastActorFullName(entity.getLastActor() != null
                        ? buildFullName(entity.getLastActor()) : null)
                .comment(entity.getComment())
                .history(entity.getHistory())
                .build();
    }

    // =====================================================================
    // WorkflowInstanceCommandDto → new WorkflowInstance entity
    // =====================================================================

    public static WorkflowInstance toEntity(WorkflowInstanceCommandDto dto) {
        if (dto == null) return null;

        WorkflowInstance entity = new WorkflowInstance();
        entity.setComment(dto.getComment());

        if (dto.getTargetTypeId() != null) {
            WorkflowTargetType targetType = new WorkflowTargetType();
            targetType.setId(dto.getTargetTypeId());
            entity.setTargetType(targetType);
        }
        if (dto.getCurrentStateId() != null) {
            WorkflowState state = new WorkflowState();
            state.setId(dto.getCurrentStateId());
            entity.setCurrentState(state);
        }
        if (dto.getInitiatedById() != null) {
            Employee employee = new Employee();
            employee.setId(dto.getInitiatedById());
            entity.setInitiatedBy(employee);
        }
        if (dto.getLastActorId() != null) {
            Employee employee = new Employee();
            employee.setId(dto.getLastActorId());
            entity.setLastActor(employee);
        }

        return entity;
    }

    // =====================================================================
    // Private helpers
    // =====================================================================

    private static String buildFullName(Employee employee) {
        if (employee == null) return null;
        String first = employee.getFirstName() != null ? employee.getFirstName() : "";
        String last  = employee.getLastName()  != null ? employee.getLastName()  : "";
        return (first + " " + last).trim();
    }
}
