/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertMapper
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-26-2026 — fix getName() → correct accessors
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

        // FlowThreshold has no name/code field — pipeline code is the correct display identifier
        String thresholdLabel = null;
        if (entity.getThreshold() != null
                && entity.getThreshold().getPipeline() != null) {
            thresholdLabel = entity.getThreshold().getPipeline().getCode();
        }

        return FlowAlertReadDto.builder()
                .id(entity.getId())
                .thresholdId(entity.getThreshold() != null ? entity.getThreshold().getId() : null)
                .thresholdName(thresholdLabel)
                .flowReadingId(entity.getFlowReading() != null ? entity.getFlowReading().getId() : null)
                .alertMessage(entity.getMessage())
                .alertTimestamp(entity.getAlertTimestamp())
                .status(entity.getStatus() != null ? entity.getStatus().getDesignationFr() : null)
                .notificationSent(entity.getNotificationSent())
                .pipelineName(thresholdLabel) // pipeline code also serves as pipeline identifier here
                .build();
    }
}
