package dz.sh.trc.hyflo.core.flow.workflow.dto.response;

import java.time.LocalDateTime;

public record WorkflowInstanceResponse(
        Long id,
        Long targetTypeId,
        String targetTypeDesignationFr,
        Long currentStateId,
        String currentStateDesignationFr,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        Long initiatedById,
        String initiatedByName,
        Long lastActorId,
        String lastActorName,
        String comment,
        String history,
        LocalDateTime updatedAt
) {}
