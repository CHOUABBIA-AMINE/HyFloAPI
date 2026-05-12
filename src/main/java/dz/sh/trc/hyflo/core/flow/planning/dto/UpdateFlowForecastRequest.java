package dz.sh.trc.hyflo.core.flow.planning.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdateFlowForecastRequest {
    private LocalDate forecastDate;
    private BigDecimal predictedVolume;
    private BigDecimal adjustedVolume;
    private BigDecimal actualVolume;
    private BigDecimal accuracy;
    private String adjustmentNotes;
    private Long infrastructureId;
    private Long productId;
    private Long operationTypeId;
    private Long supervisorId;
}
