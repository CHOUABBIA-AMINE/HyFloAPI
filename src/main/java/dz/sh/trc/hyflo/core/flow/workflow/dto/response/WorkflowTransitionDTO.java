package dz.sh.trc.hyflo.core.flow.workflow.dto.response;

import java.time.LocalDateTime;

public record WorkflowTransitionDTO(
        LocalDateTime timestamp,
        String fromState,
        String toState,
        String action,
        String actorName,
        String comment
) {}
