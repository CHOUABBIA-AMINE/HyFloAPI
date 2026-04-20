package dz.sh.trc.hyflo.core.flow.workflow.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateWorkflowTargetTypeRequest(
        @Size(max = 100)
        String designationAr,
        
        @Size(max = 100)
        String designationEn,
        
        @Size(max = 100)
        String designationFr
) {}