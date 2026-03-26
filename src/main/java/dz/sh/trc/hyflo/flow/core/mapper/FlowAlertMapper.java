/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertMapper
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.mapper;

import dz.sh.trc.hyflo.flow.core.dto.FlowAlertReadDto;
import dz.sh.trc.hyflo.flow.core.model.FlowAlert;

public final class FlowAlertMapper {

    private FlowAlertMapper() {}

    public static FlowAlertReadDto toReadDto(FlowAlert entity) {
        if (entity == null) return null;

        return FlowAlertReadDto.builder()
                .id(entity.getId())
                .thresholdId(entity.getThreshold() != null ? entity.getThreshold().getId() : null)
                .thresholdName(entity.getThreshold() != null ? entity.getThreshold().getName() : null)
                .flowReadingId(entity.getFlowReading() != null ? entity.getFlowReading().getId() : null)
                .alertMessage(entity.getMessage())
                .alertTimestamp(entity.getAlertTimestamp())
                .status(entity.getStatus() != null ? entity.getStatus().getName() : null)
                .notificationSent(entity.getNotificationSent())
                .pipelineName(null) // resolved via threshold -> pipeline if needed
                .build();
    }
}
