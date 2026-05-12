package dz.sh.trc.hyflo.core.flow.workflow.dto.response;

import java.time.LocalDateTime;

public record WorkflowInstanceSummary(
        Long id,
        String targetTypeDesignationFr,
        String currentStateDesignationFr,
        LocalDateTime startedAt,
        String initiatedByName
) {}
