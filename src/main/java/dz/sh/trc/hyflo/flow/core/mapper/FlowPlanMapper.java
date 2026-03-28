/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowPlanMapper
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class (Static Utility Mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Core
 *
 *  @Description: Static utility mapper for FlowPlan.
 *                Owns ALL conversion logic between FlowPlan entity and its DTOs.
 *                DTOs contain NO mapping methods.
 *                Follows FlowReadingMapper pattern exactly.
 *
 **/

package dz.sh.trc.hyflo.flow.core.mapper;

import dz.sh.trc.hyflo.flow.common.model.PlanStatus;
import dz.sh.trc.hyflo.flow.core.dto.FlowPlanReadDTO;
import dz.sh.trc.hyflo.flow.core.dto.command.FlowPlanCommandDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowPlan;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.core.model.Pipeline;

/**
 * Static utility mapper for FlowPlan.
 *
 * This class owns ALL conversion logic between FlowPlan entity
 * and its DTOs. DTOs are pure data carriers — they contain no mapping methods.
 *
 * Pattern mirrors FlowReadingMapper.
 */
public final class FlowPlanMapper {

    private FlowPlanMapper() {}

    // =====================================================================
    // entity → FlowPlanReadDTO
    // =====================================================================

    public static FlowPlanReadDTO toReadDTO(FlowPlan entity) {
        if (entity == null) return null;

        return FlowPlanReadDTO.builder()
                .id(entity.getId())
                .planDate(entity.getPlanDate())
                .plannedVolumeM3(entity.getPlannedVolumeM3())
                .plannedVolumeMscf(entity.getPlannedVolumeMscf())
                .plannedInletPressureBar(entity.getPlannedInletPressureBar())
                .plannedOutletPressureBar(entity.getPlannedOutletPressureBar())
                .plannedTemperatureCelsius(entity.getPlannedTemperatureCelsius())
                .scenarioCode(entity.getScenarioCode())
                .notes(entity.getNotes())
                .approvedAt(entity.getApprovedAt())
                // Pipeline
                .pipelineId(entity.getPipeline() != null ? entity.getPipeline().getId() : null)
                .pipelineCode(entity.getPipeline() != null ? entity.getPipeline().getCode() : null)
                .pipelineName(entity.getPipeline() != null ? entity.getPipeline().getName() : null)
                // Status
                .statusId(entity.getStatus() != null ? entity.getStatus().getId() : null)
                .statusCode(entity.getStatus() != null ? entity.getStatus().getCode() : null)
                // Approver
                .approvedById(entity.getApprovedBy() != null ? entity.getApprovedBy().getId() : null)
                .approverName(resolveEmployeeName(entity.getApprovedBy()))
                // Submitter
                .submittedById(entity.getSubmittedBy() != null ? entity.getSubmittedBy().getId() : null)
                .submitterName(resolveEmployeeName(entity.getSubmittedBy()))
                // Revision chain
                .revisedFromId(entity.getRevisedFrom() != null ? entity.getRevisedFrom().getId() : null)
                .build();
    }

    // =====================================================================
    // FlowPlanCommandDTO → new FlowPlan entity
    // =====================================================================

    public static FlowPlan toEntity(FlowPlanCommandDTO dto) {
        if (dto == null) return null;

        FlowPlan entity = new FlowPlan();
        entity.setPlanDate(dto.getPlanDate());
        entity.setPlannedVolumeM3(dto.getPlannedVolumeM3());
        entity.setPlannedVolumeMscf(dto.getPlannedVolumeMscf());
        entity.setPlannedInletPressureBar(dto.getPlannedInletPressureBar());
        entity.setPlannedOutletPressureBar(dto.getPlannedOutletPressureBar());
        entity.setPlannedTemperatureCelsius(dto.getPlannedTemperatureCelsius());
        entity.setScenarioCode(dto.getScenarioCode());
        entity.setNotes(dto.getNotes());

        if (dto.getPipelineId() != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(dto.getPipelineId());
            entity.setPipeline(pipeline);
        }
        if (dto.getStatusId() != null) {
            PlanStatus status = new PlanStatus();
            status.setId(dto.getStatusId());
            entity.setStatus(status);
        }
        if (dto.getSubmittedByEmployeeId() != null) {
            Employee submitter = new Employee();
            submitter.setId(dto.getSubmittedByEmployeeId());
            entity.setSubmittedBy(submitter);
        }
        if (dto.getRevisedFromId() != null) {
            FlowPlan revisedFrom = new FlowPlan();
            revisedFrom.setId(dto.getRevisedFromId());
            entity.setRevisedFrom(revisedFrom);
        }

        return entity;
    }

    // =====================================================================
    // FlowPlanCommandDTO → update existing FlowPlan entity (patch)
    // =====================================================================

    public static void updateEntity(FlowPlanCommandDTO dto, FlowPlan entity) {
        if (dto == null || entity == null) return;

        if (dto.getPlanDate() != null)                  entity.setPlanDate(dto.getPlanDate());
        if (dto.getPlannedVolumeM3() != null)           entity.setPlannedVolumeM3(dto.getPlannedVolumeM3());
        if (dto.getPlannedVolumeMscf() != null)         entity.setPlannedVolumeMscf(dto.getPlannedVolumeMscf());
        if (dto.getPlannedInletPressureBar() != null)   entity.setPlannedInletPressureBar(dto.getPlannedInletPressureBar());
        if (dto.getPlannedOutletPressureBar() != null)  entity.setPlannedOutletPressureBar(dto.getPlannedOutletPressureBar());
        if (dto.getPlannedTemperatureCelsius() != null) entity.setPlannedTemperatureCelsius(dto.getPlannedTemperatureCelsius());
        if (dto.getScenarioCode() != null)              entity.setScenarioCode(dto.getScenarioCode());
        if (dto.getNotes() != null)                     entity.setNotes(dto.getNotes());

        if (dto.getPipelineId() != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(dto.getPipelineId());
            entity.setPipeline(pipeline);
        }
        if (dto.getStatusId() != null) {
            PlanStatus status = new PlanStatus();
            status.setId(dto.getStatusId());
            entity.setStatus(status);
        }
        if (dto.getSubmittedByEmployeeId() != null) {
            Employee submitter = new Employee();
            submitter.setId(dto.getSubmittedByEmployeeId());
            entity.setSubmittedBy(submitter);
        }
    }

    // =====================================================================
    // PRIVATE HELPERS
    // =====================================================================

    private static String resolveEmployeeName(Employee employee) {
        if (employee == null) return null;
        String first = employee.getFirstName();
        String last  = employee.getLastName();
        if (first == null && last == null) return null;
        return (first != null ? first : "") + (last != null ? " " + last : "");
    }
}
