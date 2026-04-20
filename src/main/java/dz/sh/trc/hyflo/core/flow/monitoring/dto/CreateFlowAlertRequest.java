package dz.sh.trc.hyflo.core.flow.monitoring.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateFlowAlertRequest {
    private LocalDateTime alertTimestamp;
    private BigDecimal actualValue;
    private BigDecimal thresholdValue;
    private String message;
    private Boolean notificationSent;
    private Long thresholdId;
    private Long flowReadingId;
    private Long statusId;
}

