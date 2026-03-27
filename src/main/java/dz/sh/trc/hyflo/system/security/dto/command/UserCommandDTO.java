/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : UserCommandDto
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Command
 *  @Package    : System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.dto.command;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Command DTO for creating or updating a system user account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCommandDTO {

    @Schema(description = "Username for authentication", example = "a.benali",
            requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 50)
    @NotBlank(message = "Username is required")
    @Size(max = 50)
    private String username;

    @Schema(description = "User email address", requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 255)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255)
    private String email;

    @Schema(description = "Password (write-only, never returned in responses)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Schema(description = "ID of the linked employee record",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @Schema(description = "Whether the user account is enabled")
    private Boolean enabled;
}
