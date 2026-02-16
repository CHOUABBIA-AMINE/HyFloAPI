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

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.utility.model.File;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for file metadata management.
 * Tracks uploaded files with metadata, validation, and access control.
 */
@Schema(description = "Data Transfer Object for file metadata, upload tracking, and access management")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDTO extends GenericDTO<File> {

    @Schema(
        description = "Original filename as uploaded by user",
        example = "pipeline_schematic_P1234.pdf",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 255
    )
    @NotBlank(message = "Filename is required")
    @Size(max = 255, message = "Filename must not exceed 255 characters")
    private String filename;

    @Schema(
        description = "System-generated unique filename for storage (prevents conflicts)",
        example = "20260216_193000_abc123def456_pipeline_schematic_P1234.pdf",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 500
    )
    @Size(max = 500, message = "Stored filename must not exceed 500 characters")
    private String storedFilename;

    @Schema(
        description = "Storage path relative to file storage root",
        example = "/uploads/2026/02/pipelines/",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 500
    )
    @Size(max = 500, message = "File path must not exceed 500 characters")
    @Pattern(
        regexp = "^(/[a-zA-Z0-9_-]+)+/?$",
        message = "File path must be a valid directory path"
    )
    private String filePath;

    @Schema(
        description = "File size in bytes",
        example = "2048576",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        minimum = "0"
    )
    @PositiveOrZero(message = "File size must be zero or positive")
    private Long fileSize;

    @Schema(
        description = "MIME type of the file",
        example = "application/pdf",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100,
        allowableValues = {
            "application/pdf",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "image/jpeg",
            "image/png",
            "image/gif",
            "text/plain",
            "text/csv"
        }
    )
    @Size(max = 100, message = "MIME type must not exceed 100 characters")
    @Pattern(
        regexp = "^[a-z]+/[a-z0-9.+-]+$",
        message = "MIME type must be in format: type/subtype"
    )
    private String mimeType;

    @Schema(
        description = "File extension (without dot)",
        example = "pdf",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 10,
        pattern = "^[a-z0-9]+$"
    )
    @Size(max = 10, message = "File extension must not exceed 10 characters")
    @Pattern(regexp = "^[a-z0-9]+$", message = "File extension must be lowercase alphanumeric")
    private String fileExtension;

    @Schema(
        description = "Type/category of file for organization",
        example = "DOCUMENT",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 50,
        allowableValues = {"DOCUMENT", "IMAGE", "SPREADSHEET", "PRESENTATION", "VIDEO", "AUDIO", "ARCHIVE", "OTHER"}
    )
    @Size(max = 50, message = "File type must not exceed 50 characters")
    private String fileType;

    @Schema(
        description = "Optional description or notes about the file",
        example = "Pipeline schematic for sector 7 - Hassi Messaoud",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 1000
    )
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Schema(
        description = "Username of the user who uploaded the file",
        example = "achouabbia",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Uploaded by must not exceed 100 characters")
    private String uploadedBy;

    @Schema(
        description = "Timestamp when the file was uploaded",
        example = "2026-02-16T19:30:00",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        format = "yyyy-MM-dd'T'HH:mm:ss",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime uploadedAt;

    @Schema(
        description = "ID of related entity (e.g., Pipeline ID, Station ID)",
        example = "1234",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Positive(message = "Related entity ID must be positive")
    private Long relatedEntityId;

    @Schema(
        description = "Type of related entity for navigation",
        example = "Pipeline",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 50
    )
    @Size(max = 50, message = "Related entity type must not exceed 50 characters")
    private String relatedEntityType;

    @Schema(
        description = "Indicates whether the file is accessible publicly or restricted",
        example = "false",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = "false"
    )
    private Boolean isPublic;

    @Override
    public File toEntity() {
        File file = File.builder()
                .filename(this.filename)
                .storedFilename(this.storedFilename)
                .filePath(this.filePath)
                .fileSize(this.fileSize)
                .mimeType(this.mimeType)
                .fileExtension(this.fileExtension)
                .fileType(this.fileType)
                .description(this.description)
                .uploadedBy(this.uploadedBy)
                .uploadedAt(this.uploadedAt != null ? this.uploadedAt : LocalDateTime.now())
                .relatedEntityId(this.relatedEntityId)
                .relatedEntityType(this.relatedEntityType)
                .isPublic(this.isPublic != null ? this.isPublic : false)
                .build();

        if (getId() != null) {
            file.setId(getId());
        }

        return file;
    }

    @Override
    public void updateEntity(File entity) {
        if (this.filename != null) entity.setFilename(this.filename);
        if (this.storedFilename != null) entity.setStoredFilename(this.storedFilename);
        if (this.filePath != null) entity.setFilePath(this.filePath);
        if (this.fileSize != null) entity.setFileSize(this.fileSize);
        if (this.mimeType != null) entity.setMimeType(this.mimeType);
        if (this.fileExtension != null) entity.setFileExtension(this.fileExtension);
        if (this.fileType != null) entity.setFileType(this.fileType);
        if (this.description != null) entity.setDescription(this.description);
        if (this.uploadedBy != null) entity.setUploadedBy(this.uploadedBy);
        if (this.uploadedAt != null) entity.setUploadedAt(this.uploadedAt);
        if (this.relatedEntityId != null) entity.setRelatedEntityId(this.relatedEntityId);
        if (this.relatedEntityType != null) entity.setRelatedEntityType(this.relatedEntityType);
        if (this.isPublic != null) entity.setIsPublic(this.isPublic);
    }

    public static FileDTO fromEntity(File entity) {
        if (entity == null) return null;

        return FileDTO.builder()
                .id(entity.getId())
                .filename(entity.getFilename())
                .storedFilename(entity.getStoredFilename())
                .filePath(entity.getFilePath())
                .fileSize(entity.getFileSize())
                .mimeType(entity.getMimeType())
                .fileExtension(entity.getFileExtension())
                .fileType(entity.getFileType())
                .description(entity.getDescription())
                .uploadedBy(entity.getUploadedBy())
                .uploadedAt(entity.getUploadedAt())
                .relatedEntityId(entity.getRelatedEntityId())
                .relatedEntityType(entity.getRelatedEntityType())
                .isPublic(entity.getIsPublic())
                .build();
    }

    /**
     * Get human-readable file size
     */
    public String getHumanReadableSize() {
        if (fileSize == null || fileSize == 0) return "0 B";
        
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = fileSize;
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        return String.format("%.2f %s", size, units[unitIndex]);
    }
}
