/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: FlowAlertDTO
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-21-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowAlert;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class FlowAlertDTO extends GenericDTO<FlowAlert> {
    
    @NotNull(message = "Alert timestamp is required")
    private LocalDateTime alertTimestamp;
    
    @NotNull(message = "Threshold ID is required")
    private Long thresholdId;
    
    private FlowThresholdDTO threshold;
    
    private Long flowReadingId;
    private FlowReadingDTO flowReading;
    
    @NotNull(message = "Actual value is required")
    private Double actualValue;
    
    private Double thresholdValue;
    private String message;
    
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "NEW|ACKNOWLEDGED|INVESTIGATING|RESOLVED|FALSE_ALARM")
    private String status;
    
    private LocalDateTime acknowledgedAt;
    
    private Long acknowledgedById;
    private EmployeeDTO acknowledgedBy;
    
    private LocalDateTime resolvedAt;
    
    private Long resolvedById;
    private EmployeeDTO resolvedBy;
    
    private String resolutionNotes;
    
    @NotNull(message = "Notification sent flag is required")
    private Boolean notificationSent;
    
    private LocalDateTime notificationSentAt;
    
    @Override
    public FlowAlert toEntity() {
        FlowAlert entity = new FlowAlert();
        entity.setId(getId());
        entity.setAlertTimestamp(this.alertTimestamp);
        entity.setActualValue(this.actualValue);
        entity.setThresholdValue(this.thresholdValue);
        entity.setMessage(this.message);
        entity.setStatus(this.status);
        entity.setAcknowledgedAt(this.acknowledgedAt);
        entity.setResolvedAt(this.resolvedAt);
        entity.setResolutionNotes(this.resolutionNotes);
        entity.setNotificationSent(this.notificationSent);
        entity.setNotificationSentAt(this.notificationSentAt);
        return entity;
    }
    
    @Override
    public void updateEntity(FlowAlert entity) {
        if (this.alertTimestamp != null) {
            entity.setAlertTimestamp(this.alertTimestamp);
        }
        if (this.actualValue != null) {
            entity.setActualValue(this.actualValue);
        }
        if (this.thresholdValue != null) {
            entity.setThresholdValue(this.thresholdValue);
        }
        if (this.message != null) {
            entity.setMessage(this.message);
        }
        if (this.status != null) {
            entity.setStatus(this.status);
        }
        if (this.acknowledgedAt != null) {
            entity.setAcknowledgedAt(this.acknowledgedAt);
        }
        if (this.resolvedAt != null) {
            entity.setResolvedAt(this.resolvedAt);
        }
        if (this.resolutionNotes != null) {
            entity.setResolutionNotes(this.resolutionNotes);
        }
        if (this.notificationSent != null) {
            entity.setNotificationSent(this.notificationSent);
        }
        if (this.notificationSentAt != null) {
            entity.setNotificationSentAt(this.notificationSentAt);
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
                .status(entity.getStatus())
                .acknowledgedAt(entity.getAcknowledgedAt())
                .resolvedAt(entity.getResolvedAt())
                .resolutionNotes(entity.getResolutionNotes())
                .notificationSent(entity.getNotificationSent())
                .notificationSentAt(entity.getNotificationSentAt())
                .build();
    }
}
