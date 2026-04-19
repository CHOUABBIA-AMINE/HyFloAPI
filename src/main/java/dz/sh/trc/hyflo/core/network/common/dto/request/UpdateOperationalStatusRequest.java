package dz.sh.trc.hyflo.core.network.common.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateOperationalStatusRequest(
        String designationAr,
        String designationEn,
        
        @NotBlank(message = "French designation is mandatory")
        String designationFr,
        
        String descriptionAr,
        String descriptionEn,
        String descriptionFr
) {}
