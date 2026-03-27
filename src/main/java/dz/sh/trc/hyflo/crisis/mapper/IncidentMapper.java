/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentMapper
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.mapper;

import dz.sh.trc.hyflo.crisis.dto.query.IncidentReadDTO;
import dz.sh.trc.hyflo.crisis.model.Incident;

public final class IncidentMapper {

    private IncidentMapper() {}

    public static IncidentReadDTO toReadDTO(Incident entity) {
        if (entity == null) return null;

        return IncidentReadDTO.builder()
                .id(entity.getId())
                .incidentTypeCode(null)  // not on entity — reserved for future join
                .severityLevel(entity.getSeverity() != null
                        ? entity.getSeverity().getCode() : null)
                .status(null)           // not on entity — reserved for future field
                .description(entity.getDescription())
                .declaredAt(entity.getOccurredAt())
                .resolvedAt(entity.getResolvedAt())
                .pipelineId(null)       // not on entity — reserved for future join
                .pipelineCode(null)
                .pipelineSegmentId(entity.getPipelineSegment() != null
                        ? entity.getPipelineSegment().getId() : null)
                .pipelineSegmentCode(entity.getPipelineSegment() != null
                        ? entity.getPipelineSegment().getCode() : null)
                .declaredById(null)     // not on entity — reserved for future join
                .declaredByFullName(null)
                .build();
    }
}
