/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: PendingValidationDTO
 * 	@CreatedOn	: 02-07-2026
 *
 * 	@Type		: Class (DTO)
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 * 	@Description: DTO for pending validation tracking in operational monitoring
 *                 Provides queue position and waiting time information
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
@Schema(description = "Pending validation with queue information")
public class PendingValidationDTO implements Serializable {

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

    @JsonProperty("submittedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Submission timestamp", example = "2026-02-07 08:30:00")
    private LocalDateTime submittedAt;

    @JsonProperty("waitingHours")
    @Schema(description = "Hours waiting for validation", example = "2.5")
    private Double waitingHours;

    @JsonProperty("priority")
    @Schema(description = "Validation priority (HIGH, MEDIUM, LOW)", example = "HIGH")
    private String priority;

    @JsonProperty("recordedBy")
    @Schema(description = "Employee who recorded the reading", example = "AHMED Mohamed")
    private String recordedBy;

    @JsonProperty("flowValue")
    @Schema(description = "Flow measurement value", example = "125.50")
    private Double flowValue;
}
