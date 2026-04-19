package dz.sh.trc.hyflo.core.network.topology.dto.request;

import java.time.LocalDate;
import java.util.Set;

public record UpdatePipelineSystemRequest(
        String name,
        Long productId,
        Long operationalStatusId,
        Long structureId
) {}