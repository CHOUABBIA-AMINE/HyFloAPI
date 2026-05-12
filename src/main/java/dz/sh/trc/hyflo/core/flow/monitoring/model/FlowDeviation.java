/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : Notification
 * 	@CreatedOn	: 03-25-2026
 *  @UpdatedOn  : 04-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Model
 * 	@Package	: Flow / Monitoring
 *
 **/

package dz.sh.trc.hyflo.core.flow.monitoring.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import dz.sh.trc.hyflo.core.flow.measurement.model.FlowReading;
import dz.sh.trc.hyflo.core.flow.planning.model.FlowPlan;
import dz.sh.trc.hyflo.core.network.topology.model.Pipeline;
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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Deviation")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FlowDeviation")
@Table(
    name = "T_03_06_05",
    indexes = {
        @Index(name = "T_03_06_05_IX_01", columnList = "F_01"),
        @Index(name = "T_03_06_05_IX_02", columnList = "F_08"),
        @Index(name = "T_03_06_05_IX_03", columnList = "F_09")
    }
)
public class FlowDeviation extends GenericModel {

	@Schema(
        description = "date when deviation occurred",
        example = "2026-01-22T03:15:00",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Column(name = "F_01", nullable = false)
    private LocalDate date;

	@Schema(
        description = "Metric code of deviation",
        example = "2026-01-22T03:15:00",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Column(name = "F_02", nullable = false)
    private String metricCode;

	@Schema(
        description = "The measured value",
        example = "10.00",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Column(name = "F_03", nullable = false)
    private BigDecimal actualValue;

	@Schema(
        description = "The expected value",
        example = "11.30",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Column(name = "F_04", nullable = false)
    private BigDecimal expectedValue;

	@Schema(
        description = "expected value - actual value",
        example = "01.30",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Column(name = "F_05", nullable = false)
    private BigDecimal deviationValue;

	@Schema(
        description = "(expected value - actual value) / expected value",
        example = "01.30 / 11.30",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Column(name = "F_06", nullable = false)
    private BigDecimal deviationPercent;

	@Schema(
        description = "PLAN, THRESHOLD, HISTORICAL, ETC",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_07")
    private String baselineType; // PLAN / THRESHOLD / HISTORICAL

	@Schema(
            description = "Related pipeline",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_06_05_FK_01"))
    private Pipeline pipeline;

    @Schema(
            description = "Related flow reading",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_06_05_FK_02"))
    private FlowReading reading;

    @Schema(
            description = "Related flow plan",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_10", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_06_05_FK_03"))
    private FlowPlan plan;
}