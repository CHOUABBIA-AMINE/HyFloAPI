/**
 * 
 * 	@Author		: HyFlo v2 Model
 *
 * 	@Name		: FeatureSnapshot
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.model;

import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.core.model.DerivedFlowReading;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Captures the feature vector used by an AI model when producing a prediction
 * (anomaly score, quality score, risk score, or forecast).
 *
 * <h3>AI traceability</h3>
 * Every AI result ({@code FlowAnomaly}, {@code DataQualityIssue},
 * {@code PipelineRiskScore}, {@code ForecastResult}) can reference a
 * {@code FeatureSnapshot} to allow auditors and operators to understand
 * exactly which input data the model used — a hard requirement for
 * industrial certification and operator trust.
 *
 * <h3>Explicit relations (no EAV)</h3>
 * The subject of the snapshot is identified through explicit FK relations:
 * a raw reading, a derived reading, or a pipeline segment.
 * At least one of the three must be non-null (enforced at service layer).
 */
@Schema(description = "Point-in-time feature vector captured for AI model traceability")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FeatureSnapshot")
@Table(
        name = "T_03_05_06",
        indexes = {
                @Index(name = "T_03_05_06_IX_01", columnList = "F_05")
        }
)
public class FeatureSnapshot extends GenericModel {

    // ------------------------------------------------------------------
    // F_01 — Source raw reading (optional)
    // ------------------------------------------------------------------

    @Schema(
            description = "Raw flow reading that was the subject of this feature extraction (if applicable)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_01", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_05_06_FK_01"))
    private FlowReading reading;

    // ------------------------------------------------------------------
    // F_02 — Source derived reading (optional)
    // ------------------------------------------------------------------

    @Schema(
            description = "Derived flow reading that was the subject of this feature extraction (if applicable)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_02", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_05_06_FK_02"))
    private DerivedFlowReading derivedReading;

    // ------------------------------------------------------------------
    // F_03 — Source pipeline segment (optional)
    // ------------------------------------------------------------------

    @Schema(
            description = "Pipeline segment that was the subject of this feature extraction (if applicable)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_03", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_05_06_FK_03"))
    private PipelineSegment pipelineSegment;

    // ------------------------------------------------------------------
    // F_04 — Model identifier
    // ------------------------------------------------------------------

    @Schema(
            description = "Name and version of the AI model that generated this feature vector",
            example = "SEGMENT_RISK_FEATURES_V1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_04", length = 100, nullable = false)
    private String modelName;

    // ------------------------------------------------------------------
    // F_05 — Generation timestamp (part of analytical index)
    // ------------------------------------------------------------------

    @Schema(
            description = "Timestamp when this feature vector was generated",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_05", nullable = false)
    private LocalDateTime generatedAt;

    // ------------------------------------------------------------------
    // F_06 — Serialized feature vector
    // ------------------------------------------------------------------

    @Schema(
            description = "Serialized feature vector (JSON array or JSON object) used as model input",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_06", columnDefinition = "TEXT", nullable = false)
    private String featureVector;

    // ------------------------------------------------------------------
    // F_07 — Feature schema version
    // ------------------------------------------------------------------

    @Schema(
            description = "Logical schema version of the feature vector format, for backwards compatibility",
            example = "v1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 20
    )
    @Column(name = "F_07", length = 20)
    private String featureSchemaVersion;
}
