package dz.sh.trc.hyflo.core.system.security.dto.response;

import java.util.List;

public record GroupResponse(
        Long id,
        String name,
        String description,
        List<RoleSummary> roles
) {}
