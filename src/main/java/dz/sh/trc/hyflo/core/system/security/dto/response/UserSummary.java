package dz.sh.trc.hyflo.core.system.security.dto.response;

public record UserSummary(
        Long id,
        String username,
        String email,
        Boolean enabled
) {}
