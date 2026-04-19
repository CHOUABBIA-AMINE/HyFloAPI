package dz.sh.trc.hyflo.core.network.common.dto.response;

public record ProductSummary(
        Long id,
        String code,
        String designationFr,
        Boolean isHazardous
) {}
