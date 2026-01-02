/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: UserDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-11-2025
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
import dz.sh.trc.hyflo.system.security.model.Group;
import dz.sh.trc.hyflo.system.security.model.Role;
import dz.sh.trc.hyflo.system.security.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends GenericDTO<User> {
    
    private String username;
    private String email;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    
    private Set<RoleDTO> roles;
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
                .roles(roleDTOs)
                .groups(groupDTOs)
                .build();
    }
}
