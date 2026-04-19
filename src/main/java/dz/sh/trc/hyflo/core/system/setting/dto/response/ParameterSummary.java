package dz.sh.trc.hyflo.core.system.setting.dto.response;

public record ParameterSummary(
        Long id,
        String key,
        String value,
        String type
) {}
