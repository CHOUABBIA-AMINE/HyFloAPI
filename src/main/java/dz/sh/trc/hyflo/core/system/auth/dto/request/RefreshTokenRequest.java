package dz.sh.trc.hyflo.core.system.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for token refresh")
public record RefreshTokenRequest(
        @Schema(description = "Refresh token")
        @NotBlank(message = "Refresh token cannot be blank")
        String refreshToken
) {}
