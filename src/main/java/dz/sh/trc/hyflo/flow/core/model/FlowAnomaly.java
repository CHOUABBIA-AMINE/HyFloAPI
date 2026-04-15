/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowAnomaly
 * 	@CreatedOn	: 03-25-2026
 * 	@MovedOn	: 03-28-2026 — refactor: flow.core.model → flow.intelligence.model
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.network.core.model.PipelineSegment;
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
 * Records an anomaly detected on a flow reading or derived reading
 * by the intelligence engine (statistical or ML model).
 */
@Schema(description = "Anomaly detected on a flow reading by the intelligence engine")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FlowAnomaly")
@Table(name = "T_03_05_03")
public class FlowAnomaly extends GenericModel {

    @Schema(description = "Type of anomaly detected", example = "PRESSURE_DROP")
    @Column(name = "F_01", length = 100, nullable = false)
    private String anomalyType;

    @Schema(description = "Anomaly severity score (0.0 - 1.0)", example = "0.87")
    @Column(name = "F_02", precision = 5, scale = 4)
    private BigDecimal severityScore;

    @Schema(description = "Confidence score of the detection model (0.0 - 1.0)", example = "0.92")
    @Column(name = "F_03", precision = 5, scale = 4)
    private BigDecimal confidenceScore;

    @Schema(description = "Name of the detection model used", example = "ISOLATION_FOREST_V2")
    @Column(name = "F_04", length = 100)
    private String modelName;

    @Schema(description = "Human-readable explanation of the anomaly")
    @Column(name = "F_05", length = 2000)
    private String explanation;

    @Schema(description = "Timestamp when anomaly was detected")
    @Column(name = "F_06", nullable = false)
    private LocalDateTime detectedAt;

    @Schema(description = "FK to the source FlowReading")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_05_03_FK_01"))
    private FlowReading reading;

    @Schema(description = "FK to the source DerivedFlowReading (if applicable)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_05_03_FK_02"))
    private DerivedFlowReading derivedReading;

    @Schema(description = "FK to the affected pipeline segment")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_05_03_FK_03"))
    private PipelineSegment pipelineSegment;
}
