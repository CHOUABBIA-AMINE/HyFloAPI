package dz.sh.trc.hyflo.core.flow.planning.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateFlowPlanRequest {
    private LocalDate planDate;
    private BigDecimal plannedVolumeM3;
    private BigDecimal plannedVolumeMscf;
    private BigDecimal plannedInletPressureBar;
    private BigDecimal plannedOutletPressureBar;
    private BigDecimal plannedTemperatureCelsius;
    private String scenarioCode;
    private String notes;
    private LocalDateTime approvedAt;
    private Long pipelineId;
    private Long statusId;
    private Long approvedById;
    private Long submittedById;
    private Long revisedFromId;
}
