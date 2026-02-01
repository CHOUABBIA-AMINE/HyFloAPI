/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: TokenRefreshResponse
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Authentication
 *
 **/

package dz.sh.trc.hyflo.system.auth.dto;

import java.util.Set;
import java.util.stream.Collectors;

import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.system.security.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    
    // User basic info
    private Long id;
    private String username;
    private String email;
    private Boolean enabled;
    private Boolean accountNonLocked;
    
    // Employee details
    private EmployeeDTO employee;
    
    // Authorization
    private Set<String> roles;
    private Set<String> permissions;
    private Set<String> groups;
    
    /**
     * Convert User entity to UserProfileDTO
     */
    public static UserProfileDTO fromEntity(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
        
        // Collect all permissions from user's roles and groups
        Set<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> permission.getName())
                .collect(Collectors.toSet());
        
        // Add group permissions
        Set<String> groupPermissions = user.getGroups().stream()
        		.flatMap(group -> group.getRoles().stream())
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> permission.getName())
                .collect(Collectors.toSet());
        permissions.addAll(groupPermissions);
        
        Set<String> groups = user.getGroups().stream()
                .map(group -> group.getName())
                .collect(Collectors.toSet());
        
        UserProfileDTOBuilder builder = UserProfileDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .roles(roles)
                .permissions(permissions)
                .groups(groups);
        
        // Add employee details if available
        if (user.getEmployee() != null) {
            builder.employee(EmployeeDTO.fromEntity(user.getEmployee()));
        }
        
        return builder.build();
    }
}
