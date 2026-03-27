/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : WorkflowInstanceMapper
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 — fix: buildFullName() getFirstName()/getLastName() →
 *                              getFirstNameLt()/getLastNameLt() (Person fields are
 *                              bilingual-suffixed; no plain firstName/lastName exists)
 *              — fix: currentStateLabel: getLabel() → getDesignationFr()
 *                              (WorkflowState has designationFr, not label)
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.mapper;

import dz.sh.trc.hyflo.flow.workflow.dto.command.WorkflowInstanceCommandDTO;
import dz.sh.trc.hyflo.flow.workflow.dto.query.WorkflowInstanceReadDTO;
import dz.sh.trc.hyflo.flow.workflow.model.WorkflowInstance;
import dz.sh.trc.hyflo.flow.workflow.model.WorkflowState;
import dz.sh.trc.hyflo.flow.workflow.model.WorkflowTargetType;
import dz.sh.trc.hyflo.general.organization.model.Employee;

public final class WorkflowInstanceMapper {

    private WorkflowInstanceMapper() {}

    // =====================================================================
    // entity → WorkflowInstanceReadDTO
    // =====================================================================

    public static WorkflowInstanceReadDTO toReadDTO(WorkflowInstance entity) {
        if (entity == null) return null;

        return WorkflowInstanceReadDTO.builder()
                .id(entity.getId())
                .targetTypeId(entity.getTargetType() != null
                        ? entity.getTargetType().getId() : null)
                .targetTypeCode(entity.getTargetType() != null
                        ? entity.getTargetType().getCode() : null)
                .currentStateId(entity.getCurrentState() != null
                        ? entity.getCurrentState().getId() : null)
                .currentStateCode(entity.getCurrentState() != null
                        ? entity.getCurrentState().getCode() : null)
                // FIX: WorkflowState has no getLabel() — field is designationFr
                .currentStateLabel(entity.getCurrentState() != null
                        ? entity.getCurrentState().getDesignationFr() : null)
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
    // WorkflowInstanceCommandDTO → new WorkflowInstance entity
    // =====================================================================

    public static WorkflowInstance toEntity(WorkflowInstanceCommandDTO dto) {
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

    /**
     * Build full name in Latin script from an Employee.
     *
     * FIX: Person fields are bilingual-suffixed.
     *   - Latin first name: getFirstNameLt()
     *   - Latin last  name: getLastNameLt()
     * There are no plain getFirstName() / getLastName() methods on Person/Employee.
     */
    private static String buildFullName(Employee employee) {
        if (employee == null) return null;
        String first = employee.getFirstNameLt() != null ? employee.getFirstNameLt() : "";
        String last  = employee.getLastNameLt()  != null ? employee.getLastNameLt()  : "";
        return (first + " " + last).trim();
    }
}
