package dz.sh.trc.hyflo.core.system.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload after successful authentication")
public record AuthResponse(
        @Schema(description = "JWT Access Token")
        String accessToken,
        
        @Schema(description = "JWT Refresh Token")
        String refreshToken,
        
        @Schema(description = "Token Type (Bearer)")
        String tokenType,
        
        @Schema(description = "Expires in seconds")
        Long expiresIn
) {}
