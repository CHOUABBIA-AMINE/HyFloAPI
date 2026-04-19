package dz.sh.trc.hyflo.core.network.common.dto.response;

public record VendorResponse(
        Long id,
        String name,
        String shortName,
        Long vendorTypeId,
        String vendorTypeDesignationFr,
        Long countryId,
        String countryDesignationFr
) {}
