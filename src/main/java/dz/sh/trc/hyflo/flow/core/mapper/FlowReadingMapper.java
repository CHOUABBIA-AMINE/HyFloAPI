/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingMapper
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.mapper;

import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.dto.command.FlowReadingCommandDto;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.workflow.model.WorkflowInstance;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.core.model.Pipeline;

/**
 * Static utility mapper for FlowReading.
 *
 * This class owns ALL conversion logic between FlowReading entity
 * and its v2 DTOs. DTOs are pure data carriers — they contain no
 * mapping methods.
 */
public final class FlowReadingMapper {

    private FlowReadingMapper() {}

    // =====================================================================
    // entity → FlowReadingReadDto
    // =====================================================================

    public static FlowReadingReadDto toReadDto(FlowReading entity) {
        if (entity == null) return null;

        return FlowReadingReadDto.builder()
                .id(entity.getId())
                .readingDate(entity.getReadingDate())
                .volumeM3(entity.getVolumeM3())
                .volumeMscf(entity.getVolumeMscf())
                .inletPressureBar(entity.getInletPressureBar())
                .outletPressureBar(entity.getOutletPressureBar())
                .temperatureCelsius(entity.getTemperatureCelsius())
                .notes(entity.getNotes())
                .submittedAt(entity.getSubmittedAt())
                .validatedAt(entity.getValidatedAt())
                .pipelineId(entity.getPipeline() != null ? entity.getPipeline().getId() : null)
                .pipelineCode(entity.getPipeline() != null ? entity.getPipeline().getCode() : null)
                .validationStatusId(entity.getValidationStatus() != null
                        ? entity.getValidationStatus().getId() : null)
                .validationStatusCode(entity.getValidationStatus() != null
                        ? entity.getValidationStatus().getCode() : null)
                .readingSlotId(entity.getReadingSlot() != null
                        ? entity.getReadingSlot().getId() : null)
                .readingSlotCode(entity.getReadingSlot() != null
                        ? entity.getReadingSlot().getCode() : null)
                .workflowInstanceId(entity.getWorkflowInstance() != null
                        ? entity.getWorkflowInstance().getId() : null)
                .workflowStateCode(entity.getWorkflowInstance() != null
                        && entity.getWorkflowInstance().getCurrentState() != null
                        ? entity.getWorkflowInstance().getCurrentState().getCode() : null)
                .build();
    }

    // =====================================================================
    // FlowReadingCommandDto → new FlowReading entity
    // =====================================================================

    public static FlowReading toEntity(FlowReadingCommandDto dto) {
        if (dto == null) return null;

        FlowReading entity = new FlowReading();
        entity.setReadingDate(dto.getReadingDate());
        entity.setVolumeM3(dto.getVolumeM3());
        entity.setVolumeMscf(dto.getVolumeMscf());
        entity.setInletPressureBar(dto.getInletPressureBar());
        entity.setOutletPressureBar(dto.getOutletPressureBar());
        entity.setTemperatureCelsius(dto.getTemperatureCelsius());
        entity.setNotes(dto.getNotes());
        entity.setSubmittedAt(dto.getSubmittedAt());

        if (dto.getPipelineId() != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(dto.getPipelineId());
            entity.setPipeline(pipeline);
        }
        if (dto.getReadingSlotId() != null) {
            ReadingSlot slot = new ReadingSlot();
            slot.setId(dto.getReadingSlotId());
            entity.setReadingSlot(slot);
        }
        if (dto.getValidationStatusId() != null) {
            ValidationStatus status = new ValidationStatus();
            status.setId(dto.getValidationStatusId());
            entity.setValidationStatus(status);
        }
        if (dto.getRecordedById() != null) {
            Employee employee = new Employee();
            employee.setId(dto.getRecordedById());
            entity.setRecordedBy(employee);
        }
        if (dto.getWorkflowInstanceId() != null) {
            WorkflowInstance wf = new WorkflowInstance();
            wf.setId(dto.getWorkflowInstanceId());
            entity.setWorkflowInstance(wf);
        }

        return entity;
    }

    // =====================================================================
    // FlowReadingCommandDto → update existing FlowReading entity (patch)
    // =====================================================================

    public static void updateEntity(FlowReadingCommandDto dto, FlowReading entity) {
        if (dto == null || entity == null) return;

        if (dto.getReadingDate() != null)        entity.setReadingDate(dto.getReadingDate());
        if (dto.getVolumeM3() != null)           entity.setVolumeM3(dto.getVolumeM3());
        if (dto.getVolumeMscf() != null)         entity.setVolumeMscf(dto.getVolumeMscf());
        if (dto.getInletPressureBar() != null)   entity.setInletPressureBar(dto.getInletPressureBar());
        if (dto.getOutletPressureBar() != null)  entity.setOutletPressureBar(dto.getOutletPressureBar());
        if (dto.getTemperatureCelsius() != null) entity.setTemperatureCelsius(dto.getTemperatureCelsius());
        if (dto.getNotes() != null)              entity.setNotes(dto.getNotes());
        if (dto.getSubmittedAt() != null)        entity.setSubmittedAt(dto.getSubmittedAt());

        if (dto.getPipelineId() != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(dto.getPipelineId());
            entity.setPipeline(pipeline);
        }
        if (dto.getReadingSlotId() != null) {
            ReadingSlot slot = new ReadingSlot();
            slot.setId(dto.getReadingSlotId());
            entity.setReadingSlot(slot);
        }
        if (dto.getValidationStatusId() != null) {
            ValidationStatus status = new ValidationStatus();
            status.setId(dto.getValidationStatusId());
            entity.setValidationStatus(status);
        }
        if (dto.getWorkflowInstanceId() != null) {
            WorkflowInstance wf = new WorkflowInstance();
            wf.setId(dto.getWorkflowInstanceId());
            entity.setWorkflowInstance(wf);
        }
    }
}
