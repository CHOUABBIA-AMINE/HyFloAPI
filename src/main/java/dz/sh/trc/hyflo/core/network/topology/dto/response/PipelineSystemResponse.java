package dz.sh.trc.hyflo.core.network.topology.dto.response;

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