package dz.sh.trc.hyflo.core.network.type.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCompanyTypeRequest(
        String designationAr,
        String designationEn,
        
        @NotBlank(message = "French designation is mandatory")
        String designationFr
) {}