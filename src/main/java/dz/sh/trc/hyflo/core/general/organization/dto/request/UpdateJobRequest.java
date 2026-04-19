package dz.sh.trc.hyflo.core.general.organization.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateJobRequest(
        String designationAr,
        String designationEn,
        
        @NotBlank(message = "French designation is mandatory")
        String designationFr,
        
        @NotNull(message = "Structure ID is mandatory")
        Long structureId
) {}
