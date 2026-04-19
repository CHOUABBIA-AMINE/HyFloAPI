package dz.sh.trc.hyflo.core.flow.reference.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateQualityFlagRequest(
        @Size(max = 100)
        String designationAr,
        
        @Size(max = 100)
        String designationEn,
        
        @Size(max = 100)
        String designationFr
) {}