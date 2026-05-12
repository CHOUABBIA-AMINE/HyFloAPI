/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowAlert
 * 	@CreatedOn	: 01-21-2026
 * 	@UpdatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Monitoring
 *
 **/

package dz.sh.trc.hyflo.core.flow.monitoring.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.core.flow.measurement.model.FlowReading;
import dz.sh.trc.hyflo.core.flow.reference.model.AlertStatus;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Automated alert triggered when flow readings breach defined thresholds.
 * Supports acknowledgment and resolution workflow.
 * FK to FlowThreshold now references flow.common.model.FlowThreshold.
 */
@Schema(description = "Automated alert for threshold breaches requiring operator attention and resolution")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FlowAlert")
@Table(
        name = "T_03_06_02",
        indexes = {
                @Index(name = "T_03_06_02_IX_01", columnList = "F_01"),
                @Index(name = "T_03_06_02_IX_02", columnList = "F_13"),
                @Index(name = "T_03_06_02_IX_03", columnList = "F_11, F_13")
        }
)
public class FlowAlert extends GenericModel {

    @Schema(description = "Timestamp when alert was triggered", example = "2026-01-22T00:45:00",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_01", nullable = false)
    private LocalDateTime alertTimestamp;

    @Schema(description = "Actual measured value that triggered the alert", example = "135.5",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_02", nullable = false)
    private BigDecimal actualValue;

    @Schema(description = "Threshold value that was breached", example = "120.0",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "F_03")
    private BigDecimal thresholdValue;

    @Schema(description = "Alert message describing the issue",
            example = "Pressure exceeded maximum threshold by 12.9%",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 1000)
    @Column(name = "F_04", length = 1000)
    private String message;

    @Schema(description = "Timestamp when alert was acknowledged by operator",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "F_05")
    private LocalDateTime acknowledgedAt;

    @Schema(description = "Timestamp when alert was resolved",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "F_06")
    private LocalDateTime resolvedAt;

    @Schema(description = "Notes describing the resolution action taken",
            example = "Adjusted valve to reduce pressure. System stabilized.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 1000)
    @Column(name = "F_07", length = 1000)
    private String resolutionNotes;

    @Schema(description = "Indicates if notification was sent to operators", example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_08", nullable = false)
    private Boolean notificationSent;

    @Schema(description = "Timestamp when notification was sent",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "F_09")
    private LocalDateTime notificationSentAt;

    @Schema(description = "Employee who resolved this alert",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_10", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_06_02_FK_04"))
    private Employee resolvedBy;

    @Schema(description = "Flow threshold (flow.common) that was breached",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_11", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_06_02_FK_01"), nullable = false)
    private FlowThreshold threshold;

    @Schema(description = "Flow reading that triggered this alert",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_12", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_06_02_FK_02"))
    private FlowReading flowReading;

    @Schema(description = "Current status of this alert (active, acknowledged, resolved)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_13", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_06_02_FK_03"))
    private AlertStatus status;

    @Schema(description = "Employee who acknowledged this alert",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_14", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_06_02_FK_05"))
    private Employee acknowledgedBy;
}
