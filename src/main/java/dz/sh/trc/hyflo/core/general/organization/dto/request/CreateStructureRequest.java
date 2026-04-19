package dz.sh.trc.hyflo.core.general.organization.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateStructureRequest(
        @NotBlank(message = "Code is mandatory")
        String code,
        
        String designationAr,
        String designationEn,
        
        @NotBlank(message = "French designation is mandatory")
        String designationFr,
        
        @NotNull(message = "Structure type ID is mandatory")
        Long structureTypeId,
        
        Long parentStructureId
) {}
