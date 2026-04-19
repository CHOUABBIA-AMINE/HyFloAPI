package dz.sh.trc.hyflo.core.flow.reference.dto.response;

public record ReadingSourceNatureResponse(
        Long id,
        String code,
        String designationAr,
        String designationEn,
        String designationFr
) {}