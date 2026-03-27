/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : EmployeeReadDTO
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.dto.query;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Read DTO for an employee — safe for API exposure")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeReadDTO {

    @Schema(description = "Technical identifier of the employee")
    private Long id;

    @Schema(description = "Employee registration number", example = "EMP-2025-001")
    private String registrationNumber;

    @Schema(description = "First name")
    private String firstName;

    @Schema(description = "Last name")
    private String lastName;

    @Schema(description = "Computed full name", example = "Ahmed Benali")
    private String fullName;

    @Schema(description = "Professional email address")
    private String email;

    @Schema(description = "Job title or function", example = "Transport Operations Engineer")
    private String jobTitle;

    @Schema(description = "ID of the organizational structure")
    private Long structureId;

    @Schema(description = "Name of the organizational structure")
    private String structureName;

    @Schema(description = "ID of the employee's role")
    private Long roleId;

    @Schema(description = "Code of the employee's role", example = "OPERATOR")
    private String roleCode;

    @Schema(description = "Whether the employee record is active")
    private Boolean active;

    // ---- NO fromEntity / mapping logic ----
    // All mapping is owned by EmployeeMapper.
}
