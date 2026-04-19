package dz.sh.trc.hyflo.core.flow.reference.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDataSourceRequest(
        @NotBlank String code,
        String designationAr,
        String designationEn,
        @NotBlank String designationFr,
        String descriptionAr,
        String descriptionEn,
        String descriptionFr,
        @NotNull Long sourceNatureId
) {}