package dz.sh.trc.hyflo.core.system.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to create a resource type")
public record CreateResourceTypeRequest(
        @NotBlank(message = "Resource type name cannot be blank")
        String name,
        String description,
        Boolean active,
        Boolean protectedType
) {}
