/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowAlert
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-21-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Schema(description = "Flow alert entity tracking threshold violations with complete lifecycle from detection to resolution")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FlowAlert")
@Table(name = "T_03_03_03", indexes = {@Index(name = "T_03_03_03_IX_01", columnList = "F_01"),
        							   @Index(name = "T_03_03_03_IX_02", columnList = "F_07"),
        							   @Index(name = "T_03_03_03_IX_03", columnList = "F_02, F_07")})
public class FlowAlert extends GenericModel {
    
	@Schema(description = "Timestamp when the alert was generated", example = "2026-01-21T14:30:00", required = true)
    @NotNull(message = "Alert timestamp is required")
    @PastOrPresent(message = "Alert timestamp cannot be in the future")
    @Column(name = "F_01", nullable = false)
    private LocalDateTime alertTimestamp;
    
    @Schema(description = "Actual measured value that triggered the alert", example = "92.5", required = true)
    @NotNull(message = "Actual value is required")
    @Column(name = "F_04", nullable = false)
    private Double actualValue;
    
    @Schema(description = "Threshold value that was exceeded", example = "85.0")
    @Column(name = "F_05")
    private Double thresholdValue;
    
    @Schema(description = "Alert message describing the threshold violation", example = "Pressure exceeded maximum threshold of 85 bar (actual: 92.5 bar)")
    @Size(max = 1000, message = "Alert message cannot exceed 1000 characters")
    @Column(name = "F_06", length = 1000)
    private String message;
    
    @Schema(description = "Timestamp when the alert was acknowledged", example = "2026-01-21T14:35:00")
    @PastOrPresent(message = "Acknowledgment time cannot be in the future")
    @Column(name = "F_08")
    private LocalDateTime acknowledgedAt;
    
    @Schema(description = "Timestamp when the alert was resolved", example = "2026-01-21T15:00:00")
    @PastOrPresent(message = "Resolution time cannot be in the future")
    @Column(name = "F_10")
    private LocalDateTime resolvedAt;
    
    @Schema(description = "Employee who resolved the alert")
    @ManyToOne
    @JoinColumn(name = "F_11", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_03_FK_04"))
    private Employee resolvedBy;
    
    @Schema(description = "Notes describing how the alert was resolved", example = "Pressure stabilized after valve adjustment")
    @Size(max = 1000, message = "Resolution notes cannot exceed 1000 characters")
    @Column(name = "F_12", length = 1000)
    private String resolutionNotes;
    
    @Schema(description = "Whether notification was sent to operators", example = "true", required = true)
    @NotNull(message = "Notification sent flag is required")
    @Column(name = "F_13", nullable = false)
    private Boolean notificationSent;
    
    @Schema(description = "Timestamp when notification was sent", example = "2026-01-21T14:30:30")
    @PastOrPresent(message = "Notification time cannot be in the future")
    @Column(name = "F_14")
    private LocalDateTime notificationSentAt;
    
    @Schema(description = "Threshold configuration that was violated", required = true)
    @NotNull(message = "Threshold reference is required")
    @ManyToOne
    @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_03_FK_01"), nullable = false)
    private FlowThreshold threshold;
    
    @Schema(description = "Flow reading that triggered this alert")
    @ManyToOne
    @JoinColumn(name = "F_03", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_03_FK_02"))
    private FlowReading flowReading;
    
    @Schema(description = "Current status of the alert", example = "ACKNOWLEDGED", required = true, 
            allowableValues = {"NEW", "ACKNOWLEDGED", "INVESTIGATING", "RESOLVED", "FALSE_ALARM"})
    @NotBlank(message = "Alert status is required")
    @Pattern(regexp = "NEW|ACKNOWLEDGED|INVESTIGATING|RESOLVED|FALSE_ALARM", 
        message = "Status must be one of: NEW, ACKNOWLEDGED, INVESTIGATING, RESOLVED, FALSE_ALARM")
    @Size(max = 20, message = "Status cannot exceed 20 characters")
    @Column(name = "F_07", length = 20, nullable = false)
    private String status;
    
    @Schema(description = "Employee who acknowledged the alert")
    @ManyToOne
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_03_FK_03"))
    private Employee acknowledgedBy;
    
}
