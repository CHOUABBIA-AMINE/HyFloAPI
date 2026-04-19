package dz.sh.trc.hyflo.core.network.common.dto.response;

public record ProductResponse(
        Long id,
        String code,
        String designationAr,
        String designationEn,
        String designationFr,
        Double density,
        Double viscosity,
        Double flashPoint,
        Double sulfurContent,
        Boolean isHazardous
) {}
