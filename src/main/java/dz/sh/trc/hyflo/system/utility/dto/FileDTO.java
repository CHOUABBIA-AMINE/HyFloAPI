/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: FileDTO
 *	@CreatedOn	: 06-26-2023
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Utility
 *
 **/

package dz.sh.trc.hyflo.system.utility.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.utility.model.File;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * File Data Transfer Object - Extends GenericDTO
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - extension (F_01) - required
 * - size (F_02) - must not be negative
 * - path (F_03) - required
 * - fileType (F_04) - optional
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDTO extends GenericDTO<File> {

    @NotBlank(message = "Extension is required")
    @Size(max = 20, message = "Extension must not exceed 20 characters")
    private String extension;

    @Min(value = 0, message = "Size must not be negative")
    private Long size;

    @NotBlank(message = "Path is required")
    @Size(max = 250, message = "Path must not exceed 250 characters")
    private String path;

    @Size(max = 20, message = "File type must not exceed 20 characters")
    private String fileType;

    // ========== GENERIC DTO IMPLEMENTATION ==========

    @Override
    public File toEntity() {
        File file = new File();
        file.setId(getId());
        file.setExtension(this.extension);
        file.setSize(this.size != null ? this.size : 0L);
        file.setPath(this.path);
        file.setFileType(this.fileType);
        return file;
    }

    @Override
    public void updateEntity(File file) {
        if (this.extension != null) {
            file.setExtension(this.extension);
        }
        if (this.size != null) {
            file.setSize(this.size);
        }
        if (this.path != null) {
            file.setPath(this.path);
        }
        if (this.fileType != null) {
            file.setFileType(this.fileType);
        }
    }

    // ========== FACTORY METHOD ==========

    /**
     * Create DTO from entity
     */
    public static FileDTO fromEntity(File file) {
        if (file == null) return null;
        
        return FileDTO.builder()
                .id(file.getId())
                .extension(file.getExtension())
                .size(file.getSize())
                .path(file.getPath())
                .fileType(file.getFileType())
                .build();
    }
}
