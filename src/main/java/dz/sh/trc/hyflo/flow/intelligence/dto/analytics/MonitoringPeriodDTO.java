/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: MonitoringPeriodDTO
 * 	@CreatedOn	: 02-07-2026
 *
 * 	@Type		: Class (DTO)
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 * 	@Description: DTO for date range specification in monitoring queries
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto.analytics;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Date range for monitoring queries")
public class MonitoringPeriodDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Start date is required")
    @JsonProperty("startDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Start date of monitoring period", example = "2026-02-01", required = true)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonProperty("endDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "End date of monitoring period", example = "2026-02-28", required = true)
    private LocalDate endDate;

    @NotNull(message = "Structure ID is required")
    @JsonProperty("structureId")
    @Schema(description = "Structure ID to filter by", example = "1", required = true)
    private Long structureId;

    @JsonProperty("groupBy")
    @Schema(description = "Time grouping for trends (HOUR, DAY, WEEK, MONTH)", example = "DAY")
    private String groupBy;
}
