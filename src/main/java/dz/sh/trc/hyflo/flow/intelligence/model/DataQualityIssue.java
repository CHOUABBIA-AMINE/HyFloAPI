/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: DataQualityIssue
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-28-2026 — refactor: moved from flow.core.model → flow.intelligence.model
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.core.model.DerivedFlowReading;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
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
 * Records a data quality issue flagged against a flow reading
 * by the smart validation engine.
 */
@Schema(description = "Data quality issue identified on a flow reading by the validation engine")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "DataQualityIssue")
@Table(name = "T_04_02_02")
public class DataQualityIssue extends GenericModel {

    @Schema(description = "Type of quality issue", example = "OUT_OF_RANGE")
    @Column(name = "F_01", length = 100, nullable = false)
    private String issueType;

    @Schema(description = "Quality score for the reading (0.0 - 1.0)", example = "0.43")
    @Column(name = "F_02", precision = 5, scale = 4)
    private BigDecimal qualityScore;

    @Schema(description = "Detailed explanation of the issue detected")
    @Column(name = "F_03", length = 2000)
    private String details;

    @Schema(description = "Whether the operator acknowledged the issue")
    @Column(name = "F_04")
    private Boolean acknowledged;

    @Schema(description = "Timestamp when the issue was raised")
    @Column(name = "F_05", nullable = false)
    private LocalDateTime raisedAt;

    @Schema(description = "FK to the source FlowReading")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_04_02_02_FK_01"))
    private FlowReading reading;

    @Schema(description = "FK to the source DerivedFlowReading (if applicable)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_04_02_02_FK_02"))
    private DerivedFlowReading derivedReading;
}
