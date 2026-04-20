package dz.sh.trc.hyflo.core.flow.monitoring.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateFlowAnomalyRequest {
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
