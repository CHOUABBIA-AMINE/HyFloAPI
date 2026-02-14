/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineTimelineDTO
 * 	@CreatedOn	: 02-14-2026
 * 	@UpdatedOn	: 02-14-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pipeline Timeline DTO
 * Unified timeline combining alerts and events with pagination and statistics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Unified timeline of pipeline alerts and events with pagination")
public class PipelineTimelineDTO {

    @Schema(description = "Timeline items (alerts and events merged)", required = true)
    private List<TimelineItemDTO> items;

    @Schema(description = "Distribution of items by severity", 
            example = "{\"CRITICAL\": 2, \"WARNING\": 5, \"INFO\": 12}")
    private Map<String, Integer> severityCounts;

    @Schema(description = "Distribution of items by type", 
            example = "{\"ALERT\": 7, \"EVENT\": 12}")
    private Map<String, Integer> typeCounts;

    @Schema(description = "Total number of items in timeline", example = "19")
    private Integer totalItems;

    @Schema(description = "Current page number", example = "0")
    private Integer currentPage;

    @Schema(description = "Items per page", example = "20")
    private Integer pageSize;

    @Schema(description = "Total number of pages", example = "1")
    private Integer totalPages;

    @Schema(description = "Whether there are more pages", example = "false")
    private Boolean hasNext;

    @Schema(description = "Whether there is a previous page", example = "false")
    private Boolean hasPrevious;
}
