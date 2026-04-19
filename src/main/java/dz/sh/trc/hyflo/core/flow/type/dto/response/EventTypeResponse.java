package dz.sh.trc.hyflo.core.flow.type.dto.response;

public record EventTypeResponse(
        Long id,
        String code,
        String designationAr,
        String designationEn,
        String designationFr
) {}