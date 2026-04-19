package dz.sh.trc.hyflo.core.system.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to create a new permission")
public record CreatePermissionRequest(
        @NotBlank(message = "Permission name cannot be blank")
        String name,
        
        String description,
        
        @NotNull(message = "Resource ID is required")
        Long resourceId,
        
        @NotNull(message = "Action ID is required")
        Long actionId
) {}
