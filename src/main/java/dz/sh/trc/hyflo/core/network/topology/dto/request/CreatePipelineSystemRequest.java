package dz.sh.trc.hyflo.core.network.topology.dto.request;

public record CreatePipelineSystemRequest(
        String code,
        String name,
        Long productId,
        Long operationalStatusId,
        Long structureId
) {}