/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: UserDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-15-2026 - Added validation and documentation
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.system.security.model.Group;
import dz.sh.trc.hyflo.system.security.model.Role;
import dz.sh.trc.hyflo.system.security.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for user account management with Spring Security integration")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends GenericDTO<User> {
    
    @Schema(
        description = "Unique username for authentication (letters, numbers, dots, hyphens, and underscores only)",
        example = "medabir",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 3,
        maxLength = 20,
        pattern = "^[a-zA-Z0-9._-]{3,20}$"
    )
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,20}$", message = "Username must contain only letters, numbers, dots, hyphens, and underscores")
    private String username;

    @Schema(
        description = "User email address for notifications and account recovery",
        example = "abir.medjerab@sonatrach.dz",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100,
        format = "email"
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @Schema(
        description = "User password (must be at least 8 characters, will be BCrypt encrypted)",
        example = "SecurePassword123!",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 8,
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{8,120}$")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    @Schema(
        description = "Indicates if the user account is enabled and can authenticate",
        example = "true",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = "true"
    )
    private Boolean enabled;

    @Schema(
        description = "Indicates if the account has not expired (account lifecycle management)",
        example = "true",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = "true"
    )
    private Boolean accountNonExpired;

    @Schema(
        description = "Indicates if the account is not locked (security lockout management)",
        example = "true",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = "true"
    )
    private Boolean accountNonLocked;

    @Schema(
        description = "Indicates if the credentials (password) have not expired",
        example = "true",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = "true"
    )
    private Boolean credentialsNonExpired;
    
    @Schema(
        description = "ID of the associated SONATRACH employee record",
        example = "123",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long employeeId;
    
    @Schema(
        description = "Full employee details (populated when fetching user with employee information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private EmployeeDTO employee;
    
    @Schema(
        description = "Set of roles directly assigned to this user",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Set<RoleDTO> roles;

    @Schema(
        description = "Set of groups the user belongs to (each group contains roles)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Set<GroupDTO> groups;

    @Override
    public User toEntity() {
        User entity = User.builder()
                .username(this.username)
                .email(this.email)
                .password(this.password)
                .enabled(this.enabled != null ? this.enabled : true)
                .accountNonExpired(this.accountNonExpired != null ? this.accountNonExpired : true)
                .accountNonLocked(this.accountNonLocked != null ? this.accountNonLocked : true)
                .credentialsNonExpired(this.credentialsNonExpired != null ? this.credentialsNonExpired : true)
                .build();
        
        if (getId() != null) {
            entity.setId(getId());
        }
        
        if (this.employeeId != null) {
        	Employee employee = new Employee();
        	employee.setId(this.employeeId);
            entity.setEmployee(employee);
        }
        
        if (this.roles != null && !this.roles.isEmpty()) {
            Set<Role> roleEntities = this.roles.stream()
                    .map(RoleDTO::toEntity)
                    .collect(Collectors.toSet());
            entity.setRoles(roleEntities);
        }
        
        if (this.groups != null && !this.groups.isEmpty()) {
            Set<Group> groupEntities = this.groups.stream()
                    .map(GroupDTO::toEntity)
                    .collect(Collectors.toSet());
            entity.setGroups(groupEntities);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(User entity) {
        if (this.username != null) entity.setUsername(this.username);
        if (this.email != null) entity.setEmail(this.email);
        if (this.password != null) entity.setPassword(this.password);
        if (this.enabled != null) entity.setEnabled(this.enabled);
        if (this.accountNonExpired != null) entity.setAccountNonExpired(this.accountNonExpired);
        if (this.accountNonLocked != null) entity.setAccountNonLocked(this.accountNonLocked);
        if (this.credentialsNonExpired != null) entity.setCredentialsNonExpired(this.credentialsNonExpired);
        
        if (this.employeeId != null) {
        	Employee employee = new Employee();
        	employee.setId(this.employeeId);
            entity.setEmployee(employee);
        }
        
        if (this.roles != null) {
            Set<Role> roleEntities = this.roles.stream()
                    .map(RoleDTO::toEntity)
                    .collect(Collectors.toSet());
            entity.setRoles(roleEntities);
        }
        
        if (this.groups != null) {
            Set<Group> groupEntities = this.groups.stream()
                    .map(GroupDTO::toEntity)
                    .collect(Collectors.toSet());
            entity.setGroups(groupEntities);
        }
    }

    public static UserDTO fromEntity(User entity) {
        return fromEntity(entity, true, true);
    }

    public static UserDTO fromEntity(User entity, boolean includeRoles, boolean includeGroups) {
        if (entity == null) return null;
        
        Set<RoleDTO> roleDTOs = null;
        if (includeRoles && entity.getRoles() != null) {
            roleDTOs = entity.getRoles().stream()
                    .map(role -> RoleDTO.fromEntity(role, false)) // Avoid deep nesting
                    .collect(Collectors.toSet());
        }
        
        Set<GroupDTO> groupDTOs = null;
        if (includeGroups && entity.getGroups() != null) {
            groupDTOs = entity.getGroups().stream()
                    .map(group -> GroupDTO.fromEntity(group, false)) // Avoid deep nesting
                    .collect(Collectors.toSet());
        }
        
        return UserDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                // Password is intentionally not included in fromEntity for security
                .enabled(entity.isEnabled())
                .accountNonExpired(entity.isAccountNonExpired())
                .accountNonLocked(entity.isAccountNonLocked())
                .credentialsNonExpired(entity.isCredentialsNonExpired())
                .employeeId(entity.getEmployee() != null ? entity.getEmployee().getId() : null)
                
                .employee(entity.getEmployee() != null ? EmployeeDTO.fromEntity(entity.getEmployee()) : null)
                .roles(roleDTOs)
                .groups(groupDTOs)
                .build();
    }
}
