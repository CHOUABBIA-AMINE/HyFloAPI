/**
 * 
 * 	@Author		: HyFlo v2 Model
 *
 * 	@Name		: DataQualityIssue
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
import dz.sh.trc.hyflo.flow.common.model.QualityFlag;
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
 * Data quality evaluation associated with a reading or derived reading.
 */
@Schema(description = "Data quality evaluation associated with a reading or derived reading")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "DataQualityIssue")
@Table(name = "T_03_03_10")
public class DataQualityIssue extends GenericModel {

    @Schema(
            description = "Direct reading evaluated for data quality (if applicable)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_01", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_10_FK_01"))
    private FlowReading reading;

    @Schema(
            description = "Derived reading evaluated for data quality (if applicable)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_02", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_10_FK_02"))
    private DerivedFlowReading derivedReading;

    @Schema(
            description = "Quality flag assigned (OK, SUSPECT, BAD, etc.)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_03", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_10_FK_03"))
    private QualityFlag qualityFlag;

    @Schema(
            description = "Numerical quality score (0-1 or 0-100 depending on convention)",
            example = "0.85",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_04", precision = 5, scale = 3)
    private BigDecimal score;

    @Schema(
            description = "Classification of the data quality issue (e.g., MISSING_VALUE, OUTLIER, INCONSISTENT)",
            example = "OUTLIER",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_05", length = 100, nullable = false)
    private String issueType;

    @Schema(
            description = "Human-readable details about detected data quality issues",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_06", columnDefinition = "TEXT")
    private String details;

    @Schema(
            description = "Timestamp when data quality evaluation was performed",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Evaluation time is mandatory")
    @Column(name = "F_07", nullable = false)
    private LocalDateTime evaluatedAt;

    @Schema(
            description = "Data source for this quality evaluation (AI assisted)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_10_FK_04"))
    private DataSource dataSource;
}
