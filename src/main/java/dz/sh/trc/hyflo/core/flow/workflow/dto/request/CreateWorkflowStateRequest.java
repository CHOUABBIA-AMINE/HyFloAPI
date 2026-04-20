package dz.sh.trc.hyflo.core.flow.workflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateWorkflowStateRequest(
        @NotBlank(message = "Code is mandatory")
        @Size(max = 100)
        String code,
        
        @Size(max = 100)
        String designationAr,
        
        @Size(max = 100)
        String designationEn,
        
        @NotBlank(message = "French designation is mandatory")
        @Size(max = 100)
        String designationFr
) {}