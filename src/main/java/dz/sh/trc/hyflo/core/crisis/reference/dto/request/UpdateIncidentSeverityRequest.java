package dz.sh.trc.hyflo.core.crisis.reference.dto.request;

import lombok.Data;

@Data
public class UpdateIncidentSeverityRequest {
    private String code;
    private String label;
    private Integer rank;
}
