package dz.sh.trc.hyflo.core.flow.reference.dto.response;

public record SeverityResponse(
        Long id,
        String code,
        String designationAr,
        String designationEn,
        String designationFr
) {}