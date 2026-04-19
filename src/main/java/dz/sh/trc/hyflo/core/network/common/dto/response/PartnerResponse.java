package dz.sh.trc.hyflo.core.network.common.dto.response;

public record PartnerResponse(
        Long id,
        String name,
        String shortName,
        Long partnerTypeId,
        String partnerTypeDesignationFr,
        Long countryId,
        String countryDesignationFr
) {}
