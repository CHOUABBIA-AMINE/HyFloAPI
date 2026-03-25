/**
 * 
 * 	@Author		: HyFlo v2 Model
 *
 * 	@Name		: FeatureSnapshot
 * 	@CreatedOn	: 03-25-2026
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Captures a feature vector used by AI models for anomaly, quality, or risk.
 * Strongly linked to readings or pipeline segments for explainability.
 */
@Schema(description = "Captured feature vector used by AI models for anomaly, quality or risk")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FeatureSnapshot")
@Table(name = "T_03_06_01")
public class FeatureSnapshot extends GenericModel {

    @Schema(
            description = "Flow reading associated with this feature vector (if applicable)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_01", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_06_01_FK_01"))
    private FlowReading reading;

    @Schema(
            description = "Derived flow reading associated with this feature vector (if applicable)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_02", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_06_01_FK_02"))
    private DerivedFlowReading derivedReading;

    @Schema(
            description = "Pipeline segment associated with this feature vector (if applicable)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_03", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_06_01_FK_03"))
    private PipelineSegment pipelineSegment;

    @Schema(
            description = "AI model name or version used when generating this feature vector",
            example = "SEGMENT_RISK_FEATURES_V1",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @NotBlank(message = "Model name is mandatory")
    @Size(max = 100, message = "Model name must not exceed 100 characters")
    @Column(name = "F_04", length = 100, nullable = false)
    private String modelName;

    @Schema(
            description = "Timestamp when this feature vector was generated",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Generation timestamp is mandatory")
    @Column(name = "F_05", nullable = false)
    private LocalDateTime generatedAt;

    @Schema(
            description = "Serialized feature vector (e.g., JSON array)",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Feature vector is mandatory")
    @Column(name = "F_06", columnDefinition = "TEXT", nullable = false)
    private String featureVector;

    @Schema(
            description = "Logical schema version for this feature vector representation",
            example = "v1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 20
    )
    @Column(name = "F_07", length = 20)
    private String featureSchemaVersion;
}
