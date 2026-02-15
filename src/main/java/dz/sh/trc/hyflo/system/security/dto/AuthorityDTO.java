/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuthorityDTO
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
import dz.sh.trc.hyflo.system.security.model.Authority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for Spring Security authority management")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorityDTO extends GenericDTO<Authority> {
    
    @Schema(
        description = "Unique authority name (typically uppercase with underscores)",
        example = "SYSTEM_ADMIN",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 50
    )
    @NotBlank(message = "Authority name is required")
    @Size(max = 50, message = "Authority name must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z_]+$", message = "Authority name must be uppercase with underscores")
    private String name;

    @Schema(
        description = "Human-readable description of what this authority grants",
        example = "Full system administration privileges including user and security management",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 200
    )
    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    @Schema(
        description = "Type/category of authority for classification",
        example = "SYSTEM",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 50,
        allowableValues = {"SYSTEM", "APPLICATION", "MODULE", "SPECIAL", "TEMPORARY"}
    )
    @Size(max = 50, message = "Type must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z_]*$", message = "Type must be uppercase with underscores")
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
