package dz.sh.trc.hyflo.core.flow.type.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOperationTypeRequest(
        @NotBlank(message = "Code is mandatory")
        @Size(max = 20)
        String code,
        
        @Size(max = 100)
        String designationAr,
        
        @Size(max = 100)
        String designationEn,
        
        @NotBlank(message = "French designation is mandatory")
        @Size(max = 100)
        String designationFr
) {}