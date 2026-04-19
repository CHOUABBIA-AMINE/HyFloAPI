package dz.sh.trc.hyflo.core.network.type.dto.response;

public record VendorTypeResponse(
        Long id,
        String designationAr,
        String designationEn,
        String designationFr
) {}