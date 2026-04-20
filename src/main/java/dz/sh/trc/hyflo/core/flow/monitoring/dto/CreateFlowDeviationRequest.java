package dz.sh.trc.hyflo.core.flow.monitoring.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateFlowDeviationRequest {
    private LocalDate date;
    private String metricCode;
    private BigDecimal actualValue;
    private BigDecimal expectedValue;
    private BigDecimal deviationValue;
    private BigDecimal deviationPercent;
    private String baselineType;
    private Long pipelineId;
    private Long readingId;
    private Long planId;
}
