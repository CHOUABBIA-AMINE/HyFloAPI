/**
 * 
 * 	@Author		: HyFlo v2 DTO
 *
 * 	@Name		: IncidentReadDto
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Read DTO for incidents.
 */
@Schema(description = "Read DTO for incidents")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IncidentReadDto {

    @Schema(description = "Technical identifier of the incident")
    private Long id;

    @Schema(description = "Human-readable unique incident code")
    private String code;

    @Schema(description = "Short title describing the incident")
    private String title;

    @Schema(description = "Detailed description of the incident")
    private String description;

    @Schema(description = "Identifier of the severity reference (if any)")
    private Long severityId;

    @Schema(description = "Identifier of the event status reference (if any)")
    private Long statusId;

    @Schema(description = "Identifier of the primary infrastructure asset (if any)")
    private Long infrastructureId;

    @Schema(description = "Identifier of the primary pipeline segment (if any)")
    private Long pipelineSegmentId;

    @Schema(description = "Identifier of the root flow event (if any)")
    private Long rootEventId;

    @Schema(description = "Identifier of the root flow alert (if any)")
    private Long rootAlertId;

    @Schema(description = "Timestamp when this incident started")
    private LocalDateTime startedAt;

    @Schema(description = "Timestamp when this incident ended")
    private LocalDateTime endedAt;

    @Schema(description = "Indicates whether this incident is still active")
    private Boolean active;
}
