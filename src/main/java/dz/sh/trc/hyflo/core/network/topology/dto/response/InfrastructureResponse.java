package dz.sh.trc.hyflo.core.network.topology.dto.response;

import java.time.LocalDate;

public record InfrastructureResponse(
        Long id,
        String code,
        String name,
        LocalDate installationDate,
        LocalDate commissioningDate,
        LocalDate decommissioningDate,
        Long operationalStatusId,
        String operationalStatusDesignationFr,
        Long ownerId,
        String ownerDesignationFr
) {}
