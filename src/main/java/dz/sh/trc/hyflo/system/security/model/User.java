/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: User
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * User account entity implementing Spring Security UserDetails.
 * Represents system users with authentication, authorization, and employee linkage.
 */
@Schema(description = "User account with Spring Security integration, roles, groups, and employee association")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="User")
@Table(name = "T_00_02_02", uniqueConstraints = {@UniqueConstraint(name = "T_00_02_02_UK_01", columnNames = "F_01"),
												 @UniqueConstraint(name = "T_00_02_02_UK_02", columnNames = "F_02")})
public class User extends GenericModel implements UserDetails {

	private static final long serialVersionUID = 6957215815941701487L;

	@Schema(
		description = "Unique username for authentication",
		example = "medabir",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 20
	)
	@NotBlank(message = "Username is mandatory")
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
	@Pattern(regexp = "^[a-zA-Z0-9._-]{3,20}$", message = "Username must contain only letters, numbers, dots, hyphens, and underscores")
	@Column(name="F_01", length=20, nullable=false)
	private String username;

	@Schema(
		description = "User email address for notifications and recovery",
		example = "abir.medjerab@sonatrach.dz",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email must be valid")
	@Size(max = 100, message = "Email must not exceed 100 characters")
	@Column(name="F_02", length=100, nullable=false)
	private String email;

	@Schema(
		description = "Encrypted password (BCrypt hashed)",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 120,
		accessMode = Schema.AccessMode.WRITE_ONLY
	)
	@NotBlank(message = "Password is mandatory")
	@Size(min = 8, message = "Password must be at least 8 characters")
	@JsonIgnore
	@Column(name="F_03", length=120, nullable=false)
	private String password;

	@Schema(
		description = "Indicates if the account has not expired",
		example = "true",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@Builder.Default
	@Column(name="F_04", nullable=false)
	private boolean accountNonExpired = true;
	
	@Schema(
		description = "Indicates if the account is not locked",
		example = "true",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@Builder.Default
	@Column(name="F_05", nullable=false)
	private boolean accountNonLocked = true;
	
	@Schema(
		description = "Indicates if the credentials have not expired",
		example = "true",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@Builder.Default
	@Column(name="F_06", nullable=false)
	private boolean credentialsNonExpired = true;
	
	@Schema(
		description = "Indicates if the user account is enabled",
		example = "true",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@Builder.Default
	@Column(name="F_07", nullable=false)
	private boolean enabled = true;

	@Schema(
		description = "Associated SONATRACH employee record",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name="F_08", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_00_02_02_FK_01"), nullable = true)
	private Employee employee;
	
	@Schema(
		description = "Set of roles directly assigned to this user",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Builder.Default
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "R_T000202_T000203",
		joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T000202_T000203_FK_01")),
		inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T000202_T000203_FK_02")),
		uniqueConstraints = @UniqueConstraint(name = "R_T000202_T000203_UK_01", columnNames = {"F_01", "F_02"})
	)
	private Set<Role> roles = new HashSet<>();

	@Schema(
		description = "Set of groups the user belongs to (each group contains roles)",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Builder.Default
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "R_T000202_T000201",
		joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T000202_T000201_FK_01")),
		inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T000202_T000201_FK_02")),
		uniqueConstraints = @UniqueConstraint(name = "R_T000202_T000201_UK_01", columnNames = {"F_01", "F_02"})
	)
	private Set<Group> groups = new HashSet<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		
		// Add roles
		for (Role role : roles) {
			authorities.addAll(role.getAuthorities());
		}
		
		// Add groups and their roles
		for (Group group : groups) {
			for (Role groupRole : group.getRoles()) {
				authorities.addAll(groupRole.getAuthorities());
			}
		}
		
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
