/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: TimelineItemDTO
 * 	@CreatedOn	: 02-14-2026
 * 	@UpdatedOn	: 02-14-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Timeline Item DTO
 * Represents a single item (alert or event) in the unified pipeline timeline
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "A single timeline item (alert or event)")
public class TimelineItemDTO {

    @Schema(description = "Unique identifier", example = "1", required = true)
    private Long id;

    @Schema(description = "Item type", example = "ALERT", required = true,
            allowableValues = {"ALERT", "EVENT", "OPERATION"})
    private String type;

    @Schema(description = "Severity level", example = "WARNING",
            allowableValues = {"CRITICAL", "WARNING", "INFO", "NORMAL"})
    private String severity;

    @Schema(description = "Title/summary of the item", example = "Pressure spike detected", required = true)
    private String title;

    @Schema(description = "Detailed description", example = "Pressure exceeded threshold by 5 bar")
    private String description;

    @Schema(description = "Timestamp when the item occurred", example = "2026-02-14T13:45:00", required = true)
    private LocalDateTime timestamp;

    @Schema(description = "Current status", example = "RESOLVED",
            allowableValues = {"ACTIVE", "RESOLVED", "ACKNOWLEDGED", "CLOSED", "PENDING"})
    private String status;

    @Schema(description = "Related pipeline ID", example = "1")
    private Long pipelineId;

    @Schema(description = "Related pipeline name", example = "GT-2023-A")
    private String pipelineName;

    @Schema(description = "User/operator who created or handled the item", example = "John Smith")
    private String operatorName;

    @Schema(description = "Additional metadata or context")
    private String metadata;

    @Schema(description = "Category or classification", example = "THRESHOLD_VIOLATION")
    private String category;

    @Schema(description = "Whether the item requires action", example = "false")
    private Boolean requiresAction;

    @Schema(description = "Link to related entity (alert, event, operation)", example = "/api/v1/flow/core/alerts/1")
    private String detailsUrl;

    @Schema(description = "Resolved timestamp (if applicable)", example = "2026-02-14T14:30:00")
    private LocalDateTime resolvedAt;

    @Schema(description = "Resolution notes")
    private String resolutionNotes;
}
