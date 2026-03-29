/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 *  @Name       : IncidentMapper
 *  @CreatedOn  : 03-25-2026
 * 	@UpdatedOn	: 03-29-2026
 *
 *  @Type       : Class
 *  @Layer      : Mapper
 *  @Package    : Crisis /Core
 *
 **/

package dz.sh.trc.hyflo.crisis.core.mapper;

import dz.sh.trc.hyflo.crisis.core.dto.query.IncidentReadDTO;
import dz.sh.trc.hyflo.crisis.core.model.Incident;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;

/**
 * Stateless mapper for {@link Incident} → {@link IncidentReadDTO}.
 *
 * <p>Mapping rules:
 * <ul>
 *   <li>All relation traversals are null-safe (segment, segment.pipeline).</li>
 *   <li>{@code status} is derived: "RESOLVED" when {@code resolvedAt != null}, else "ACTIVE".</li>
 *   <li>{@code declaredById} and {@code declaredByFullName} are always null until
 *       an Employee FK is added to the Incident entity.</li>
 * </ul>
 */
public final class IncidentMapper {

    private IncidentMapper() {}

    /**
     * Maps an {@link Incident} entity to its read DTO.
     *
     * @param entity the Incident to map; returns null when entity is null
     * @return populated {@link IncidentReadDTO}
     */
    public static IncidentReadDTO toReadDTO(Incident entity) {
        if (entity == null) return null;

        PipelineSegment segment = entity.getPipelineSegment();
        Pipeline pipeline = (segment != null) ? segment.getPipeline() : null;

        // Derive status from resolvedAt and active flag.
        // "RESOLVED" when resolvedAt is set; "ACTIVE" otherwise.
        String status = (entity.getResolvedAt() != null) ? "RESOLVED" : "ACTIVE";

        return IncidentReadDTO.builder()
                .id(entity.getId())

                // incidentCode ← Incident.code (F_01)
                .incidentCode(entity.getCode())

                // title ← Incident.title (F_02)
                .title(entity.getTitle())

                // severityLevel ← Incident.severity.code (lazy, null-safe)
                .severityLevel(entity.getSeverity() != null
                        ? entity.getSeverity().getCode() : null)

                // status derived from resolvedAt / active
                .status(status)

                // description ← Incident.description (F_03)
                .description(entity.getDescription())

                // occurredAt ← Incident.occurredAt (F_04) — NOT a declaration timestamp
                .occurredAt(entity.getOccurredAt())

                // resolvedAt ← Incident.resolvedAt (F_05) — null when still active
                .resolvedAt(entity.getResolvedAt())

                // pipelineSegment chain (null-safe)
                .pipelineSegmentId(segment != null ? segment.getId() : null)
                .pipelineSegmentCode(segment != null ? segment.getCode() : null)

                // pipeline chain via segment.getPipeline() (null-safe)
                .pipelineId(pipeline != null ? pipeline.getId() : null)
                .pipelineCode(pipeline != null ? pipeline.getCode() : null)

                // RESERVED — no Employee FK on Incident yet
                .declaredById(null)
                .declaredByFullName(null)

                .build();
    }
}
