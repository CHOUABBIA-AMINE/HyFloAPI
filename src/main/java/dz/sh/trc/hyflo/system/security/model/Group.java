/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Group
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

import java.util.HashSet;
import java.util.Set;

/**
 * User group entity for organizing users and assigning roles collectively.
 * Examples: Pipeline Engineers, Operations Managers, Safety Inspectors.
 */
@Schema(description = "User group for collective role assignment and organization management")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Group")
@Table(name = "T_00_02_01", uniqueConstraints = {@UniqueConstraint(name = "T_00_02_01_UK_01", columnNames = "F_01")})
public class Group extends GenericModel {

	@Schema(
		description = "Unique group name (typically uppercase with underscores)",
		example = "PIPELINE_ENGINEERS",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 50
	)
	@NotBlank(message = "Group name is mandatory")
	@Size(min = 3, max = 50, message = "Group name must be between 3 and 50 characters")
	@Pattern(regexp = "^[A-Z_]+$", message = "Group name must be uppercase with underscores (e.g., PIPELINE_ENGINEERS)")
	@Column(name="F_01", length=50, nullable=false)
	private String name;

	@Schema(
		description = "Human-readable description of the group and its purpose",
		example = "Pipeline integrity engineers responsible for monitoring and maintenance",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "Description must not exceed 200 characters")
	@Column(name="F_02", length=200)
	private String description;

	@Schema(
		description = "Set of roles assigned to all users in this group",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Builder.Default
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "R_T000201_T000203",
		joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T000201_T000203_FK_01")),
		inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T000201_T000203_FK_02")),
		uniqueConstraints = @UniqueConstraint(name = "R_T000201_T000203_UK_01", columnNames = {"F_01", "F_02"})
	)
	private Set<Role> roles = new HashSet<>();
}
