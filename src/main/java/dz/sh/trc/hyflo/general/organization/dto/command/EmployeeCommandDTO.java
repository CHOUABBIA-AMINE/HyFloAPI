/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : EmployeeCommandDTO
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Command
 *  @Package    : General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.dto.command;

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

@Schema(description = "Command DTO for creating or updating an employee record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeCommandDTO {

    @Schema(description = "Employee registration number", example = "EMP-2025-001",
            requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 30)
    @NotBlank(message = "Registration number is required")
    @Size(max = 30)
    private String registrationNumber;

    @Schema(description = "First name", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @Schema(description = "Last name", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    @Schema(description = "Professional email address", example = "a.benali@sonatrach.dz",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 255)
    @Email(message = "Email must be valid")
    @Size(max = 255)
    private String email;

    @Schema(description = "Job title or function", example = "Transport Operations Engineer",
            maxLength = 150)
    @Size(max = 150)
    private String jobTitle;

    @Schema(description = "ID of the organizational structure this employee belongs to",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Structure ID is required")
    private Long structureId;

    @Schema(description = "ID of the employee's role", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Role ID is required")
    private Long roleId;

    @Schema(description = "Whether the employee record is active")
    private Boolean active;
}
