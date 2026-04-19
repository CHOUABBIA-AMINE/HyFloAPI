package dz.sh.trc.hyflo.core.network.topology.dto.response;

import java.time.LocalDate;

public record FacilityResponse(
        Long id,
        String code,
        String name,
        LocalDate installationDate,
        LocalDate commissioningDate,
        LocalDate decommissioningDate,
        Long operationalStatusId,
        String operationalStatusDesignationFr,
        Long ownerId,
        String ownerDesignationFr,
        Long vendorId,
        String vendorDesignationFr,
        Long locationId,
        String locationDesignationFr
) {}