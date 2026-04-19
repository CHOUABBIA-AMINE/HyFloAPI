package dz.sh.trc.hyflo.core.system.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to create a resource")
public record CreateResourceRequest(
        @NotBlank(message = "Resource code cannot be blank")
        String code,
        String description,
        @NotNull(message = "Resource Type ID is required")
        Long resourceTypeId
) {}
