package dz.sh.trc.hyflo.core.network.topology.dto.request;

import java.time.LocalDate;
import java.util.Set;

public record CreateStationRequest(
        String code,
        String name,
        LocalDate installationDate,
        LocalDate commissioningDate,
        LocalDate decommissioningDate,
        Long operationalStatusId,
        Long ownerId,
        Long vendorId,
        Long locationId,
        Long stationTypeId,
        Long pipelineSystemId,
        Set<Long> pipelineIds
) {}