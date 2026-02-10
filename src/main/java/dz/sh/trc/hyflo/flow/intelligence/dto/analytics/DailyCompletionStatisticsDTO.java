/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: DailyCompletionStatisticsDTO
 * 	@CreatedOn	: 02-07-2026
 *
 * 	@Type		: Class (DTO)
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 * 	@Description: DTO for daily completion statistics in operational monitoring
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto.analytics;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Schema(description = "Daily completion statistics for flow readings")
public class DailyCompletionStatisticsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Date of the statistics", example = "2026-02-07")
    private LocalDate date;

    @JsonProperty("totalPipelines")
    @Schema(description = "Total number of pipelines in the structure", example = "50")
    private Long totalPipelines;

    @JsonProperty("recordedCount")
    @Schema(description = "Number of readings recorded", example = "45")
    private Long recordedCount;

    @JsonProperty("submittedCount")
    @Schema(description = "Number of readings submitted for validation", example = "40")
    private Long submittedCount;

    @JsonProperty("approvedCount")
    @Schema(description = "Number of readings approved/validated", example = "35")
    private Long approvedCount;

    @JsonProperty("rejectedCount")
    @Schema(description = "Number of readings rejected", example = "3")
    private Long rejectedCount;

    @JsonProperty("recordingCompletionPercentage")
    @Schema(description = "Percentage of recordings completed", example = "90.00")
    private Double recordingCompletionPercentage;

    @JsonProperty("validationCompletionPercentage")
    @Schema(description = "Percentage of validations completed", example = "76.00")
    private Double validationCompletionPercentage;
}
