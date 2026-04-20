package dz.sh.trc.hyflo.core.flow.monitoring.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UpdateFlowAnomalyRequest {
    private String anomalyType;
    private BigDecimal severityScore;
    private BigDecimal confidenceScore;
    private String modelName;
    private String explanation;
    private LocalDateTime detectedAt;
    private Long readingId;
    private Long segmentFlowReadingId;
    private Long pipelineSegmentId;
}
