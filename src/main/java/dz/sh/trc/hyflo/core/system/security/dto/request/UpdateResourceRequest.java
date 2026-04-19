package dz.sh.trc.hyflo.core.system.security.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to update a resource")
public record UpdateResourceRequest(
        String description,
        Long resourceTypeId
) {}
