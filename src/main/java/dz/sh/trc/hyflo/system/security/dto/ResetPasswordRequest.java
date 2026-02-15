/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ResetPasswordRequest
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetPasswordRequest {
    
	@Schema(
        description = "Unique username for authentication (letters, numbers, dots, hyphens, and underscores only)",
        example = "medabir",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 3,
        maxLength = 20,
        pattern = "^[a-zA-Z0-9._-]{3,20}$"
    )
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;
    
    @Schema(
        description = "User new password (must be at least 8 characters, will be BCrypt encrypted)",
        example = "SecurePassword123!",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 8,
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    @NotBlank(message = "New password is required")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{8,120}$")
    @Size(min = 8, max = 120, message = "Password must be between 8 and 120 characters")
    private String newPassword;
}
