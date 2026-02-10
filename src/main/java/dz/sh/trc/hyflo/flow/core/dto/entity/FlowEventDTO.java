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

package dz.sh.trc.hyflo.flow.core.dto.entity;

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

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Flow event DTO for operational events and incidents")
public class FlowEventDTO extends GenericDTO<FlowEvent> {

    @NotNull(message = "Event timestamp is required")
    @PastOrPresent(message = "Event timestamp cannot be in the future")
    @Schema(description = "Timestamp when event occurred", example = "2026-01-22T03:15:00", required = true)
    private LocalDateTime eventTimestamp;

    @NotBlank(message = "Event title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Schema(description = "Brief event title", example = "Emergency Shutdown - Pipeline P-101", required = true, minLength = 3, maxLength = 100)
    private String title;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    @Schema(description = "Detailed event description", example = "Automatic shutdown triggered", maxLength = 2000)
    private String description;

    @PastOrPresent(message = "Start time cannot be in the future")
    @Schema(description = "Event start timestamp", example = "2026-01-22T03:15:00")
    private LocalDateTime startTime;

    @Schema(description = "Event end timestamp", example = "2026-01-22T05:30:00")
    private LocalDateTime endTime;

    @Size(max = 2000, message = "Action taken must not exceed 2000 characters")
    @Schema(description = "Corrective action description", example = "Isolated segment, deployed repair crew", maxLength = 2000)
    private String actionTaken;

    @NotNull(message = "Impact on flow flag is required")
    @Schema(description = "Did this event impact flow operations", example = "true", required = true)
    private Boolean impactOnFlow;

    // Foreign Key IDs
    @Schema(description = "Severity ID")
    private Long severityId;

    @NotNull(message = "Infrastructure is required")
    @Schema(description = "Infrastructure ID", required = true)
    private Long infrastructureId;

    @NotNull(message = "Reporter is required")
    @Schema(description = "Reported by employee ID", required = true)
    private Long reportedById;

    @Schema(description = "Related flow reading ID")
    private Long relatedReadingId;

    @Schema(description = "Related alert ID")
    private Long relatedAlertId;

    @Schema(description = "Event status ID")
    private Long statusId;

    // Nested DTOs
    @Schema(description = "Severity details")
    private SeverityDTO severity;

    @Schema(description = "Infrastructure details")
    private InfrastructureDTO infrastructure;

    @Schema(description = "Reporter employee details")
    private EmployeeDTO reportedBy;

    @Schema(description = "Related flow reading details")
    private FlowReadingDTO relatedReading;

    @Schema(description = "Related alert details")
    private FlowAlertDTO relatedAlert;

    @Schema(description = "Event status details")
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
