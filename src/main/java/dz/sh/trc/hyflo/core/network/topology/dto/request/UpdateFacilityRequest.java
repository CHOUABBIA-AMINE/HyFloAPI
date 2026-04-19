package dz.sh.trc.hyflo.core.network.topology.dto.request;

import java.time.LocalDate;

public record UpdateFacilityRequest(
        String name,
        LocalDate installationDate,
        LocalDate commissioningDate,
        LocalDate decommissioningDate,
        Long operationalStatusId,
        Long ownerId,
        Long vendorId,
        Long locationId
) {}