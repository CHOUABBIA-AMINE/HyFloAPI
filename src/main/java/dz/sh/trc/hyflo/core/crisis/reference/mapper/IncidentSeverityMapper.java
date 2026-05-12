/**
 *
 *	@Author		: MEDJERAB Abir
 *
 *  @Name       : IncidentSeverityMapper
 *  @CreatedOn  : 03-26-2026
 *	@UpdatedOn	: 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Mapper
 *  @Package    : Crisis / Common
 *
 **/

package dz.sh.trc.hyflo.core.crisis.reference.mapper;

import dz.sh.trc.hyflo.core.crisis.reference.dto.query.IncidentSeverityReadDTO;
import dz.sh.trc.hyflo.core.crisis.reference.model.IncidentSeverity;

public final class IncidentSeverityMapper {

    private IncidentSeverityMapper() {}

    public static IncidentSeverityReadDTO toReadDTO(IncidentSeverity entity) {
        if (entity == null) return null;
        return IncidentSeverityReadDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .label(entity.getLabel())
                .rank(entity.getRank())
                .build();
    }
}
