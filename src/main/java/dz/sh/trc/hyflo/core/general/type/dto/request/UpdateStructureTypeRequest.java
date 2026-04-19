package dz.sh.trc.hyflo.core.general.type.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateStructureTypeRequest(
        String designationAr,
        String designationEn,
        @NotBlank(message = "French designation is mandatory")
        String designationFr
) {}
