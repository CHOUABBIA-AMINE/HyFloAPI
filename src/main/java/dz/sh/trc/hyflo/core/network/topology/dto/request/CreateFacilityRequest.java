package dz.sh.trc.hyflo.core.network.topology.dto.request;

import java.time.LocalDate;

public record CreateFacilityRequest(
        String code,
        String name,
        LocalDate installationDate,
        LocalDate commissioningDate,
        LocalDate decommissioningDate,
        Long operationalStatusId,
        Long ownerId,
        Long vendorId,
        Long locationId
) {}