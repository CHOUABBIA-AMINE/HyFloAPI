/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: GroupDTO
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
import dz.sh.trc.hyflo.system.security.model.Group;
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

@Schema(description = "Data Transfer Object for user group management and collective role assignment")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDTO extends GenericDTO<Group> {
    
    @Schema(
        description = "Unique group name (typically uppercase with underscores)",
        example = "PIPELINE_ENGINEERS",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 3,
        maxLength = 50,
        pattern = "^[A-Z_]+$"
    )
    @NotBlank(message = "Group name is required")
    @Size(min = 3, max = 50, message = "Group name must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Z_]+$", message = "Group name must be uppercase with underscores (e.g., PIPELINE_ENGINEERS)")
    private String name;

    @Schema(
        description = "Human-readable description of the group and its purpose",
        example = "Pipeline integrity engineers responsible for monitoring and maintenance",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 200
    )
    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    @Schema(
        description = "Set of roles assigned to all users in this group",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
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
