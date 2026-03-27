/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentImpactMapper
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 - Corrected to map real IncidentImpact entity fields
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.mapper;

import java.util.stream.Collectors;

import dz.sh.trc.hyflo.crisis.dto.query.IncidentImpactReadDTO;
import dz.sh.trc.hyflo.crisis.model.IncidentImpact;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;

public final class IncidentImpactMapper {

    private IncidentImpactMapper() {}

    public static IncidentImpactReadDTO toReadDTO(IncidentImpact entity) {
        if (entity == null) return null;

        return IncidentImpactReadDTO.builder()
                .id(entity.getId())
                .incidentId(entity.getIncident() != null
                        ? entity.getIncident().getId() : null)
                .impactLevel(entity.getImpactLevel())
                .estimatedLoss(entity.getEstimatedLoss())
                .downtimeMinutes(entity.getDowntimeMinutes())
                .affectedPipelineIds(
                        entity.getAffectedPipelines() != null
                                ? entity.getAffectedPipelines().stream()
                                        .map(Pipeline::getId)
                                        .collect(Collectors.toList())
                                : null)
                .affectedSegmentIds(
                        entity.getAffectedSegments() != null
                                ? entity.getAffectedSegments().stream()
                                        .map(PipelineSegment::getId)
                                        .collect(Collectors.toList())
                                : null)
                .build();
    }
}
