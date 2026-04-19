package dz.sh.trc.hyflo.core.network.topology.dto.response;

public record PipelineSegmentSummary(
        Long id,
        String code,
        String name
) {}