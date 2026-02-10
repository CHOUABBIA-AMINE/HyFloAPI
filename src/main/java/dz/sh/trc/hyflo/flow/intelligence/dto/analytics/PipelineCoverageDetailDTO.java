/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineCoverageDetailDTO
 * 	@CreatedOn	: 02-07-2026
 *
 * 	@Type		: Class (DTO)
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 * 	@Description: DTO for detailed pipeline coverage analysis with missing dates
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto.analytics;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detailed pipeline coverage data with missing dates information")
public class PipelineCoverageDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("pipelineId")
    @Schema(description = "Pipeline ID", example = "1")
    private Long pipelineId;

    @JsonProperty("pipelineCode")
    @Schema(description = "Pipeline code", example = "PIP-001")
    private String pipelineCode;

    @JsonProperty("pipelineName")
    @Schema(description = "Pipeline name", example = "Main Supply Line")
    private String pipelineName;

    @JsonProperty("expectedReadings")
    @Schema(description = "Expected number of readings for date range", example = "30")
    private Long expectedReadings;

    @JsonProperty("actualReadings")
    @Schema(description = "Actual number of readings submitted", example = "27")
    private Long actualReadings;

    @JsonProperty("coveragePercentage")
    @Schema(description = "Coverage percentage", example = "90.00")
    private Double coveragePercentage;

    @JsonProperty("missingDates")
    @Schema(description = "Comma-separated list of dates with missing readings", example = "2026-02-05, 2026-02-10, 2026-02-15")
    private String missingDates;
}
