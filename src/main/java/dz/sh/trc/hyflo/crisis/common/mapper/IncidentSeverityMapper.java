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

package dz.sh.trc.hyflo.crisis.common.mapper;

import dz.sh.trc.hyflo.crisis.common.dto.query.IncidentSeverityReadDTO;
import dz.sh.trc.hyflo.crisis.common.model.IncidentSeverity;

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
