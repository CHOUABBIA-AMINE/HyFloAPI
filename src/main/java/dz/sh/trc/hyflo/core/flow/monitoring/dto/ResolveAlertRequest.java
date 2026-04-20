package dz.sh.trc.hyflo.core.flow.monitoring.dto;

import lombok.Data;

@Data
public class ResolveAlertRequest {
    private Long resolvedByEmployeeId;
    private String resolutionNotes;
}

