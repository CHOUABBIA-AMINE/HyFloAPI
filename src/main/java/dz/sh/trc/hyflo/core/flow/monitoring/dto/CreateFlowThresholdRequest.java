package dz.sh.trc.hyflo.core.flow.monitoring.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateFlowThresholdRequest {
    private BigDecimal pressureMin;
    private BigDecimal pressureMax;
    private BigDecimal temperatureMin;
    private BigDecimal temperatureMax;
    private BigDecimal flowRateMin;
    private BigDecimal flowRateMax;
    private BigDecimal containedVolumeMin;
    private BigDecimal containedVolumeMax;
    private BigDecimal alertTolerance;
    private Boolean active;
    private Long pipelineId;
}
