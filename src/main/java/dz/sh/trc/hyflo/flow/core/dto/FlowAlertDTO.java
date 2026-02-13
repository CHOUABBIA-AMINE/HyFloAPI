/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowAlertDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
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

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Flow alert DTO for threshold breach notifications")
public class FlowAlertDTO extends GenericDTO<FlowAlert> {

    @NotNull(message = "Alert timestamp is required")
    @PastOrPresent(message = "Alert timestamp cannot be in the future")
    @Schema(description = "Alert trigger timestamp", 
    		example = "2026-01-22T00:45:00", 
    		requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime alertTimestamp;

    @NotNull(message = "Actual value is required")
    @Schema(description = "Actual measured value", 
    		example = "135.5", 
    		requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal actualValue;

    @Schema(description = "Threshold value breached", example = "120.0")
    private BigDecimal thresholdValue;

    @Size(max = 1000, message = "Alert message must not exceed 1000 characters")
    @Schema(description = "Alert message", 
    		example = "Pressure exceeded maximum", 
    		maxLength = 1000,
    		requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String message;

    @PastOrPresent(message = "Acknowledgment time cannot be in the future")
    @Schema(description = "Acknowledgment timestamp", 
		    example = "2026-01-22T00:50:00",
		    requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime acknowledgedAt;

    @PastOrPresent(message = "Resolution time cannot be in the future")
    @Schema(description = "Resolution timestamp", 
    		example = "2026-01-22T01:30:00",
    		requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime resolvedAt;

    @Size(max = 1000, message = "Resolution notes must not exceed 1000 characters")
    @Schema(description = "Resolution notes", example = "Adjusted valve", maxLength = 1000)
    private String resolutionNotes;

    @NotNull(message = "Notification sent flag is required")
    @Schema(description = "Notification sent", 
    		example = "true", 
    		requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean notificationSent;

    @PastOrPresent(message = "Notification time cannot be in the future")
    @Schema(description = "Notification timestamp", 
    		example = "2026-01-22T00:45:30", 
    		requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime notificationSentAt;

    @Schema(description = "Resolved by employee ID")
    private Long resolvedById;

    @NotNull(message = "Threshold is required")
    @Schema(description = "Threshold ID", 
    		requiredMode = Schema.RequiredMode.REQUIRED)
    private Long thresholdId;

    @Schema(description = "Flow reading ID", 
    		requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long flowReadingId;

    @Schema(description = "Alert status ID", 
    		requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long statusId;

    @Schema(description = "Acknowledged by employee ID", 
    		requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long acknowledgedById;

    // Nested DTOs
    @Schema(description = "Resolved by employee details")
    private EmployeeDTO resolvedBy;

    @Schema(description = "Threshold details")
    private FlowThresholdDTO threshold;

    @Schema(description = "Flow reading details")
    private FlowReadingDTO flowReading;

    @Schema(description = "Alert status details")
    private AlertStatusDTO status;

    @Schema(description = "Acknowledged by employee details")
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
