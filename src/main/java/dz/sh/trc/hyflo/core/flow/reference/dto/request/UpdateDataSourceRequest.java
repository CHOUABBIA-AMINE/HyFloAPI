package dz.sh.trc.hyflo.core.flow.reference.dto.request;

public record UpdateDataSourceRequest(
        String designationAr,
        String designationEn,
        String designationFr,
        String descriptionAr,
        String descriptionEn,
        String descriptionFr,
        Long sourceNatureId
) {}