package dz.sh.trc.hyflo.core.crisis.emergency.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreateIncidentImpactRequest {
    private Long incidentId;
    private BigDecimal estimatedLoss;
    private Long downtimeMinutes;
    private String impactLevel;
}
