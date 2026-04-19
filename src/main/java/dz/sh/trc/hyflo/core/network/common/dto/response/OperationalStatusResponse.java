package dz.sh.trc.hyflo.core.network.common.dto.response;

public record OperationalStatusResponse(
        Long id,
        String code,
        String designationAr,
        String designationEn,
        String designationFr,
        String descriptionAr,
        String descriptionEn,
        String descriptionFr
) {}
