/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ForecastResultReadDTO
 *  @CreatedOn  : 03-25-2026
 *  @MovedOn    : 03-28-2026 — refactor: flow.core.dto → flow.intelligence.dto
 *
 *  @Type       : Class
 *  @Layer      : DTO (Read)
 *  @Package    : Flow / Intelligence
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

    private Long id;
    private Long forecastId;
    private BigDecimal actualVolume;
    private BigDecimal absoluteError;
    private BigDecimal percentageError;
    private String evaluationWindow;
    private LocalDateTime evaluatedAt;
}
