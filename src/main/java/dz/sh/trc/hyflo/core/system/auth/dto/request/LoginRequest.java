package dz.sh.trc.hyflo.core.system.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for user login")
public record LoginRequest(
        @Schema(description = "Username", example = "admin")
        @NotBlank(message = "Username cannot be blank")
        String username,
        
        @Schema(description = "Password", example = "secret")
        @NotBlank(message = "Password cannot be blank")
        String password
) {}
