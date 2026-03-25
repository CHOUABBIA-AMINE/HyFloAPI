/**
 * 
 * 	@Author		: HyFlo v2 Model
 *
 * 	@Name		: PipelineRiskScore
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Risk score associated with a specific pipeline segment for a given time window.
 */
@Schema(description = "Risk score associated with a pipeline segment")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PipelineRiskScore")
@Table(name = "T_02_03_10")
public class PipelineRiskScore extends GenericModel {

    @Schema(
            description = "Pipeline segment this risk score applies to",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Pipeline segment is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_01", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_02_03_10_FK_01"), nullable = false)
    private PipelineSegment pipelineSegment;

    @Schema(
            description = "Timestamp when this risk score was calculated",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Calculation timestamp is mandatory")
    @Column(name = "F_02", nullable = false)
    private LocalDateTime calculatedAt;

    @Schema(
            description = "Timestamp until which this risk score is considered valid",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_03")
    private LocalDateTime validUntil;

    @Schema(
            description = "Risk score (0-100, higher means higher risk)",
            example = "82.5",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Risk score is mandatory")
    @Column(name = "F_04", precision = 5, scale = 2, nullable = false)
    private BigDecimal riskScore;

    @Schema(
            description = "Name or version of the model used to compute this score",
            example = "SEGMENT_RISK_V1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_05", length = 100)
    private String modelName;

    @Schema(
            description = "Optional details about the inputs or assumptions used",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_06", columnDefinition = "TEXT")
    private String details;
}
