/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Permission
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Fine-grained permission for access control.
 * Permissions are aggregated into roles. Format: RESOURCE_ACTION (e.g., PIPELINE_READ, PIPELINE_WRITE).
 */
@Schema(description = "Fine-grained permission for resource-level access control within the system")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Permission")
@Table(name = "T_00_02_04", uniqueConstraints = {@UniqueConstraint(name = "T_00_02_04_UK_01", columnNames = "F_01")})
public class Permission extends GenericModel {

	@Schema(
		description = "Unique permission name in format RESOURCE_ACTION",
		example = "PIPELINE_WRITE",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "Permission name is mandatory")
	@Size(max = 100, message = "Permission name must not exceed 100 characters")
	@Pattern(regexp = "^[A-Z_]+$", message = "Permission name must be uppercase with underscores")
	@Column(name="F_01", length=100, nullable=false)
	private String name;

	@Schema(
		description = "Human-readable description of what this permission grants",
		example = "Allows creating, updating, and modifying pipeline data",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "Description must not exceed 200 characters")
	@Column(name="F_02", length=200)
	private String description;

	@Schema(
		description = "Resource/entity this permission applies to",
		example = "PIPELINE",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 50
	)
	@Size(max = 50, message = "Resource must not exceed 50 characters")
	@Pattern(regexp = "^[A-Z_]*$", message = "Resource must be uppercase with underscores")
	@Column(name="F_03", length=50)
	private String resource;

	@Schema(
		description = "Action/operation this permission allows",
		example = "WRITE",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 20,
		allowableValues = {"READ", "WRITE", "CREATE", "UPDATE", "DELETE", "APPROVE", "EXECUTE", "MANAGE"}
	)
	@Size(max = 20, message = "Action must not exceed 20 characters")
	@Pattern(regexp = "^[A-Z_]*$", message = "Action must be uppercase with underscores")
	@Column(name="F_04", length=20)
	private String action;
}
