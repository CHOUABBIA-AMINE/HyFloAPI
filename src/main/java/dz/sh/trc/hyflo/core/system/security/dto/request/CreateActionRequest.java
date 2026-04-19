package dz.sh.trc.hyflo.core.system.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to create an action")
public record CreateActionRequest(
        @NotBlank(message = "Action name cannot be blank")
        String name,
        String description
) {}
