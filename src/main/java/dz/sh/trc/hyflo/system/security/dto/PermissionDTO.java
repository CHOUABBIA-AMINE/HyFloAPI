/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PermissionDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-15-2026 - Added validation and documentation
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.security.model.Permission;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for fine-grained permission management")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionDTO extends GenericDTO<Permission> {
    
    @Schema(
        description = "Unique permission name in format RESOURCE_ACTION",
        example = "PIPELINE_WRITE",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Permission name is required")
    @Size(max = 100, message = "Permission name must not exceed 100 characters")
    @Pattern(regexp = "^[A-Z_]+$", message = "Permission name must be uppercase with underscores")
    private String name;

    @Schema(
        description = "Human-readable description of what this permission grants",
        example = "Allows creating, updating, and modifying pipeline data",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 200
    )
    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    @Schema(
        description = "Resource/entity this permission applies to",
        example = "PIPELINE",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 50
    )
    @Size(max = 50, message = "Resource must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z_]*$", message = "Resource must be uppercase with underscores")
    private String resource;

    @Schema(
        description = "Action/operation this permission allows",
        example = "WRITE",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 20,
        allowableValues = {"READ", "WRITE", "CREATE", "UPDATE", "DELETE", "APPROVE", "EXECUTE", "MANAGE"}
    )
    @Size(max = 20, message = "Action must not exceed 20 characters")
    @Pattern(regexp = "^[A-Z_]*$", message = "Action must be uppercase with underscores")
    private String action;

    @Override
    public Permission toEntity() {
        Permission entity = Permission.builder()
                .name(this.name)
                .description(this.description)
                .resource(this.resource)
                .action(this.action)
                .build();
        if (getId() != null) {
            entity.setId(getId());
        }
        return entity;
    }

    @Override
    public void updateEntity(Permission entity) {
        if (this.name != null) entity.setName(this.name);
        if (this.description != null) entity.setDescription(this.description);
        if (this.resource != null) entity.setResource(this.resource);
        if (this.action != null) entity.setAction(this.action);
    }

    public static PermissionDTO fromEntity(Permission entity) {
        if (entity == null) return null;
        
        return PermissionDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .resource(entity.getResource())
                .action(entity.getAction())
                .build();
    }
}
