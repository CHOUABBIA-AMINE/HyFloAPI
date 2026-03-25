/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentReadDto
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.dto.query;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Read DTO for a pipeline incident event")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentReadDto {

    @Schema(description = "Technical identifier of the incident")
    private Long id;

    @Schema(description = "Incident type code", example = "LEAK")
    private String incidentTypeCode;

    @Schema(description = "Incident severity level", example = "HIGH")
    private String severityLevel;

    @Schema(description = "Incident status", example = "OPEN")
    private String status;

    @Schema(description = "Short description of the incident")
    private String description;

    @Schema(description = "Timestamp when the incident was declared")
    private LocalDateTime declaredAt;

    @Schema(description = "Timestamp when the incident was resolved")
    private LocalDateTime resolvedAt;

    @Schema(description = "ID of the affected pipeline")
    private Long pipelineId;

    @Schema(description = "Code of the affected pipeline", example = "GZ1-HASSI-ARZEW")
    private String pipelineCode;

    @Schema(description = "ID of the affected pipeline segment")
    private Long pipelineSegmentId;

    @Schema(description = "Code of the affected pipeline segment", example = "GZ1-SEG-02")
    private String pipelineSegmentCode;

    @Schema(description = "ID of the employee who declared the incident")
    private Long declaredById;

    @Schema(description = "Full name of the employee who declared the incident")
    private String declaredByFullName;

    // ---- NO fromEntity / mapping logic ----
    // All mapping is owned by IncidentMapper.
}
