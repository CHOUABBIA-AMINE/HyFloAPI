package dz.sh.trc.hyflo.core.flow.reference.dto.response;

public record DataSourceResponse(
        Long id,
        String code,
        String designationAr,
        String designationEn,
        String designationFr,
        String descriptionAr,
        String descriptionEn,
        String descriptionFr,
        Long sourceNatureId,
        String sourceNatureDesignationFr
) {}