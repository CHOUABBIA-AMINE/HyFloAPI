package dz.sh.trc.hyflo.core.system.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for changing password")
public record ChangePasswordRequest(
        @Schema(description = "Old password")
        @NotBlank(message = "Old password cannot be blank")
        String oldPassword,
        
        @Schema(description = "New password")
        @NotBlank(message = "New password cannot be blank")
        String newPassword
) {}
