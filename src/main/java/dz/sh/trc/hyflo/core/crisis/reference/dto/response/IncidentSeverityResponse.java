package dz.sh.trc.hyflo.core.crisis.reference.dto.response;

import lombok.Data;

@Data
public class IncidentSeverityResponse {
    private Long id;
    private String code;
    private String label;
    private Integer rank;
}
