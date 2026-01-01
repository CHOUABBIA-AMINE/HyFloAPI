/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FolderDTO
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Common / Environment
 *
 **/

package dz.sh.trc.hyflo.common.environment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.sh.trc.hyflo.common.environment.model.Folder;
import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Folder Data Transfer Object
 * Extends GenericDTO for automatic entity conversion
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FolderDTO extends GenericDTO<Folder> {

    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    @NotNull(message = "Archive box ID is required")
    private Long archiveBoxId;

    @Override
    public Folder toEntity() {
        Folder entity = new Folder();
        entity.setId(this.getId());
        entity.setCode(this.code);
        entity.setDescription(this.description);
        return entity;
    }

    @Override
    public void updateEntity(Folder entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.description != null) entity.setDescription(this.description);
    }

    public static FolderDTO fromEntity(Folder entity) {
        if (entity == null) return null;
        return FolderDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .description(entity.getDescription())
                .archiveBoxId(entity.getArchiveBox() != null ? entity.getArchiveBox().getId() : null)
                .build();
    }
}