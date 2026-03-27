/**
 * 
 * 	@Author		: HyFlo v2 DTO
 *
 * 	@Name		: ForecastResultReadDTO
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-26-2026 — Task 2: add @Builder, @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Read DTO for forecast evaluation results.
 */
@Schema(description = "Read DTO for forecast evaluation results")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ForecastResultReadDTO {

    @Schema(description = "Technical identifier of the forecast result")
    private Long id;

    @Schema(description = "Identifier of the forecast being evaluated")
    private Long forecastId;

    @Schema(description = "Actual volume realized over the forecast period")
    private BigDecimal actualVolume;

    @Schema(description = "Absolute error between forecast and actual volume")
    private BigDecimal absoluteError;

    @Schema(description = "Percentage error between forecast and actual volume")
    private BigDecimal percentageError;

    @Schema(description = "Evaluation window or aggregation level (e.g., DAILY, WEEKLY, MONTHLY)")
    private String evaluationWindow;

    @Schema(description = "Timestamp when this evaluation was computed")
    private LocalDateTime evaluatedAt;
}
