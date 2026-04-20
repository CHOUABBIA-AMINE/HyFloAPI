package dz.sh.trc.hyflo.core.flow.monitoring.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FlowAlertSummary {
    private Long id;
    private LocalDateTime alertTimestamp;
    private String statusName;
    private String message;
}
