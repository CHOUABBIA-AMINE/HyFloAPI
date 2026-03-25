/**
 *
 * 	@Author		: HyFlo v2
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

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.crisis.model.Incident;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Read DTO for a transport incident")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentReadDto extends GenericDTO<Incident> {

    @Schema(description = "Unique incident code", example = "INC-2026-0012")
    private String code;

    @Schema(description = "Short title", example = "Pressure drop – GZ1 Seg 3")
    private String title;

    @Schema(description = "Full description")
    private String description;

    @Schema(description = "When the incident occurred")
    private LocalDateTime occurredAt;

    @Schema(description = "When the incident was resolved (null if ongoing)")
    private LocalDateTime resolvedAt;

    @Schema(description = "Whether the incident is still active")
    private Boolean active;

    @Schema(description = "ID of the severity level")
    private Long severityId;

    @Schema(description = "Severity code", example = "P1")
    private String severityCode;

    @Schema(description = "Severity label", example = "Critical")
    private String severityLabel;

    @Schema(description = "ID of the affected pipeline segment")
    private Long pipelineSegmentId;

    @Schema(description = "Code of the affected pipeline segment", example = "GZ1-SEG-01")
    private String pipelineSegmentCode;

    @Override
    public Incident toEntity() {
        throw new UnsupportedOperationException("Use IncidentCommandService for write operations");
    }

    @Override
    public void updateEntity(Incident entity) {
        throw new UnsupportedOperationException("Use IncidentCommandService for update operations");
    }

    public static IncidentReadDto fromEntity(Incident entity) {
        if (entity == null) return null;
        return IncidentReadDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .occurredAt(entity.getOccurredAt())
                .resolvedAt(entity.getResolvedAt())
                .active(entity.getActive())
                .severityId(entity.getSeverity() != null ? entity.getSeverity().getId() : null)
                .severityCode(entity.getSeverity() != null ? entity.getSeverity().getCode() : null)
                .severityLabel(entity.getSeverity() != null ? entity.getSeverity().getLabel() : null)
                .pipelineSegmentId(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getId() : null)
                .pipelineSegmentCode(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getCode() : null)
                .build();
    }
}
