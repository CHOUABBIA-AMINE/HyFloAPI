/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: ForecastResultReadDTO
 * 	@CreatedOn	: 03-28-2026 — extracted from flow.core.dto, relocated to flow.intelligence.dto
 *
 * 	@Type		: Class
 * 	@Layer		: DTO (Read)
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Read DTO for forecast evaluation results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForecastResultReadDTO {

    @Schema(description = "Record identifier")
    private Long id;

    @Schema(description = "Forecast identifier being evaluated")
    private Long forecastId;

    @Schema(description = "Actual volume realized over the forecast period", example = "27250.50")
    private BigDecimal actualVolume;

    @Schema(description = "Absolute error between forecast and actual volume", example = "250.50")
    private BigDecimal absoluteError;

    @Schema(description = "Percentage error between forecast and actual volume", example = "0.92")
    private BigDecimal percentageError;

    @Schema(description = "Evaluation window or aggregation level", example = "DAILY")
    private String evaluationWindow;

    @Schema(description = "Timestamp when this evaluation was computed")
    private LocalDateTime evaluatedAt;
}
