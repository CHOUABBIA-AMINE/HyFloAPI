/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentSeverityMapper
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.mapper;

import dz.sh.trc.hyflo.crisis.dto.query.IncidentSeverityReadDto;
import dz.sh.trc.hyflo.crisis.model.IncidentSeverity;

public final class IncidentSeverityMapper {

    private IncidentSeverityMapper() {}

    public static IncidentSeverityReadDto toReadDto(IncidentSeverity entity) {
        if (entity == null) return null;
        return IncidentSeverityReadDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .label(entity.getLabel())
                .rank(entity.getRank())
                .build();
    }
}
