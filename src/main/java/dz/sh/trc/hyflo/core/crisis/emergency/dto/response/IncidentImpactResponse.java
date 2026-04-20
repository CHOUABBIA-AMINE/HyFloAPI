package dz.sh.trc.hyflo.core.crisis.emergency.dto.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class IncidentImpactResponse {
    private Long id;
    private Long incidentId;
    private BigDecimal estimatedLoss;
    private Long downtimeMinutes;
    private String impactLevel;
    private String incidentCode;
}
