/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowEventReadDto
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Record DTO (Read)
 *  @Layer      : DTO
 *  @Package    : Flow / Core
 *
 *  @Replaces   : FlowEventDTO (legacy, deleted)
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class FlowEventReadDto {

    Long id;
    String title;
    String description;
    Long infrastructureId;
    String infrastructureName;
    String severity;
    String status;
    Boolean impactOnFlow;
    LocalDateTime eventTimestamp;
    LocalDateTime resolvedAt;
}
