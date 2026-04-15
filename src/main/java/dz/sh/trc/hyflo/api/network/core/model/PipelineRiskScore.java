/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: PipelineRiskScore
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.api.network.core.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Stores a risk score computed for a pipeline segment by the risk engine.
 * Each record is a point-in-time snapshot enabling trend analysis.
 */
@Schema(description = "Risk score snapshot for a pipeline segment computed by the risk engine")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PipelineRiskScore")
@Table(name = "T_03_02_01")
public class PipelineRiskScore extends GenericModel {

    @Schema(description = "Composite risk score (0.0 - 1.0)", example = "0.74")
    @Column(name = "F_01", precision = 5, scale = 4, nullable = false)
    private BigDecimal riskScore;

    @Schema(description = "Pressure-related risk component (0.0 - 1.0)", example = "0.68")
    @Column(name = "F_02", precision = 5, scale = 4)
    private BigDecimal pressureRisk;

    @Schema(description = "Flow-related risk component (0.0 - 1.0)", example = "0.55")
    @Column(name = "F_03", precision = 5, scale = 4)
    private BigDecimal flowRisk;

    @Schema(description = "Age/corrosion risk component (0.0 - 1.0)", example = "0.80")
    @Column(name = "F_04", precision = 5, scale = 4)
    private BigDecimal ageCorrosionRisk;

    @Schema(description = "Name of the risk model used", example = "COMPOSITE_RISK_V1")
    @Column(name = "F_05", length = 100)
    private String modelName;

    @Schema(description = "Additional details or contributing factors")
    @Column(name = "F_06", length = 2000)
    private String details;

    @Schema(description = "Timestamp when the risk score was calculated")
    @Column(name = "F_07", nullable = false)
    private LocalDateTime calculatedAt;

    @Schema(description = "FK to the scored pipeline segment")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_02_01_FK_01"), nullable = false)
    private PipelineSegment pipelineSegment;
}
