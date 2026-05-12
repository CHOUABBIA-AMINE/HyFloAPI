/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowThreshold
 * 	@CreatedOn	: 01-21-2026
 * 	@UpdatedOn	: 03-25-2026
 * 	@MovedOn	: 03-28-2026 — refactor: flow.core.model → flow.common.model
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Monitoring
 *
 **/

package dz.sh.trc.hyflo.core.flow.monitoring.model;

import java.math.BigDecimal;

import dz.sh.trc.hyflo.core.network.topology.model.Pipeline;
import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Operating thresholds for pipeline monitoring and alerting.
 * Defines safe operating ranges for pressure, temperature, and flow rates.
 * Moved to flow.common as a shared reference entity per HyFlo v2 architecture.
 */
@Schema(description = "Pipeline operating thresholds for monitoring and automated alerting")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FlowThreshold")
@Table(
        name = "T_03_06_01",
        uniqueConstraints = {
                @UniqueConstraint(name = "T_03_03_06_UK_01", columnNames = {"F_09", "F_10"})
        }
)
public class FlowThreshold extends GenericModel {

    @Schema(description = "Minimum safe pressure (bar)", example = "50.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_01", precision = 8, scale = 4, nullable = false)
    private BigDecimal pressureMin;

    @Schema(description = "Maximum safe pressure (bar)", example = "120.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_02", precision = 8, scale = 4, nullable = false)
    private BigDecimal pressureMax;

    @Schema(description = "Minimum safe temperature (\u00b0C)", example = "5.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_03", precision = 8, scale = 4, nullable = false)
    private BigDecimal temperatureMin;

    @Schema(description = "Maximum safe temperature (\u00b0C)", example = "85.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_04", precision = 8, scale = 4, nullable = false)
    private BigDecimal temperatureMax;

    @Schema(description = "Minimum acceptable flow rate (m\u00b3/h or bpd)", example = "500.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_05", precision = 10, scale = 4, nullable = false)
    private BigDecimal flowRateMin;

    @Schema(description = "Maximum acceptable flow rate (m\u00b3/h or bpd)", example = "2000.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_06", precision = 10, scale = 4, nullable = false)
    private BigDecimal flowRateMax;

    @Schema(description = "Minimum acceptable contained volume (m\u00b3)", example = "500.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_07", precision = 15, scale = 4, nullable = false)
    private BigDecimal containedVolumeMin;

    @Schema(description = "Maximum acceptable contained volume (m\u00b3)", example = "40000.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_08", precision = 15, scale = 4, nullable = false)
    private BigDecimal containedVolumeMax;

    @Schema(description = "Alert tolerance percentage for threshold breaches (e.g., 5.0 for \u00b15%)",
            example = "5.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_09", precision = 7, scale = 4, nullable = false)
    private BigDecimal alertTolerance;

    @Schema(description = "Indicates if this threshold configuration is currently active",
            example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_10", nullable = false)
    private Boolean active;

    @Schema(description = "Pipeline to which these thresholds apply",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_11", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_06_01_FK_01"), nullable = false)
    private Pipeline pipeline;
}
