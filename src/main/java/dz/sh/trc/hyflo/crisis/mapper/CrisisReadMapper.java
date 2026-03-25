/**
 * 
 * 	@Author		: HyFlo v2 Mapper
 *
 * 	@Name		: CrisisReadMapper
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Mapping
 * 	@Package	: Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import dz.sh.trc.hyflo.crisis.dto.IncidentImpactReadDto;
import dz.sh.trc.hyflo.crisis.dto.IncidentReadDto;
import dz.sh.trc.hyflo.crisis.model.Incident;
import dz.sh.trc.hyflo.crisis.model.IncidentImpact;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;

/**
 * Static mappers from crisis entities to read DTOs.
 */
public final class CrisisReadMapper {

    private CrisisReadMapper() {
        // utility class
    }

    public static IncidentReadDto toDto(Incident entity) {
        if (entity == null) {
            return null;
        }
        IncidentReadDto dto = new IncidentReadDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setSeverityId(entity.getSeverity() != null ? entity.getSeverity().getId() : null);
        dto.setStatusId(entity.getStatus() != null ? entity.getStatus().getId() : null);
        dto.setInfrastructureId(entity.getInfrastructure() != null ? entity.getInfrastructure().getId() : null);
        dto.setPipelineSegmentId(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getId() : null);
        dto.setRootEventId(entity.getRootEvent() != null ? entity.getRootEvent().getId() : null);
        dto.setRootAlertId(entity.getRootAlert() != null ? entity.getRootAlert().getId() : null);
        dto.setStartedAt(entity.getStartedAt());
        dto.setEndedAt(entity.getEndedAt());
        dto.setActive(entity.getActive());
        return dto;
    }

    public static IncidentImpactReadDto toDto(IncidentImpact entity) {
        if (entity == null) {
            return null;
        }
        IncidentImpactReadDto dto = new IncidentImpactReadDto();
        dto.setId(entity.getId());
        dto.setIncidentId(entity.getIncident() != null ? entity.getIncident().getId() : null);
        dto.setAffectedPipelineIds(extractIds(entity.getAffectedPipelines()));
        dto.setAffectedSegmentIds(extractSegmentIds(entity.getAffectedSegments()));
        dto.setEstimatedLoss(entity.getEstimatedLoss());
        dto.setDowntimeMinutes(entity.getDowntimeMinutes());
        dto.setImpactLevel(entity.getImpactLevel());
        return dto;
    }

    private static Set<Long> extractIds(Set<Pipeline> pipelines) {
        if (pipelines == null) {
            return null;
        }
        return pipelines.stream()
                .map(Pipeline::getId)
                .collect(Collectors.toSet());
    }

    private static Set<Long> extractSegmentIds(Set<PipelineSegment> segments) {
        if (segments == null) {
            return null;
        }
        return segments.stream()
                .map(PipelineSegment::getId)
                .collect(Collectors.toSet());
    }
}
