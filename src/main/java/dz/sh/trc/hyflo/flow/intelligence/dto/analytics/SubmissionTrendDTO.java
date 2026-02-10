/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: SubmissionTrendDTO
 * 	@CreatedOn	: 02-07-2026
 *
 * 	@Type		: Class (DTO)
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 * 	@Description: DTO for submission trends in operational monitoring
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
@Schema(description = "Submission trend data grouped by time period")
public class SubmissionTrendDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("period")
    @Schema(description = "Time period (format depends on groupBy: HOUR='2026-02-07 14:00:00', DAY='2026-02-07', WEEK='2026-06', MONTH='2026-02')", 
            example = "2026-02-07")
    private String period;

    @JsonProperty("submissionCount")
    @Schema(description = "Number of submissions in this period", example = "45")
    private Long submissionCount;

    @JsonProperty("uniquePipelines")
    @Schema(description = "Number of unique pipelines with submissions", example = "30")
    private Long uniquePipelines;

    @JsonProperty("averageSubmissionsPerPipeline")
    @Schema(description = "Average number of submissions per pipeline", example = "1.50")
    private Double averageSubmissionsPerPipeline;
}
