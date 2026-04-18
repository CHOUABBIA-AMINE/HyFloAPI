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

package dz.sh.trc.hyflo.core.system.security.model;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Fine-grained permission for access control.
 * Permissions are aggregated into roles. Format: RESOURCE_ACTION (e.g., PIPELINE_READ, PIPELINE_WRITE).
 */
@Schema(description = "Fine-grained permission for resource-level access control within the system")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Permission")
@Table(name = "T_00_02_07", uniqueConstraints = {@UniqueConstraint(name = "T_00_02_07_UK_01", columnNames = "F_01")})
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
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Resource is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_03", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_00_02_07_FK_01"), nullable=false)
	private Resource resource;
	
	@Schema(
		description = "Action this permission grants with",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Action is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_04", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_00_02_07_FK_02"), nullable=false)
	private Action action;
}
