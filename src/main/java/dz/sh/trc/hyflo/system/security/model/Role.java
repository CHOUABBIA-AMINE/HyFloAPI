/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Role
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Role entity for Role-Based Access Control (RBAC).
 * Aggregates permissions and can be assigned to users directly or through groups.
 */
@Schema(description = "Security role aggregating permissions for RBAC authorization")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Role")
@Table(name = "T_00_02_03", uniqueConstraints = {@UniqueConstraint(name = "T_00_02_03_UK_01", columnNames = "F_01")})
public class Role extends GenericModel {

	@Schema(
		description = "Unique role name (typically uppercase with ROLE_ prefix)",
		example = "ROLE_PIPELINE_ENGINEER",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 50
	)
	@NotBlank(message = "Role name is mandatory")
	@Size(max = 50, message = "Role name must not exceed 50 characters")
	@Pattern(regexp = "^ROLE_[A-Z0-9_]+$", message = "Role name must follow format ROLE_NAME_FORMAT")
	@Column(name="F_01", length=50, nullable=false)
	private String name;

	@Schema(
		description = "Human-readable description of the role and its purpose",
		example = "Pipeline engineers responsible for pipeline integrity and operations",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "Description must not exceed 200 characters")
	@Column(name="F_02", length=200)
	private String description;

	@Schema(
		description = "Set of permissions granted by this role",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Builder.Default
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "R_T000203_T000204",
		joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T000203_T000204_FK_01")),
		inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T000203_T000204_FK_02")),
		uniqueConstraints = @UniqueConstraint(name = "R_T000203_T000204_UK_01", columnNames = {"F_01", "F_02"})
	)
	private Set<Permission> permissions = new HashSet<>();

	/**
	 * Converts permissions to Spring Security GrantedAuthority objects
	 * @return Set of granted authorities for Spring Security
	 */
	public Set<GrantedAuthority> getAuthorities() {
		return permissions.stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getName()))
				.collect(Collectors.toSet());
	}
}
