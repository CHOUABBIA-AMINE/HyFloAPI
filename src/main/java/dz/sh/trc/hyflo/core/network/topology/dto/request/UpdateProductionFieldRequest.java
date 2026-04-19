package dz.sh.trc.hyflo.core.network.topology.dto.request;

import java.time.LocalDate;
import java.util.Set;

public record UpdateProductionFieldRequest(
        String name,
        LocalDate installationDate,
        LocalDate commissioningDate,
        LocalDate decommissioningDate,
        Long operationalStatusId,
        Long ownerId,
        Long vendorId,
        Long locationId,
        Double capacity,
        Long productionFieldTypeId,
        Long processingPlantId,
        Set<Long> productIds,
        Set<Long> partnerIds
) {}