package dz.sh.trc.hyflo.core.crisis.emergency.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class IncidentResponse {
    private Long id;
    private String code;
    private String title;
    private String description;
    private LocalDateTime occurredAt;
    private LocalDateTime resolvedAt;
    private Boolean active;
    private Long severityId;
    private Long pipelineSegmentId;
    private String severityLabel;
    private String pipelineSegmentName;
}
