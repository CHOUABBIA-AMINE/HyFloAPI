package dz.sh.trc.hyflo.core.system.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for user registration")
public record RegisterRequest(
        @Schema(description = "Username", example = "jdoe")
        @NotBlank(message = "Username cannot be blank")
        String username,
        
        @Schema(description = "Email address", example = "jdoe@sonatrach.dz")
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email,
        
        @Schema(description = "Password", example = "SecurePass123!")
        @NotBlank(message = "Password cannot be blank")
        String password,
        
        @Schema(description = "Associated employee ID (optional)")
        Long employeeId
) {}
