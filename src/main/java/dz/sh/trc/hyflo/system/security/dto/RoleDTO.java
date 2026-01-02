/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: RoleDTO
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

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.security.model.Permission;
import dz.sh.trc.hyflo.system.security.model.Role;
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
public class RoleDTO extends GenericDTO<Role> {
    
    private String name;
    private String description;
    private Set<PermissionDTO> permissions;

    @Override
    public Role toEntity() {
        Role entity = Role.builder()
                .name(this.name)
                .description(this.description)
                .build();
        
        if (getId() != null) {
            entity.setId(getId());
        }
        
        if (this.permissions != null && !this.permissions.isEmpty()) {
            Set<Permission> permissionEntities = this.permissions.stream()
                    .map(PermissionDTO::toEntity)
                    .collect(Collectors.toSet());
            entity.setPermissions(permissionEntities);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Role entity) {
        if (this.name != null) entity.setName(this.name);
        if (this.description != null) entity.setDescription(this.description);
        
        if (this.permissions != null) {
            Set<Permission> permissionEntities = this.permissions.stream()
                    .map(PermissionDTO::toEntity)
                    .collect(Collectors.toSet());
            entity.setPermissions(permissionEntities);
        }
    }

    public static RoleDTO fromEntity(Role entity) {
        return fromEntity(entity, true);
    }

    public static RoleDTO fromEntity(Role entity, boolean includePermissions) {
        if (entity == null) return null;
        
        Set<PermissionDTO> permissionDTOs = null;
        if (includePermissions && entity.getPermissions() != null) {
            permissionDTOs = entity.getPermissions().stream()
                    .map(PermissionDTO::fromEntity)
                    .collect(Collectors.toSet());
        }
        
        return RoleDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .permissions(permissionDTOs)
                .build();
    }
}
