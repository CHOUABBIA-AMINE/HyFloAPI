/**
 *
 * 	@Author		: HyFlo v2 Model
 *
 * 	@Name		: ForecastResult
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
 * Evaluation result comparing forecasted vs actual flow volumes.
 */
@Schema(description = "Evaluation result comparing forecasted vs actual flow volumes")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ForecastResult")
@Table(name = "T_03_03_11")
public class ForecastResult extends GenericModel {

    @Schema(description = "Flow forecast being evaluated",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_01", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_11_FK_01"), nullable = false)
    private FlowForecast forecast;

    @Schema(description = "Actual volume realized over the forecast period", example = "27250.50",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "F_02", precision = 15, scale = 4)
    private BigDecimal actualVolume;

    @Schema(description = "Absolute error between forecast and actual volume", example = "250.50",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "F_03", precision = 15, scale = 4)
    private BigDecimal absoluteError;

    @Schema(description = "Percentage error between forecast and actual volume", example = "0.92",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "F_04", precision = 6, scale = 4)
    private BigDecimal percentageError;

    @Schema(description = "Evaluation window or aggregation level (e.g., DAILY, WEEKLY, MONTHLY)",
            example = "DAILY", requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 20)
    @Column(name = "F_05", length = 20)
    private String evaluationWindow;

    @Schema(description = "Timestamp when this evaluation was computed",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_06", nullable = false)
    private LocalDateTime evaluatedAt;
}
