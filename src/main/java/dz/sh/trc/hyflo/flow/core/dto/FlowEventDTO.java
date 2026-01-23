/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowEventDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.common.dto.EventStatusDTO;
import dz.sh.trc.hyflo.flow.common.model.EventStatus;
import dz.sh.trc.hyflo.flow.core.model.FlowEvent;
import dz.sh.trc.hyflo.flow.type.dto.EventTypeDTO;
import dz.sh.trc.hyflo.flow.type.model.EventType;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.core.dto.InfrastructureDTO;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Flow event DTO for operational events and incidents")
public class FlowEventDTO extends GenericDTO<FlowEvent> {

    @NotBlank(message = "Event title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    @Schema(description = "Event title", example = "Planned Maintenance", required = true, maxLength = 200)
    private String title;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    @Schema(description = "Event description", maxLength = 2000)
    private String description;

    @NotNull(message = "Start time is required")
    @PastOrPresent(message = "Start time cannot be in the future")
    @Schema(description = "Event start timestamp", example = "2026-01-22T08:00:00", required = true)
    private LocalDateTime startTime;

    @Schema(description = "Event end timestamp", example = "2026-01-22T12:00:00")
    private LocalDateTime endTime;

    @NotNull(message = "Planned flag is required")
    @Schema(description = "Is this a planned event", example = "true", required = true)
    private Boolean planned;

    @Size(max = 100, message = "Impact level must not exceed 100 characters")
    @Schema(description = "Impact level", example = "MEDIUM", maxLength = 100)
    private String impactLevel;

    @Size(max = 1000, message = "Resolution notes must not exceed 1000 characters")
    @Schema(description = "Resolution notes", maxLength = 1000)
    private String resolutionNotes;

    @PastOrPresent(message = "Resolution time cannot be in the future")
    @Schema(description = "Resolution timestamp", example = "2026-01-22T12:15:00")
    private LocalDateTime resolvedAt;

    // Foreign Key IDs
    @NotNull(message = "Infrastructure is required")
    @Schema(description = "Infrastructure ID", required = true)
    private Long infrastructureId;

    @NotNull(message = "Event type is required")
    @Schema(description = "Event type ID", required = true)
    private Long typeId;

    @NotNull(message = "Recording employee is required")
    @Schema(description = "Recorded by employee ID", required = true)
    private Long recordedById;

    @Schema(description = "Resolved by employee ID")
    private Long resolvedById;

    @Schema(description = "Event status ID")
    private Long statusId;

    // Nested DTOs
    @Schema(description = "Infrastructure details")
    private InfrastructureDTO infrastructure;

    @Schema(description = "Event type details")
    private EventTypeDTO type;

    @Schema(description = "Recording employee details")
    private EmployeeDTO recordedBy;

    @Schema(description = "Resolving employee details")
    private EmployeeDTO resolvedBy;

    @Schema(description = "Event status details")
    private EventStatusDTO status;

    @Override
    public FlowEvent toEntity() {
        FlowEvent entity = new FlowEvent();
        entity.setId(getId());
        entity.setTitle(this.title);
        entity.setDescription(this.description);
        entity.setStartTime(this.startTime);
        entity.setEndTime(this.endTime);
        entity.setPlanned(this.planned);
        entity.setImpactLevel(this.impactLevel);
        entity.setResolutionNotes(this.resolutionNotes);
        entity.setResolvedAt(this.resolvedAt);

        if (this.infrastructureId != null) {
            Infrastructure infrastructure = new Infrastructure();
            infrastructure.setId(this.infrastructureId);
            entity.setInfrastructure(infrastructure);
        }

        if (this.typeId != null) {
            EventType type = new EventType();
            type.setId(this.typeId);
            entity.setType(type);
        }

        if (this.recordedById != null) {
            Employee employee = new Employee();
            employee.setId(this.recordedById);
            entity.setRecordedBy(employee);
        }

        if (this.resolvedById != null) {
            Employee employee = new Employee();
            employee.setId(this.resolvedById);
            entity.setResolvedBy(employee);
        }

        if (this.statusId != null) {
            EventStatus status = new EventStatus();
            status.setId(this.statusId);
            entity.setStatus(status);
        }

        return entity;
    }

    @Override
    public void updateEntity(FlowEvent entity) {
        if (this.title != null) entity.setTitle(this.title);
        if (this.description != null) entity.setDescription(this.description);
        if (this.startTime != null) entity.setStartTime(this.startTime);
        if (this.endTime != null) entity.setEndTime(this.endTime);
        if (this.planned != null) entity.setPlanned(this.planned);
        if (this.impactLevel != null) entity.setImpactLevel(this.impactLevel);
        if (this.resolutionNotes != null) entity.setResolutionNotes(this.resolutionNotes);
        if (this.resolvedAt != null) entity.setResolvedAt(this.resolvedAt);

        if (this.infrastructureId != null) {
            Infrastructure infrastructure = new Infrastructure();
            infrastructure.setId(this.infrastructureId);
            entity.setInfrastructure(infrastructure);
        }

        if (this.typeId != null) {
            EventType type = new EventType();
            type.setId(this.typeId);
            entity.setType(type);
        }

        if (this.recordedById != null) {
            Employee employee = new Employee();
            employee.setId(this.recordedById);
            entity.setRecordedBy(employee);
        }

        if (this.resolvedById != null) {
            Employee employee = new Employee();
            employee.setId(this.resolvedById);
            entity.setResolvedBy(employee);
        }

        if (this.statusId != null) {
            EventStatus status = new EventStatus();
            status.setId(this.statusId);
            entity.setStatus(status);
        }
    }

    public static FlowEventDTO fromEntity(FlowEvent entity) {
        if (entity == null) return null;

        return FlowEventDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .planned(entity.getPlanned())
                .impactLevel(entity.getImpactLevel())
                .resolutionNotes(entity.getResolutionNotes())
                .resolvedAt(entity.getResolvedAt())

                .infrastructureId(entity.getInfrastructure() != null ? entity.getInfrastructure().getId() : null)
                .typeId(entity.getType() != null ? entity.getType().getId() : null)
                .recordedById(entity.getRecordedBy() != null ? entity.getRecordedBy().getId() : null)
                .resolvedById(entity.getResolvedBy() != null ? entity.getResolvedBy().getId() : null)
                .statusId(entity.getStatus() != null ? entity.getStatus().getId() : null)

                .infrastructure(entity.getInfrastructure() != null ? InfrastructureDTO.fromEntity(entity.getInfrastructure()) : null)
                .type(entity.getType() != null ? EventTypeDTO.fromEntity(entity.getType()) : null)
                .recordedBy(entity.getRecordedBy() != null ? EmployeeDTO.fromEntity(entity.getRecordedBy()) : null)
                .resolvedBy(entity.getResolvedBy() != null ? EmployeeDTO.fromEntity(entity.getResolvedBy()) : null)
                .status(entity.getStatus() != null ? EventStatusDTO.fromEntity(entity.getStatus()) : null)
                .build();
    }
}
