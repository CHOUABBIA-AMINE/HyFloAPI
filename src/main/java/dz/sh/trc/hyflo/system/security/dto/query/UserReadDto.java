/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : UserReadDto
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.dto.query;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Read DTO for a system user — safe for API exposure, no credentials returned")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserReadDto {

    @Schema(description = "Technical identifier of the user account")
    private Long id;

    @Schema(description = "Username", example = "a.benali")
    private String username;

    @Schema(description = "Email address")
    private String email;

    @Schema(description = "Whether the account is enabled")
    private Boolean enabled;

    @Schema(description = "ID of the linked employee")
    private Long employeeId;

    @Schema(description = "Full name of the linked employee")
    private String employeeFullName;

    @Schema(description = "Role code of the linked employee", example = "SUPERVISOR")
    private String roleCode;

    @Schema(description = "Account creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last login timestamp")
    private LocalDateTime lastLoginAt;

    // ---- NO fromEntity / mapping logic ----
    // All mapping is owned by UserMapper.
    // NOTE: password field is INTENTIONALLY absent — never expose credentials on read.
}
