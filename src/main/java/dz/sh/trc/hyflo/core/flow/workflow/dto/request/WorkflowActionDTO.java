package dz.sh.trc.hyflo.core.flow.workflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WorkflowActionDTO(
        @NotBlank(message = "Action is mandatory")
        String action, // SUBMIT, VALIDATE, APPROVE, REJECT
        
        String comment,
        
        @NotNull(message = "Actor employee ID is mandatory")
        Long actorEmployeeId
) {}
