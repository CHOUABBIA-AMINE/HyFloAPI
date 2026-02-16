/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowEventDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026 - Updated @Schema documentation and requiredMode syntax
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
import dz.sh.trc.hyflo.flow.common.dto.SeverityDTO;
import dz.sh.trc.hyflo.flow.common.model.EventStatus;
import dz.sh.trc.hyflo.flow.common.model.Severity;
import dz.sh.trc.hyflo.flow.core.model.FlowAlert;
import dz.sh.trc.hyflo.flow.core.model.FlowEvent;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
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

/**
 * Data Transfer Object for FlowEvent entity.
 * Used for API requests and responses related to operational events and incidents.
 */
@Schema(description = "Flow event DTO for operational activities, incidents, and maintenance tracking")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowEventDTO extends GenericDTO<FlowEvent> {

    @Schema(
        description = "Timestamp when the event occurred",
        example = "2026-01-22T03:15:00",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Event timestamp is required")
    @PastOrPresent(message = "Event timestamp cannot be in the future")
    private LocalDateTime eventTimestamp;

    @Schema(
        description = "Brief title summarizing the event",
        example = "Emergency Shutdown - Pipeline P-101",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 3,
        maxLength = 100
    )
    @NotBlank(message = "Event title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @Schema(
        description = "Detailed description of the event, including circumstances and observations",
        example = "Automatic shutdown triggered due to pressure spike. Leak detected at KP 45.2.",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 2000
    )
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Schema(
        description = "Timestamp when the event started (for ongoing or time-bound events)",
        example = "2026-01-22T03:15:00",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Start time cannot be in the future")
    private LocalDateTime startTime;

    @Schema(
        description = "Timestamp when the event ended (for completed events)",
        example = "2026-01-22T05:30:00",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private LocalDateTime endTime;

    @Schema(
        description = "Description of corrective action taken to address the event",
        example = "Isolated affected segment, deployed repair crew. Leak sealed and pressure restored.",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 2000
    )
    @Size(max = 2000, message = "Action taken must not exceed 2000 characters")
    private String actionTaken;

    @Schema(
        description = "Indicates whether this event impacted normal flow operations",
        example = "true",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Impact on flow flag is required")
    private Boolean impactOnFlow;

    // Foreign Key IDs
    @Schema(
        description = "ID of the severity level of this event",
        example = "2",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long severityId;

    @Schema(
        description = "ID of the infrastructure affected by this event",
        example = "101",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Infrastructure is required")
    private Long infrastructureId;

    @Schema(
        description = "ID of the employee who reported this event",
        example = "345",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Reporter is required")
    private Long reportedById;

    @Schema(
        description = "ID of the related flow reading if this event was triggered by a measurement",
        example = "789",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long relatedReadingId;

    @Schema(
        description = "ID of the related alert if this event originated from an alert",
        example = "456",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long relatedAlertId;

    @Schema(
        description = "ID of the current status of this event",
        example = "3",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long statusId;

    // Nested DTOs
    @Schema(
        description = "Details of the severity level",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private SeverityDTO severity;

    @Schema(
        description = "Details of the affected infrastructure",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private InfrastructureDTO infrastructure;

    @Schema(
        description = "Details of the employee who reported the event",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private EmployeeDTO reportedBy;

    @Schema(
        description = "Details of the related flow reading",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private FlowReadingDTO relatedReading;

    @Schema(
        description = "Details of the related alert",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private FlowAlertDTO relatedAlert;

    @Schema(
        description = "Details of the current event status",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private EventStatusDTO status;

    @Override
    public FlowEvent toEntity() {
        FlowEvent entity = new FlowEvent();
        entity.setId(getId());
        entity.setEventTimestamp(this.eventTimestamp);
        entity.setTitle(this.title);
        entity.setDescription(this.description);
        entity.setStartTime(this.startTime);
        entity.setEndTime(this.endTime);
        entity.setActionTaken(this.actionTaken);
        entity.setImpactOnFlow(this.impactOnFlow);

        if (this.severityId != null) {
            Severity severity = new Severity();
            severity.setId(this.severityId);
            entity.setSeverity(severity);
        }

        if (this.infrastructureId != null) {
            Infrastructure infrastructure = new Infrastructure();
            infrastructure.setId(this.infrastructureId);
            entity.setInfrastructure(infrastructure);
        }

        if (this.reportedById != null) {
            Employee employee = new Employee();
            employee.setId(this.reportedById);
            entity.setReportedBy(employee);
        }

        if (this.relatedReadingId != null) {
            FlowReading reading = new FlowReading();
            reading.setId(this.relatedReadingId);
            entity.setRelatedReading(reading);
        }

        if (this.relatedAlertId != null) {
            FlowAlert alert = new FlowAlert();
            alert.setId(this.relatedAlertId);
            entity.setRelatedAlert(alert);
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
        if (this.eventTimestamp != null) entity.setEventTimestamp(this.eventTimestamp);
        if (this.title != null) entity.setTitle(this.title);
        if (this.description != null) entity.setDescription(this.description);
        if (this.startTime != null) entity.setStartTime(this.startTime);
        if (this.endTime != null) entity.setEndTime(this.endTime);
        if (this.actionTaken != null) entity.setActionTaken(this.actionTaken);
        if (this.impactOnFlow != null) entity.setImpactOnFlow(this.impactOnFlow);

        if (this.severityId != null) {
            Severity severity = new Severity();
            severity.setId(this.severityId);
            entity.setSeverity(severity);
        }

        if (this.infrastructureId != null) {
            Infrastructure infrastructure = new Infrastructure();
            infrastructure.setId(this.infrastructureId);
            entity.setInfrastructure(infrastructure);
        }

        if (this.reportedById != null) {
            Employee employee = new Employee();
            employee.setId(this.reportedById);
            entity.setReportedBy(employee);
        }

        if (this.relatedReadingId != null) {
            FlowReading reading = new FlowReading();
            reading.setId(this.relatedReadingId);
            entity.setRelatedReading(reading);
        }

        if (this.relatedAlertId != null) {
            FlowAlert alert = new FlowAlert();
            alert.setId(this.relatedAlertId);
            entity.setRelatedAlert(alert);
        }

        if (this.statusId != null) {
            EventStatus status = new EventStatus();
            status.setId(this.statusId);
            entity.setStatus(status);
        }
    }

    /**
     * Converts a FlowEvent entity to its DTO representation.
     *
     * @param entity the FlowEvent entity to convert
     * @return FlowEventDTO or null if entity is null
     */
    public static FlowEventDTO fromEntity(FlowEvent entity) {
        if (entity == null) return null;

        return FlowEventDTO.builder()
                .id(entity.getId())
                .eventTimestamp(entity.getEventTimestamp())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .actionTaken(entity.getActionTaken())
                .impactOnFlow(entity.getImpactOnFlow())

                .severityId(entity.getSeverity() != null ? entity.getSeverity().getId() : null)
                .infrastructureId(entity.getInfrastructure() != null ? entity.getInfrastructure().getId() : null)
                .reportedById(entity.getReportedBy() != null ? entity.getReportedBy().getId() : null)
                .relatedReadingId(entity.getRelatedReading() != null ? entity.getRelatedReading().getId() : null)
                .relatedAlertId(entity.getRelatedAlert() != null ? entity.getRelatedAlert().getId() : null)
                .statusId(entity.getStatus() != null ? entity.getStatus().getId() : null)

                .severity(entity.getSeverity() != null ? SeverityDTO.fromEntity(entity.getSeverity()) : null)
                .infrastructure(entity.getInfrastructure() != null ? InfrastructureDTO.fromEntity(entity.getInfrastructure()) : null)
                .reportedBy(entity.getReportedBy() != null ? EmployeeDTO.fromEntity(entity.getReportedBy()) : null)
                .relatedReading(entity.getRelatedReading() != null ? FlowReadingDTO.fromEntity(entity.getRelatedReading()) : null)
                .relatedAlert(entity.getRelatedAlert() != null ? FlowAlertDTO.fromEntity(entity.getRelatedAlert()) : null)
                .status(entity.getStatus() != null ? EventStatusDTO.fromEntity(entity.getStatus()) : null)
                .build();
    }
}
