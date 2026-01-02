/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: AuthorityDTO
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
import dz.sh.trc.hyflo.system.security.model.Authority;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorityDTO extends GenericDTO<Authority> {
    
    private String name;
    private String description;
    private String type;

    @Override
    public Authority toEntity() {
        Authority entity = Authority.builder()
                .name(this.name)
                .description(this.description)
                .type(this.type)
                .build();
        if (getId() != null) {
            entity.setId(getId());
        }
        return entity;
    }

    @Override
    public void updateEntity(Authority entity) {
        if (this.name != null) entity.setName(this.name);
        if (this.description != null) entity.setDescription(this.description);
        if (this.type != null) entity.setType(this.type);
    }

    public static AuthorityDTO fromEntity(Authority entity) {
        if (entity == null) return null;
        
        return AuthorityDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .type(entity.getType())
                .build();
    }
}
