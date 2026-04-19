package dz.sh.trc.hyflo.core.general.organization.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateJobRequest(
        @NotBlank(message = "Code is mandatory")
        String code,
        
        String designationAr,
        String designationEn,
        
        @NotBlank(message = "French designation is mandatory")
        String designationFr,
        
        @NotNull(message = "Structure ID is mandatory")
        Long structureId
) {}
