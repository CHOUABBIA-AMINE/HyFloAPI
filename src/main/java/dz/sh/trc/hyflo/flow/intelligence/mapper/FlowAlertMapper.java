/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertMapper
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-28-2026 — moved from flow.core.mapper to flow.intelligence.mapper
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.mapper;

import dz.sh.trc.hyflo.flow.intelligence.dto.FlowAlertReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.model.FlowAlert;

public final class FlowAlertMapper {

    private FlowAlertMapper() {}

    public static FlowAlertReadDTO toReadDTO(FlowAlert entity) {
        if (entity == null) return null;

        // FlowThreshold has no name/code field — pipeline code is the correct display identifier
        String thresholdLabel = null;
        if (entity.getThreshold() != null
                && entity.getThreshold().getPipeline() != null) {
            thresholdLabel = entity.getThreshold().getPipeline().getCode();
        }

        return FlowAlertReadDTO.builder()
                .id(entity.getId())
                .thresholdId(entity.getThreshold() != null ? entity.getThreshold().getId() : null)
                .thresholdPipelineCode(thresholdLabel)
                .flowReadingId(entity.getFlowReading() != null ? entity.getFlowReading().getId() : null)
                .message(entity.getMessage())
                .alertTimestamp(entity.getAlertTimestamp())
                .statusName(entity.getStatus() != null ? entity.getStatus().getDesignationFr() : null)
                .notificationSent(entity.getNotificationSent())
                .build();
    }
}
