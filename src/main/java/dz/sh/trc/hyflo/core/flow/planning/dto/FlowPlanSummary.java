package dz.sh.trc.hyflo.core.flow.planning.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class FlowPlanSummary {
    private Long id;
    private LocalDate planDate;
    private BigDecimal plannedVolumeM3;
    private String scenarioCode;
    private String pipelineName;
    private String statusName;
}
