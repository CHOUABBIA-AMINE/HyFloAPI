package dz.sh.trc.hyflo.core.network.topology.dto.response;

public record PipelineSummary(
        Long id,
        String code,
        String name,
        Double length,
        String operationalStatusDesignationFr,
        String pipelineSystemName
) {}
