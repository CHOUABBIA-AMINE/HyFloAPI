/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: RoleDTO
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

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.security.model.Permission;
import dz.sh.trc.hyflo.system.security.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for role-based access control (RBAC) management")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO extends GenericDTO<Role> {
    
    @Schema(
        description = "Unique role name (must start with ROLE_ prefix followed by uppercase identifier)",
        example = "ROLE_PIPELINE_ENGINEER",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 50,
        pattern = "^ROLE_[A-Z0-9_]+$"
    )
    @NotBlank(message = "Role name is required")
    @Size(max = 50, message = "Role name must not exceed 50 characters")
    @Pattern(regexp = "^ROLE_[A-Z0-9_]+$", message = "Role name must follow format ROLE_NAME_FORMAT with uppercase letters, numbers, and underscores")
    private String name;

    @Schema(
        description = "Human-readable description of the role and its purpose",
        example = "Pipeline engineers responsible for pipeline integrity and operations",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 200
    )
    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    @Schema(
        description = "Set of permissions granted by this role",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
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
