package dz.sh.trc.hyflo.core.general.type.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateStructureTypeRequest(
        String designationAr,
        String designationEn,
        @NotBlank(message = "French designation is mandatory")
        String designationFr
) {}
