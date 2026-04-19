package dz.sh.trc.hyflo.core.system.security.dto.response;

public record ResourceResponse(
        Long id,
        String code,
        String description,
        String resourceTypeName
) {}
