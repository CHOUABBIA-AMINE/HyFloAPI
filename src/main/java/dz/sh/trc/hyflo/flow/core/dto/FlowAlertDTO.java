/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowAlertDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026 - Updated @Schema documentation and requiredMode syntax
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.common.dto.AlertStatusDTO;
import dz.sh.trc.hyflo.flow.common.model.AlertStatus;
import dz.sh.trc.hyflo.flow.core.model.FlowAlert;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for FlowAlert entity.
 * Used for API requests and responses related to threshold breach alerts.
 */
@Schema(description = "Flow alert DTO for threshold breach notifications and resolution tracking")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowAlertDTO extends GenericDTO<FlowAlert> {

    @Schema(
        description = "Timestamp when the alert was triggered by the system",
        example = "2026-01-22T00:45:00",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Alert timestamp is required")
    @PastOrPresent(message = "Alert timestamp cannot be in the future")
    private LocalDateTime alertTimestamp;

    @Schema(
        description = "Actual measured value that triggered the threshold breach",
        example = "135.5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Actual value is required")
    private BigDecimal actualValue;

    @Schema(
        description = "Threshold value that was breached",
        example = "120.0",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private BigDecimal thresholdValue;

    @Schema(
        description = "Alert message describing the threshold breach and potential impact",
        example = "Pressure exceeded maximum threshold by 12.9%",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 1000
    )
    @Size(max = 1000, message = "Alert message must not exceed 1000 characters")
    private String message;

    @Schema(
        description = "Timestamp when the alert was acknowledged by an operator",
        example = "2026-01-22T00:50:00",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Acknowledgment time cannot be in the future")
    private LocalDateTime acknowledgedAt;

    @Schema(
        description = "Timestamp when the alert was resolved",
        example = "2026-01-22T01:30:00",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Resolution time cannot be in the future")
    private LocalDateTime resolvedAt;

    @Schema(
        description = "Notes describing the resolution action taken to address the alert",
        example = "Adjusted valve to reduce pressure. System stabilized.",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 1000
    )
    @Size(max = 1000, message = "Resolution notes must not exceed 1000 characters")
    private String resolutionNotes;

    @Schema(
        description = "Indicates whether notification was sent to operators",
        example = "true",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Notification sent flag is required")
    private Boolean notificationSent;

    @Schema(
        description = "Timestamp when the notification was sent to operators",
        example = "2026-01-22T00:45:30",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Notification time cannot be in the future")
    private LocalDateTime notificationSentAt;

    // Foreign Key IDs
    @Schema(
        description = "ID of the employee who resolved this alert",
        example = "123",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long resolvedById;

    @Schema(
        description = "ID of the threshold that was breached",
        example = "456",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Threshold is required")
    private Long thresholdId;

    @Schema(
        description = "ID of the flow reading that triggered this alert",
        example = "789",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long flowReadingId;

    @Schema(
        description = "ID of the current alert status (active, acknowledged, resolved)",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long statusId;

    @Schema(
        description = "ID of the employee who acknowledged this alert",
        example = "234",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long acknowledgedById;

    // Nested DTOs
    @Schema(
        description = "Details of the employee who resolved this alert",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private EmployeeDTO resolvedBy;

    @Schema(
        description = "Details of the threshold that was breached",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private FlowThresholdDTO threshold;

    @Schema(
        description = "Details of the flow reading that triggered this alert",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private FlowReadingDTO flowReading;

    @Schema(
        description = "Details of the current alert status",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private AlertStatusDTO status;

    @Schema(
        description = "Details of the employee who acknowledged this alert",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private EmployeeDTO acknowledgedBy;

    @Override
    public FlowAlert toEntity() {
        FlowAlert entity = new FlowAlert();
        entity.setId(getId());
        entity.setAlertTimestamp(this.alertTimestamp);
        entity.setActualValue(this.actualValue);
        entity.setThresholdValue(this.thresholdValue);
        entity.setMessage(this.message);
        entity.setAcknowledgedAt(this.acknowledgedAt);
        entity.setResolvedAt(this.resolvedAt);
        entity.setResolutionNotes(this.resolutionNotes);
        entity.setNotificationSent(this.notificationSent);
        entity.setNotificationSentAt(this.notificationSentAt);

        if (this.resolvedById != null) {
            Employee employee = new Employee();
            employee.setId(this.resolvedById);
            entity.setResolvedBy(employee);
        }

        if (this.thresholdId != null) {
            FlowThreshold threshold = new FlowThreshold();
            threshold.setId(this.thresholdId);
            entity.setThreshold(threshold);
        }

        if (this.flowReadingId != null) {
            FlowReading reading = new FlowReading();
            reading.setId(this.flowReadingId);
            entity.setFlowReading(reading);
        }

        if (this.statusId != null) {
            AlertStatus status = new AlertStatus();
            status.setId(this.statusId);
            entity.setStatus(status);
        }

        if (this.acknowledgedById != null) {
            Employee employee = new Employee();
            employee.setId(this.acknowledgedById);
            entity.setAcknowledgedBy(employee);
        }

        return entity;
    }

    @Override
    public void updateEntity(FlowAlert entity) {
        if (this.alertTimestamp != null) entity.setAlertTimestamp(this.alertTimestamp);
        if (this.actualValue != null) entity.setActualValue(this.actualValue);
        if (this.thresholdValue != null) entity.setThresholdValue(this.thresholdValue);
        if (this.message != null) entity.setMessage(this.message);
        if (this.acknowledgedAt != null) entity.setAcknowledgedAt(this.acknowledgedAt);
        if (this.resolvedAt != null) entity.setResolvedAt(this.resolvedAt);
        if (this.resolutionNotes != null) entity.setResolutionNotes(this.resolutionNotes);
        if (this.notificationSent != null) entity.setNotificationSent(this.notificationSent);
        if (this.notificationSentAt != null) entity.setNotificationSentAt(this.notificationSentAt);

        if (this.resolvedById != null) {
            Employee employee = new Employee();
            employee.setId(this.resolvedById);
            entity.setResolvedBy(employee);
        }

        if (this.thresholdId != null) {
            FlowThreshold threshold = new FlowThreshold();
            threshold.setId(this.thresholdId);
            entity.setThreshold(threshold);
        }

        if (this.flowReadingId != null) {
            FlowReading reading = new FlowReading();
            reading.setId(this.flowReadingId);
            entity.setFlowReading(reading);
        }

        if (this.statusId != null) {
            AlertStatus status = new AlertStatus();
            status.setId(this.statusId);
            entity.setStatus(status);
        }

        if (this.acknowledgedById != null) {
            Employee employee = new Employee();
            employee.setId(this.acknowledgedById);
            entity.setAcknowledgedBy(employee);
        }
    }

    /**
     * Converts a FlowAlert entity to its DTO representation.
     *
     * @param entity the FlowAlert entity to convert
     * @return FlowAlertDTO or null if entity is null
     */
    public static FlowAlertDTO fromEntity(FlowAlert entity) {
        if (entity == null) return null;

        return FlowAlertDTO.builder()
                .id(entity.getId())
                .alertTimestamp(entity.getAlertTimestamp())
                .actualValue(entity.getActualValue())
                .thresholdValue(entity.getThresholdValue())
                .message(entity.getMessage())
                .acknowledgedAt(entity.getAcknowledgedAt())
                .resolvedAt(entity.getResolvedAt())
                .resolutionNotes(entity.getResolutionNotes())
                .notificationSent(entity.getNotificationSent())
                .notificationSentAt(entity.getNotificationSentAt())

                .resolvedById(entity.getResolvedBy() != null ? entity.getResolvedBy().getId() : null)
                .thresholdId(entity.getThreshold() != null ? entity.getThreshold().getId() : null)
                .flowReadingId(entity.getFlowReading() != null ? entity.getFlowReading().getId() : null)
                .statusId(entity.getStatus() != null ? entity.getStatus().getId() : null)
                .acknowledgedById(entity.getAcknowledgedBy() != null ? entity.getAcknowledgedBy().getId() : null)

                .resolvedBy(entity.getResolvedBy() != null ? EmployeeDTO.fromEntity(entity.getResolvedBy()) : null)
                .threshold(entity.getThreshold() != null ? FlowThresholdDTO.fromEntity(entity.getThreshold()) : null)
                .flowReading(entity.getFlowReading() != null ? FlowReadingDTO.fromEntity(entity.getFlowReading()) : null)
                .status(entity.getStatus() != null ? AlertStatusDTO.fromEntity(entity.getStatus()) : null)
                .acknowledgedBy(entity.getAcknowledgedBy() != null ? EmployeeDTO.fromEntity(entity.getAcknowledgedBy()) : null)
                .build();
    }
}
