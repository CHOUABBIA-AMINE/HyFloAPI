/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: OverdueReadingDTO
 * 	@CreatedOn	: 02-07-2026
 *
 * 	@Type		: Class (DTO)
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 * 	@Description: DTO for overdue reading tracking in operational monitoring
 *                 Extends FlowReadingDTO with overdue-specific information
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto.monitoring;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Overdue reading with deadline information")
public class OverdueReadingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("readingId")
    @Schema(description = "Flow reading ID", example = "1")
    private Long readingId;

    @JsonProperty("pipelineCode")
    @Schema(description = "Pipeline code", example = "PIP-001")
    private String pipelineCode;

    @JsonProperty("pipelineName")
    @Schema(description = "Pipeline name", example = "Main Supply Line")
    private String pipelineName;

    @JsonProperty("readingSlot")
    @Schema(description = "Reading slot designation", example = "Morning Shift")
    private String readingSlot;

    @JsonProperty("recordedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Recording timestamp", example = "2026-02-05 08:30:00")
    private LocalDateTime recordedAt;

    @JsonProperty("deadline")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Validation deadline (slot end time)", example = "2026-02-05 12:00:00")
    private LocalDateTime deadline;

    @JsonProperty("hoursOverdue")
    @Schema(description = "Hours past deadline", example = "48.5")
    private Double hoursOverdue;

    @JsonProperty("validationStatus")
    @Schema(description = "Current validation status", example = "SUBMITTED")
    private String validationStatus;

    @JsonProperty("recordedBy")
    @Schema(description = "Employee who recorded the reading", example = "AHMED Mohamed")
    private String recordedBy;
}
