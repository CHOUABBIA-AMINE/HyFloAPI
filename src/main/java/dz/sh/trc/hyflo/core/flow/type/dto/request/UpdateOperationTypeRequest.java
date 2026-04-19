package dz.sh.trc.hyflo.core.flow.type.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateOperationTypeRequest(
        @Size(max = 100)
        String designationAr,
        
        @Size(max = 100)
        String designationEn,
        
        @Size(max = 100)
        String designationFr
) {}