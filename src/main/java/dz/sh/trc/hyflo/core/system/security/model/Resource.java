/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Resource
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
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Spring Security GrantedResource implementation for direct authority assignment.
 * Can represent system-level or special authorities beyond role-based permissions.
 */
@Schema(description = "Represents a secured domain object that can be protected by permissions in the RBAC system")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Resource")
@Table(name = "T_00_02_06", uniqueConstraints = {@UniqueConstraint(name = "T_00_02_06_UK_01", columnNames = "F_01")})
public class Resource extends GenericModel {

	@Schema(
		description = "Unique resource code (typically uppercase)",
		example = "ROLE",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 50
	)
	@NotBlank(message = "Resource code is mandatory")
	@Size(max = 50, message = "Resource code must not exceed 50 characters")
	@Pattern(regexp = "^[A-Z_]+$", message = "Resource code must be uppercase with underscores")
	@Column(name="F_01", length=50, nullable=false)
	private String code;

	@Schema(
		description = "Human-readable description of what is this resource",
		example = "Role resource",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "Description must not exceed 200 characters")
	@Column(name="F_02", length=200)
	private String description;

	@Schema(
		description = "Type/category of resource for classification",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Resource type is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_03", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_00_02_06_FK_02"), nullable=false)
	private ResourceType resourceType;
}
