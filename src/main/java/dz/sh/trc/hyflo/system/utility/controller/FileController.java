/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FileController
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: System / Utility
 *
 **/

package dz.sh.trc.hyflo.system.utility.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.utility.dto.FileDTO;
import dz.sh.trc.hyflo.system.utility.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/utility/file")
@Slf4j
public class FileController extends GenericController<FileDTO, Long> {

    private final FileService fileService;

    public FileController(FileService fileService) {
        super(fileService, "File");
        this.fileService = fileService;
    }

    // ========== PHYSICAL FILE OPERATIONS ==========

    /**
     * Upload single file
     * POST /system/file/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<FileDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "fileType", required = false) String fileType) {
        log.info("REST request to upload file: {}", file.getOriginalFilename());
        
        FileDTO uploadedFile = fileService.uploadFile(file, fileType);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadedFile);
    }

    /**
     * Upload multiple files
     * POST /system/file/upload/batch
     */
    @PostMapping("/upload/batch")
    public ResponseEntity<Map<String, Object>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "fileType", required = false) String fileType) {
        log.info("REST request to upload {} files", files.size());
        
        List<FileDTO> uploadedFiles = fileService.uploadFiles(files, fileType);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "uploadedCount", uploadedFiles.size(),
            "totalCount", files.size(),
            "files", uploadedFiles
        ));
    }

    /**
     * Download file
     * GET /system/file/{id}/download
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        log.info("REST request to download file with id: {}", id);
        
        Resource resource = fileService.downloadFile(id);
        FileDTO fileDTO = fileService.getById(id);
        
        // Get content type from service
        String contentType = fileService.getContentType(fileDTO.getExtension());
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * Replace file content
     * PUT /system/file/{id}/replace
     */
    @PutMapping("/{id}/replace")
    public ResponseEntity<FileDTO> replaceFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        log.info("REST request to replace file with id: {}", id);
        
        FileDTO replacedFile = fileService.replaceFile(id, file);
        
        return ResponseEntity.ok(replacedFile);
    }

    /**
     * Copy file
     * POST /system/file/{id}/copy
     */
    @PostMapping("/{id}/copy")
    public ResponseEntity<FileDTO> copyFile(
            @PathVariable Long id,
            @RequestParam(value = "fileType", required = false) String fileType) {
        log.info("REST request to copy file with id: {}", id);
        
        FileDTO copiedFile = fileService.copyFile(id, fileType);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(copiedFile);
    }

    /**
     * Delete file physically (overrides standard delete)
     * DELETE /system/file/{id}
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("REST request to delete file (physical and metadata) with id: {}", id);
        
        fileService.delete(id);
        
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if physical file exists
     * GET /system/file/{id}/exists
     */
    @GetMapping("/{id}/physical/exists")
    public ResponseEntity<Map<String, Boolean>> checkPhysicalFileExists(@PathVariable Long id) {
        log.info("REST request to check if physical file exists with id: {}", id);
        
        boolean exists = fileService.physicalFileExists(id);
        
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    /**
     * Get physical file size
     * GET /system/file/{id}/physical-size
     */
    @GetMapping("/{id}/physical-size")
    public ResponseEntity<Map<String, Object>> getPhysicalFileSize(@PathVariable Long id) {
        log.info("REST request to get physical file size with id: {}", id);
        
        Long size = fileService.getPhysicalFileSize(id);
        
        return ResponseEntity.ok(Map.of(
            "size", size != null ? size : 0,
            "formatted", formatSize(size)
        ));
    }

    // ========== CONTENT TYPE OPERATIONS ==========

    /**
     * Get content type for file
     * GET /system/file/{id}/content-type
     */
    @GetMapping("/{id}/content-type")
    public ResponseEntity<Map<String, String>> getContentType(@PathVariable Long id) {
        log.info("REST request to get content type for file with id: {}", id);
        
        String contentType = fileService.getContentTypeById(id);
        FileDTO file = fileService.getById(id);
        
        return ResponseEntity.ok(Map.of(
            "extension", file.getExtension(),
            "contentType", contentType
        ));
    }

    /**
     * Get all supported content types
     * GET /system/file/content-types
     */
    @GetMapping("/content-types")
    public ResponseEntity<Map<String, String>> getSupportedContentTypes() {
        log.info("REST request to get all supported content types");
        
        return ResponseEntity.ok(fileService.getSupportedContentTypes());
    }

    /**
     * Check if extension is supported
     * GET /system/file/content-types/check/{extension}
     */
    @GetMapping("/content-types/check/{extension}")
    public ResponseEntity<Map<String, Object>> checkContentTypeSupport(@PathVariable String extension) {
        log.info("REST request to check if extension is supported: {}", extension);
        
        boolean supported = fileService.isContentTypeSupported(extension);
        String contentType = supported ? fileService.getContentType(extension) : null;
        
        return ResponseEntity.ok(Map.of(
            "extension", extension,
            "supported", supported,
            "contentType", contentType != null ? contentType : "N/A"
        ));
    }

    // ========== MAINTENANCE OPERATIONS ==========

    /**
     * Cleanup orphaned files
     * POST /system/file/cleanup/orphans
     */
    @PostMapping("/cleanup/orphans")
    public ResponseEntity<Map<String, Object>> cleanupOrphanedFiles() {
        log.info("REST request to cleanup orphaned files");
        
        FileService.OrphanCleanupResult result = fileService.cleanupOrphanedFiles();
        
        return ResponseEntity.ok(Map.of(
            "deletedCount", result.deletedCount(),
            "deletedFiles", result.deletedFiles(),
            "errors", result.errors(),
            "hasErrors", result.hasErrors()
        ));
    }

    /**
     * Find files with missing physical files
     * GET /system/file/missing-physical
     */
    @GetMapping("/missing-physical")
    public ResponseEntity<Map<String, Object>> findFilesWithMissingPhysical() {
        log.info("REST request to find files with missing physical files");
        
        List<FileDTO> files = fileService.findFilesWithMissingPhysical();
        
        return ResponseEntity.ok(Map.of(
            "count", files.size(),
            "files", files
        ));
    }

    // ========== METADATA QUERY ENDPOINTS ==========

    /**
     * Find file by path
     * GET /system/file/by-path?path=...
     */
    @GetMapping("/by-path")
    public ResponseEntity<FileDTO> getByPath(@RequestParam String path) {
        log.info("REST request to get File by path: {}", path);
        return ResponseEntity.ok(fileService.findByPath(path));
    }

    /**
     * Find files by extension
     * GET /system/file/by-extension/{extension}
     */
    @GetMapping("/by-extension/{extension}")
    public ResponseEntity<List<FileDTO>> getByExtension(@PathVariable String extension) {
        log.info("REST request to get Files by extension: {}", extension);
        return ResponseEntity.ok(fileService.findByExtension(extension));
    }

    /**
     * Find files by file type
     * GET /system/file/by-type/{fileType}
     */
    @GetMapping("/by-type/{fileType}")
    public ResponseEntity<List<FileDTO>> getByFileType(@PathVariable String fileType) {
        log.info("REST request to get Files by file type: {}", fileType);
        return ResponseEntity.ok(fileService.findByFileType(fileType));
    }

    /**
     * Get storage statistics
     * GET /system/file/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStorageStats() {
        log.info("REST request to get storage statistics");
        
        FileService.FileStorageStats stats = fileService.getStorageStats();
        
        return ResponseEntity.ok(Map.of(
            "totalFiles", stats.totalFiles(),
            "totalSize", stats.totalSize() != null ? stats.totalSize() : 0,
            "totalSizeFormatted", stats.getFormattedTotalSize(),
            "maxFileSize", stats.maxFileSize(),
            "maxFileSizeFormatted", stats.getFormattedMaxFileSize(),
            "allowedExtensions", stats.allowedExtensions()
        ));
    }

    // ========== HELPER METHODS ==========

    private String formatSize(Long size) {
        if (size == null || size == 0) return "0 B";
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.1f MB", size / (1024.0 * 1024));
        return String.format("%.1f GB", size / (1024.0 * 1024 * 1024));
    }
}
