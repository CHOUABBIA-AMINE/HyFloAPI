/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertReadDto
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Record DTO (Read)
 *  @Layer      : DTO
 *  @Package    : Flow / Core
 *
 *  @Replaces   : FlowAlertDTO (legacy, deleted)
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class FlowAlertReadDto {

    Long id;
    Long thresholdId;
    String thresholdName;
    Long flowReadingId;
    String alertMessage;
    LocalDateTime alertTimestamp;
    String status;
    Boolean notificationSent;
    String pipelineName;
}
