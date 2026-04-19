package dz.sh.trc.hyflo.core.system.security.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to update an existing permission")
public record UpdatePermissionRequest(
        String description
) {}
