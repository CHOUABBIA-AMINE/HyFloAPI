/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowAlert
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-22-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.common.model.AlertStatus;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Automated alert triggered when flow readings breach defined thresholds.
 * Supports acknowledgment and resolution workflow.
 */
@Schema(description = "Automated alert for threshold breaches requiring operator attention and resolution")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FlowAlert")
@Table(name = "T_03_03_05", indexes = {@Index(name = "T_03_03_05_IX_01", columnList = "F_01"),
									   @Index(name = "T_03_03_05_IX_02", columnList = "F_13"),
									   @Index(name = "T_03_03_05_IX_03", columnList = "F_11, F_13")})
public class FlowAlert extends GenericModel {
    
	@Schema(
		description = "Timestamp when alert was triggered",
		example = "2026-01-22T00:45:00",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Alert timestamp is mandatory")
	@PastOrPresent(message = "Alert timestamp cannot be in the future")
	@Column(name = "F_01", nullable = false)
	private LocalDateTime alertTimestamp;
    
	@Schema(
		description = "Actual measured value that triggered the alert",
		example = "135.5",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Actual value is mandatory")
	@Column(name = "F_02", nullable = false)
	private BigDecimal actualValue;
    
	@Schema(
		description = "Threshold value that was breached",
		example = "120.0",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Column(name = "F_03")
	private BigDecimal thresholdValue;
    
	@Schema(
		description = "Alert message describing the issue",
		example = "Pressure exceeded maximum threshold by 12.9%",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 1000
	)
	@Size(max = 1000, message = "Alert message must not exceed 1000 characters")
	@Column(name = "F_04", length = 1000)
	private String message;
    
	@Schema(
		description = "Timestamp when alert was acknowledged by operator",
		example = "2026-01-22T00:50:00",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PastOrPresent(message = "Acknowledgment time cannot be in the future")
	@Column(name = "F_05")
	private LocalDateTime acknowledgedAt;
    
	@Schema(
		description = "Timestamp when alert was resolved",
		example = "2026-01-22T01:30:00",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PastOrPresent(message = "Resolution time cannot be in the future")
	@Column(name = "F_06")
	private LocalDateTime resolvedAt;
    
	@Schema(
		description = "Notes describing the resolution action taken",
		example = "Adjusted valve to reduce pressure. System stabilized.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 1000
	)
	@Size(max = 1000, message = "Resolution notes must not exceed 1000 characters")
	@Column(name = "F_07", length = 1000)
	private String resolutionNotes;
    
	@Schema(
		description = "Indicates if notification was sent to operators",
		example = "true",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Notification sent flag is mandatory")
	@Column(name = "F_08", nullable = false)
	private Boolean notificationSent;
    
	@Schema(
		description = "Timestamp when notification was sent",
		example = "2026-01-22T00:45:30",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PastOrPresent(message = "Notification time cannot be in the future")
	@Column(name = "F_09")
	private LocalDateTime notificationSentAt;
    
	@Schema(
		description = "Employee who resolved this alert",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name = "F_10", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_05_FK_04"))
	private Employee resolvedBy;
    
	@Schema(
		description = "Flow threshold that was breached",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Threshold reference is mandatory")
	@ManyToOne
	@JoinColumn(name = "F_11", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_05_FK_01"), nullable = false)
	private FlowThreshold threshold;
    
	@Schema(
		description = "Flow reading that triggered this alert",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name = "F_12", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_05_FK_02"))
	private FlowReading flowReading;
    
	@Schema(
		description = "Current status of this alert (active, acknowledged, resolved)",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name = "F_13", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_05_FK_03"))
	private AlertStatus status;
    
	@Schema(
		description = "Employee who acknowledged this alert",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name = "F_14", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_05_FK_05"))
	private Employee acknowledgedBy;
}
