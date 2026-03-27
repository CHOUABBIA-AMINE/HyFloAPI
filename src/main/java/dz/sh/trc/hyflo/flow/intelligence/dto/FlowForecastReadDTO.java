/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowForecastReadDTO
 * 	@CreatedOn	: 03-28-2026 — extracted from flow.core.dto, relocated to flow.intelligence.dto
 *
 * 	@Type		: Class
 * 	@Layer		: DTO (Read)
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Read DTO for a flow forecast record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowForecastReadDTO {

    @Schema(description = "Record identifier")
    private Long id;

    @Schema(description = "Date for which forecast is made", example = "2026-01-25")
    private LocalDate forecastDate;

    @Schema(description = "Infrastructure identifier")
    private Long infrastructureId;

    @Schema(description = "Infrastructure name")
    private String infrastructureName;

    @Schema(description = "Product identifier")
    private Long productId;

    @Schema(description = "Product name")
    private String productName;

    @Schema(description = "Operation type identifier")
    private Long operationTypeId;

    @Schema(description = "Operation type name")
    private String operationTypeName;

    @Schema(description = "Predicted volume", example = "28000.00")
    private BigDecimal predictedVolume;

    @Schema(description = "Adjusted volume after expert review", example = "27500.00")
    private BigDecimal adjustedVolume;

    @Schema(description = "Actual volume realized", example = "27250.50")
    private BigDecimal actualVolume;

    @Schema(description = "Forecast accuracy percentage", example = "98.5")
    private BigDecimal accuracy;

    @Schema(description = "Notes explaining forecast adjustments")
    private String adjustmentNotes;

    @Schema(description = "Supervisor identifier")
    private Long supervisorId;

    @Schema(description = "Supervisor full name")
    private String supervisorName;
}
