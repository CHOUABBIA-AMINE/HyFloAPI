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

package dz.sh.trc.hyflo.system.security.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Spring Security GrantedAuthority implementation for direct authority assignment.
 * Can represent system-level or special authorities beyond role-based permissions.
 */
@Schema(description = "Spring Security authority for direct privilege assignment and system-level access")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Authority")
@Table(name = "T_00_02_05", uniqueConstraints = {@UniqueConstraint(name = "T_00_02_05_UK_01", columnNames = "F_01")})
public class Authority extends GenericModel {

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

	@Schema(
		description = "Type/category of authority for classification",
		example = "SYSTEM",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 50,
		allowableValues = {"SYSTEM", "APPLICATION", "MODULE", "SPECIAL", "TEMPORARY"}
	)
	@Size(max = 50, message = "Type must not exceed 50 characters")
	@Pattern(regexp = "^[A-Z_]*$", message = "Type must be uppercase with underscores")
	@Column(name="F_03", length=50)
	private String type;
}
