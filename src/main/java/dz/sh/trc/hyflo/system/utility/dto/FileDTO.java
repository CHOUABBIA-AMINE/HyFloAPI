/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : FileDTO
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-16-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO
 *  @Package    : System / Utility
 *
 **/

package dz.sh.trc.hyflo.system.utility.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.utility.model.File;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for file metadata management.
 * Tracks uploaded files with storage information and classification.
 */
@Schema(description = "Data Transfer Object for file metadata and storage management")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDTO extends GenericDTO<File> {

    @Schema(
        description = "File extension (without dot)",
        example = "pdf",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20,
        pattern = "^[a-zA-Z0-9]+$"
    )
    @NotBlank(message = "File extension is required")
    @Size(max = 20, message = "File extension must not exceed 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "File extension must contain only alphanumeric characters")
    private String extension;

    @Schema(
        description = "File size in bytes",
        example = "2048576",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        minimum = "1"
    )
    @Positive(message = "File size must be positive")
    private Long size;

    @Schema(
        description = "File storage path (relative or absolute)",
        example = "/uploads/documents/2026/01/pipeline-report.pdf",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 250
    )
    @NotBlank(message = "File path is required")
    @Size(max = 250, message = "File path must not exceed 250 characters")
    private String path;

    @Schema(
        description = "File type category for classification",
        example = "DOCUMENT",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 20,
        allowableValues = {"DOCUMENT", "IMAGE", "VIDEO", "AUDIO", "ARCHIVE", "OTHER"}
    )
    @Size(max = 20, message = "File type must not exceed 20 characters")
    private String fileType;

    @Override
    public File toEntity() {
        File file = new File();
        file.setExtension(this.extension);
        file.setSize(this.size);
        file.setPath(this.path);
        file.setFileType(this.fileType);

        if (getId() != null) {
            file.setId(getId());
        }

        return file;
    }

    @Override
    public void updateEntity(File entity) {
        if (this.extension != null) entity.setExtension(this.extension);
        if (this.size != null) entity.setSize(this.size);
        if (this.path != null) entity.setPath(this.path);
        if (this.fileType != null) entity.setFileType(this.fileType);
    }

    public static FileDTO fromEntity(File entity) {
        if (entity == null) return null;

        return FileDTO.builder()
                .id(entity.getId())
                .extension(entity.getExtension())
                .size(entity.getSize())
                .path(entity.getPath())
                .fileType(entity.getFileType())
                .build();
    }

    /**
     * Get human-readable file size
     */
    public String getHumanReadableSize() {
        if (size == null || size == 0) return "0 B";
        
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double fileSize = size;
        
        while (fileSize >= 1024 && unitIndex < units.length - 1) {
            fileSize /= 1024;
            unitIndex++;
        }
        
        return String.format("%.2f %s", fileSize, units[unitIndex]);
    }
}
