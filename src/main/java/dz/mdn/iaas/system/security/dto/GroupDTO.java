/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: GroupDTO
 *	@CreatedOn	: 11-18-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: DTO
 *	@Layer		: DTO
 *	@Package	: System / Security
 *
 **/

package dz.mdn.iaas.system.security.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.mdn.iaas.configuration.template.GenericDTO;
import dz.mdn.iaas.system.security.model.Group;
import dz.mdn.iaas.system.security.model.Role;
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
public class GroupDTO extends GenericDTO<Group> {
    
    private String name;
    private String description;
    private Set<RoleDTO> roles;

    @Override
    public Group toEntity() {
        Group entity = Group.builder()
                .name(this.name)
                .description(this.description)
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
        
        return entity;
    }

    @Override
    public void updateEntity(Group entity) {
        if (this.name != null) entity.setName(this.name);
        if (this.description != null) entity.setDescription(this.description);
        
        if (this.roles != null) {
            Set<Role> roleEntities = this.roles.stream()
                    .map(RoleDTO::toEntity)
                    .collect(Collectors.toSet());
            entity.setRoles(roleEntities);
        }
    }

    public static GroupDTO fromEntity(Group entity) {
        return fromEntity(entity, true);
    }

    public static GroupDTO fromEntity(Group entity, boolean includeRoles) {
        if (entity == null) return null;
        
        Set<RoleDTO> roleDTOs = null;
        if (includeRoles && entity.getRoles() != null) {
            roleDTOs = entity.getRoles().stream()
                    .map(role -> RoleDTO.fromEntity(role, false)) // Avoid circular reference
                    .collect(Collectors.toSet());
        }
        
        return GroupDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .roles(roleDTOs)
                .build();
    }
}
