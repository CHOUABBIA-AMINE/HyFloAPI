/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentImpactMapper
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.mapper;

import dz.sh.trc.hyflo.crisis.dto.query.IncidentImpactReadDto;
import dz.sh.trc.hyflo.crisis.model.IncidentImpact;
import dz.sh.trc.hyflo.general.organization.model.Employee;

public final class IncidentImpactMapper {

    private IncidentImpactMapper() {}

    public static IncidentImpactReadDto toReadDto(IncidentImpact entity) {
        if (entity == null) return null;

        return IncidentImpactReadDto.builder()
                .id(entity.getId())
                .incidentId(entity.getIncident() != null ? entity.getIncident().getId() : null)
                .impactCategory(entity.getImpactCategory())
                .estimatedVolumeLossM3(entity.getEstimatedVolumeLossM3())
                .disruptionDurationHours(entity.getDisruptionDurationHours())
                .financialImpactUsd(entity.getFinancialImpactUsd())
                .assessedAt(entity.getAssessedAt())
                .assessedById(entity.getAssessedBy() != null ? entity.getAssessedBy().getId() : null)
                .assessedByFullName(entity.getAssessedBy() != null
                        ? buildFullName(entity.getAssessedBy()) : null)
                .build();
    }

    private static String buildFullName(Employee e) {
        if (e == null) return null;
        String first = e.getFirstName() != null ? e.getFirstName() : "";
        String last  = e.getLastName()  != null ? e.getLastName()  : "";
        return (first + " " + last).trim();
    }
}
