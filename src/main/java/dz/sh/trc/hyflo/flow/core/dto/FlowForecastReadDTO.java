/**
 *
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : FlowForecastReadDTO
 *  @CreatedOn  : 01-23-2026
 *  @MovedOn    : 03-28-2026 — refactor: flow.core.dto → flow.intelligence.dto
 *
 *  @Type       : Class
 *  @Layer      : DTO (Read)
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.intelligence.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Read DTO for a flow forecast")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowForecastReadDTO {

    private Long id;
    private LocalDate forecastDate;
    private BigDecimal predictedVolume;
    private BigDecimal adjustedVolume;
    private BigDecimal actualVolume;
    private BigDecimal accuracy;
    private String adjustmentNotes;
    private Long infrastructureId;
    private String infrastructureName;
    private Long productId;
    private String productName;
    private Long operationTypeId;
    private String operationTypeName;
    private Long supervisorId;
    private String supervisorName;
}
