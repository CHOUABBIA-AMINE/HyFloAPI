/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowEventMapper
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-26-2026 — fix getName() → correct accessors
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.mapper;

import dz.sh.trc.hyflo.flow.core.dto.FlowEventReadDto;
import dz.sh.trc.hyflo.flow.core.model.FlowEvent;

public final class FlowEventMapper {

    private FlowEventMapper() {}

    public static FlowEventReadDto toReadDto(FlowEvent entity) {
        if (entity == null) return null;

        return FlowEventReadDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .infrastructureId(entity.getInfrastructure() != null ? entity.getInfrastructure().getId() : null)
                .infrastructureName(entity.getInfrastructure() != null ? entity.getInfrastructure().getName() : null)
                .severity(entity.getSeverity() != null ? entity.getSeverity().getDesignationFr() : null)
                .status(entity.getStatus() != null ? entity.getStatus().getDesignationFr() : null)
                .impactOnFlow(entity.getImpactOnFlow())
                .eventTimestamp(entity.getEventTimestamp())
                .resolvedAt(entity.getEndTime())
                .build();
    }
}
