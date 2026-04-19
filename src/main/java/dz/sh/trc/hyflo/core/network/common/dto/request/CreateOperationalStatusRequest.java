package dz.sh.trc.hyflo.core.network.common.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateOperationalStatusRequest(
        @NotBlank(message = "Status code is mandatory")
        String code,
        
        String designationAr,
        String designationEn,
        
        @NotBlank(message = "French designation is mandatory")
        String designationFr,
        
        String descriptionAr,
        String descriptionEn,
        String descriptionFr
) {}
