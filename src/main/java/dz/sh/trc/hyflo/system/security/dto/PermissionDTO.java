/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PermissionDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-11-2025
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
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionDTO extends GenericDTO<Permission> {
    
    private String name;
    private String description;
    private String resource;
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
