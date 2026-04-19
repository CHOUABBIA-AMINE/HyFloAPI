package dz.sh.trc.hyflo.core.network.type.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateProcessingPlantTypeRequest(
        @NotBlank(message = "Code is mandatory")
        String code,
        
        String designationAr,
        String designationEn,
        
        @NotBlank(message = "French designation is mandatory")
        String designationFr
) {}