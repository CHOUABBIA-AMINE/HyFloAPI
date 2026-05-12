package dz.sh.trc.hyflo.core.flow.monitoring.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FlowAlertSummary {
    private Long id;
    private LocalDateTime alertTimestamp;
    private String statusName;
    private String message;
}
