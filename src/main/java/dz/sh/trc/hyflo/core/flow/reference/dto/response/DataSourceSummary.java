package dz.sh.trc.hyflo.core.flow.reference.dto.response;

public record DataSourceSummary(
        Long id,
        String code,
        String designationFr,
        String sourceNatureDesignationFr
) {}