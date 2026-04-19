package dz.sh.trc.hyflo.core.system.security.dto.response;

public record ResourceTypeResponse(
        Long id,
        String name,
        String description,
        Boolean active,
        Boolean protectedType
) {}
