package dz.sh.trc.hyflo.core.system.security.dto.request;

import java.util.Set;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to update an existing role")
public record UpdateRoleRequest(
        String description,
        
        Set<Long> permissionIds
) {}
