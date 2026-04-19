package dz.sh.trc.hyflo.core.network.topology.dto.request;

import java.time.LocalDate;
import java.util.Set;

public record CreateProcessingPlantRequest(
        String code,
        String name,
        LocalDate installationDate,
        LocalDate commissioningDate,
        LocalDate decommissioningDate,
        Long operationalStatusId,
        Long ownerId,
        Long vendorId,
        Long locationId,
        Double capacity,
        Long processingPlantTypeId,
        Set<Long> productIds,
        Set<Long> partnerIds,
        Set<Long> pipelineIds
) {}