package dz.sh.trc.hyflo.core.flow.monitoring.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FlowIncidentResponse {
    private Long id;
    private LocalDateTime eventTimestamp;
    private String title;
    private String description;
    private Long severityId;
    private Long infrastructureId;
    private Long reportedById;
    private Long relatedReadingId;
    private Long relatedAlertId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long statusId;
    private String actionTaken;
    private Boolean impactOnFlow;
    private String infrastructureCode;
}
