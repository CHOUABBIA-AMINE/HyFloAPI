package dz.sh.trc.hyflo.core.network.topology.dto.request;

public record UpdatePipelineSystemRequest(
        String name,
        Long productId,
        Long operationalStatusId,
        Long structureId
) {}