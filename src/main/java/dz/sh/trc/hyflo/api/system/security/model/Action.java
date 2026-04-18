/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Authority
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.api.system.security.model;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Spring Security GrantedAuthority implementation for direct authority assignment.
 * Can represent system-level or special authorities beyond role-based permissions.
 */
@Schema(description = "Spring Security authority for direct privilege assignment and system-level access")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Action")
@Table(name = "T_00_02_04", uniqueConstraints = {@UniqueConstraint(name = "T_00_02_04_UK_01", columnNames = "F_01")})
public class Action extends GenericModel {

	@Schema(
		description = "Unique authority name (typically uppercase)",
		example = "SYSTEM_ADMIN",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 50
	)
	@NotBlank(message = "Authority name is mandatory")
	@Size(max = 50, message = "Authority name must not exceed 50 characters")
	@Pattern(regexp = "^[A-Z_]+$", message = "Authority name must be uppercase with underscores")
	@Column(name="F_01", length=50, nullable=false)
	private String name;

	@Schema(
		description = "Human-readable description of what this authority grants",
		example = "Full system administration privileges including user and security management",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "Description must not exceed 200 characters")
	@Column(name="F_02", length=200)
	private String description;
}
