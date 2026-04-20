package dz.sh.trc.hyflo.core.crisis.emergency.dto.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CreateIncidentRequest {
    private String code;
    private String title;
    private String description;
    private LocalDateTime occurredAt;
    private LocalDateTime resolvedAt;
    private Boolean active;
    private Long severityId;
    private Long pipelineSegmentId;
}
