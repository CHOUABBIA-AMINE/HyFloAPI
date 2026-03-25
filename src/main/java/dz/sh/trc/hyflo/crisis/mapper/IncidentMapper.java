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

import dz.sh.trc.hyflo.crisis.dto.query.IncidentReadDto;
import dz.sh.trc.hyflo.crisis.model.Incident;
import dz.sh.trc.hyflo.general.organization.model.Employee;

public final class IncidentMapper {

    private IncidentMapper() {}

    public static IncidentReadDto toReadDto(Incident entity) {
        if (entity == null) return null;

        return IncidentReadDto.builder()
                .id(entity.getId())
                .incidentTypeCode(entity.getIncidentType() != null
                        ? entity.getIncidentType().getCode() : null)
                .severityLevel(entity.getSeverityLevel())
                .status(entity.getStatus())
                .description(entity.getDescription())
                .declaredAt(entity.getDeclaredAt())
                .resolvedAt(entity.getResolvedAt())
                .pipelineId(entity.getPipeline() != null
                        ? entity.getPipeline().getId() : null)
                .pipelineCode(entity.getPipeline() != null
                        ? entity.getPipeline().getCode() : null)
                .pipelineSegmentId(entity.getPipelineSegment() != null
                        ? entity.getPipelineSegment().getId() : null)
                .pipelineSegmentCode(entity.getPipelineSegment() != null
                        ? entity.getPipelineSegment().getCode() : null)
                .declaredById(entity.getDeclaredBy() != null
                        ? entity.getDeclaredBy().getId() : null)
                .declaredByFullName(entity.getDeclaredBy() != null
                        ? buildFullName(entity.getDeclaredBy()) : null)
                .build();
    }

    private static String buildFullName(Employee e) {
        if (e == null) return null;
        String first = e.getFirstName() != null ? e.getFirstName() : "";
        String last  = e.getLastName()  != null ? e.getLastName()  : "";
        return (first + " " + last).trim();
    }
}
