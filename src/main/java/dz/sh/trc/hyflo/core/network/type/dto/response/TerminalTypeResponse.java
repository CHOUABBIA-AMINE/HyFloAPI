package dz.sh.trc.hyflo.core.network.type.dto.response;

public record TerminalTypeResponse(
        Long id,
        String code,
        String designationAr,
        String designationEn,
        String designationFr
) {}