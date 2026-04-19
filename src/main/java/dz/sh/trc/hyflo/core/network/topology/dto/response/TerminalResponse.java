package dz.sh.trc.hyflo.core.network.topology.dto.response;

import java.time.LocalDate;
import java.util.Set;

public record TerminalResponse(
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
        String locationDesignationFr,
        Long terminalTypeId,
        String terminalTypeDesignationFr,
        Set<Long> facilityIds
) {}