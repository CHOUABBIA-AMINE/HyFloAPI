/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ArchiveBoxDTO
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Common / Environment
 *
 **/

package dz.mdn.iaas.common.environment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.mdn.iaas.common.environment.model.ArchiveBox;
import dz.mdn.iaas.configuration.template.GenericDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * ArchiveBox Data Transfer Object
 * Extends GenericDTO for automatic entity conversion
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArchiveBoxDTO extends GenericDTO<ArchiveBox> {

    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    @NotNull(message = "Shelf floor ID is required")
    private Long shelfFloorId;

    @Override
    public ArchiveBox toEntity() {
        ArchiveBox entity = new ArchiveBox();
        entity.setId(this.getId());
        entity.setCode(this.code);
        entity.setDescription(this.description);
        return entity;
    }

    @Override
    public void updateEntity(ArchiveBox entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.description != null) entity.setDescription(this.description);
    }

    public static ArchiveBoxDTO fromEntity(ArchiveBox entity) {
        if (entity == null) return null;
        return ArchiveBoxDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .description(entity.getDescription())
                .shelfFloorId(entity.getShelfFloor() != null ? entity.getShelfFloor().getId() : null)
                .build();
    }
}