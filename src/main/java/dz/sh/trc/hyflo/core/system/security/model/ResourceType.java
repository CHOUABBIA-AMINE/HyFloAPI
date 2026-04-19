/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ResourceType
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 04-18-2026
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
 * Represents a classification category for resources.
 * Used to group resources by domain (e.g., SECURITY, BUSINESS, AI).
 */
@Schema(description = "Classification type for resources, used to group them by domain (e.g., SECURITY, BUSINESS, AI)")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ResourceType")
@Table(name = "T_00_02_05", uniqueConstraints = {@UniqueConstraint(name = "T_00_02_05_UK_01", columnNames = "F_01")})
public class ResourceType extends GenericModel {

	@Schema(
		description = "Domain classification (SECURITY, BUSINESS, AI)",
		example = "SECURITY",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 50
	)
	@NotBlank(message = "Resource type name is mandatory")
	@Size(max = 50, message = "Resource type name must not exceed 50 characters")
	@Pattern(regexp = "^[A-Z0-9_]+$", message = "Resource type name must be uppercase with underscores")
	@Column(name="F_01", length=50, nullable=false)
	private String name;

	@Schema(
		description = "Human-readable description of what is this resource type",
		example = "SECURITY type",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "Description must not exceed 200 characters")
	@Column(name="F_02", length=200)
	private String description;
	
	@Schema(
		description = "Define if this reousrce type is used or not",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Active is mandatory")
	@Builder.Default
	@Column(name="F_03", nullable=false)
	private Boolean active = true;
	
	@Schema(
		description = "Define if this reousrce type can be deleted or not",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Protected type is mandatory")
	@Builder.Default
	@Column(name="F_04", nullable=false)
	private Boolean protectedType = false;
}
