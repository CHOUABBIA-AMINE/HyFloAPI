package dz.sh.trc.hyflo.core.network.topology.dto.response;

import java.time.LocalDate;
import java.util.Set;

public record PipelineSystemResponse(
        Long id,
        String code,
        String name,
        Long productId,
        String productDesignationFr,
        Long operationalStatusId,
        String operationalStatusDesignationFr,
        Long structureId,
        String structureDesignationFr
) {}