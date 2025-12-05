/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FileDTO
 *	@CreatedOn	: 10-14-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Utility
 *
 **/

package dz.mdn.iaas.system.utility.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.mdn.iaas.system.utility.model.File;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDTO {

    private Long id; // F_00

    @NotBlank(message = "Extension is required")
    @Size(max = 20, message = "Extension must not exceed 20 characters")
    private String extension; // F_01

    @Min(value = 0, message = "Size must not be negative")
    private Long size; // F_02

    @NotBlank(message = "Path is required")
    @Size(max = 250, message = "Path must not exceed 250 characters")
    private String path; // F_03

    @Size(max = 20, message = "File type must not exceed 20 characters")
    private String fileType; // F_04

    // Additional fields for API responses
    private String fileName;
    private String sizeFormatted;
    private String downloadUrl;
    private String contentType;
    private Boolean exists;

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
                .fileName(extractFileName(file.getPath()))
                .sizeFormatted(formatSize(file.getSize()))
                .downloadUrl("/file/" + file.getId() + "/content")
                .contentType(getContentType(file.getExtension()))
                .build();
    }

    /**
     * Convert to entity
     */
    public File toEntity() {
        File file = new File();
        file.setId(this.id);
        file.setExtension(this.extension);
        file.setSize(this.size != null ? this.size : 0);
        file.setPath(this.path);
        file.setFileType(this.fileType);
        return file;
    }

    private static String extractFileName(String path) {
        if (path == null) return null;
        return java.nio.file.Paths.get(path).getFileName().toString();
    }

    private static String formatSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.1f MB", size / (1024.0 * 1024));
        return String.format("%.1f GB", size / (1024.0 * 1024 * 1024));
    }

    private static String getContentType(String extension) {
        if (extension == null) return "application/octet-stream";
        return switch (extension.toLowerCase()) {
            case "pdf" -> "application/pdf";
            case "txt" -> "text/plain";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "json" -> "application/json";
            case "xml" -> "application/xml";
            case "zip" -> "application/zip";
            default -> "application/octet-stream";
        };
    }
}
