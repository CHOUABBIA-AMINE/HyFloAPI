/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : Role
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Model
 *  @Package    : General / Organization
 *
 *  @Description: Represents a functional role within the SONATRACH organizational
 *                hierarchy (e.g., OPERATOR, VALIDATOR, SUPERVISOR, ADMIN).
 *                Referenced by Employee to establish RBAC assignment at the
 *                employee level.
 *
 **/

package dz.sh.trc.hyflo.general.organization.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Functional role entity.
 *
 * Each employee is assigned exactly one Role that governs their permissions
 * within the HyFlo workflow (reading submission, validation, incident management, etc.).
 */
@Schema(description = "Functional role within the SONATRACH organizational structure")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Role")
@Table(
        name = "T_01_03_05",
        uniqueConstraints = {
                @UniqueConstraint(name = "T_01_03_05_UK_01", columnNames = {"F_01"}),
                @UniqueConstraint(name = "T_01_03_05_UK_02", columnNames = {"F_02"})
        }
)
public class Role extends GenericModel {

    @Schema(
            description = "Unique technical code for the role (e.g., OPERATOR, VALIDATOR, SUPERVISOR, ADMIN)",
            example = "OPERATOR",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 50
    )
    @NotBlank(message = "Role code is mandatory")
    @Size(max = 50, message = "Role code must not exceed 50 characters")
    @Column(name = "F_01", length = 50, nullable = false)
    private String code;

    @Schema(
            description = "Human-readable designation of the role in French",
            example = "Op\u00e9rateur terrain",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @NotBlank(message = "Role name is mandatory")
    @Size(max = 100, message = "Role name must not exceed 100 characters")
    @Column(name = "F_02", length = 100, nullable = false)
    private String name;

    @Schema(
            description = "Optional description of responsibilities and scope",
            example = "Responsible for daily flow reading submission on assigned pipelines",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 500
    )
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "F_03", length = 500)
    private String description;

    @Schema(
            description = "Whether this role is currently active and assignable",
            example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_04", nullable = false)
    private Boolean active = Boolean.TRUE;
}
