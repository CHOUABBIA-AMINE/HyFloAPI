package dz.sh.trc.hyflo.core.system.setting.dto.response;

public record ParameterResponse(
        Long id,
        String key,
        String value,
        String type,
        String description
) {}
