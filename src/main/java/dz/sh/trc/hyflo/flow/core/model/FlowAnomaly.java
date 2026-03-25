/**
 * 
 * 	@Author		: HyFlo v2 Model
 *
 * 	@Name		: FlowAnomaly
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.common.model.DataSource;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
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
 * Persisted anomaly detection event on a flow reading or derived reading.
 *
 * Business rule:
 * - Either reading OR derivedReading MUST be non-null.
 * - Both null is invalid and must be prevented at the service/validation layer.
 */
@Schema(description = "Persisted anomaly detection event on a flow reading or derived reading")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FlowAnomaly")
@Table(name = "T_03_03_09")
public class FlowAnomaly extends GenericModel {

    @Schema(
            description = "Direct reading associated with this anomaly (if applicable)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_01", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_09_FK_01"))
    private FlowReading reading;

    @Schema(
            description = "Derived reading associated with this anomaly (if applicable)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_02", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_09_FK_02"))
    private DerivedFlowReading derivedReading;

    @Schema(
            description = "Pipeline segment affected by this anomaly (if determinable)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_03", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_09_FK_03"))
    private PipelineSegment pipelineSegment;

    @Schema(
            description = "Timestamp when this anomaly was detected",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Detection time is mandatory")
    @Column(name = "F_04", nullable = false)
    private LocalDateTime detectedAt;

    @Schema(
            description = "Anomaly type classification (e.g., PRESSURE_SPIKE, FLOW_DROP)",
            example = "PRESSURE_SPIKE",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @NotBlank(message = "Anomaly type is mandatory")
    @Size(max = 100, message = "Anomaly type must not exceed 100 characters")
    @Column(name = "F_05", length = 100, nullable = false)
    private String anomalyType;

    @Schema(
            description = "Anomaly score produced by the AI model",
            example = "0.98",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_06", precision = 5, scale = 3)
    private BigDecimal score;

    @Schema(
            description = "AI model name or version that produced this anomaly",
            example = "FLOW_ANOMALY_V1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Size(max = 100, message = "Model name must not exceed 100 characters")
    @Column(name = "F_07", length = 100)
    private String modelName;

    @Schema(
            description = "Human-readable explanation or feature attribution summary",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_08", columnDefinition = "TEXT")
    private String explanation;

    @Schema(
            description = "Validation status of this anomaly (e.g., PENDING_REVIEW, CONFIRMED, DISMISSED)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_09_FK_04"))
    private ValidationStatus status;

    @Schema(
            description = "Data source for this anomaly (AI assisted)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_10", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_09_FK_05"))
    private DataSource dataSource;
}
