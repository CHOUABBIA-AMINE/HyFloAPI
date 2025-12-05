/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FileController
 *	@CreatedOn	: 10-14-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: System / Utility
 *
 **/

package dz.mdn.iaas.system.utility.controller;

import dz.mdn.iaas.system.utility.service.FileService;
import dz.mdn.iaas.system.utility.dto.FileDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    // ========== POST ONE FILE (CONTENT AND METADATA) ==========

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDTO> uploadFile(
            @RequestParam MultipartFile file,
            @RequestParam(required = false) String fileType) {
        
        log.info("Uploading file: {}", file.getOriginalFilename());
        
        FileDTO createdFile = fileService.createFile(file, fileType);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFile);
    }

    // ========== GET CONTENT ==========

    @GetMapping("/{id}/content")
    public ResponseEntity<Resource> downloadFileContent(@PathVariable Long id) {
        log.debug("Downloading content for file ID: {}", id);
        
        // Get metadata first to determine content type and filename
        FileDTO fileMetadata = fileService.getFileMetadata(id);
        
        // Get file content
        Resource resource = fileService.getFileContent(id);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileMetadata.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + fileMetadata.getFileName() + "\"")
                .contentLength(fileMetadata.getSize())
                .body(resource);
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<Resource> streamFileContent(@PathVariable Long id) {
        log.debug("Streaming content for file ID: {}", id);
        
        FileDTO fileMetadata = fileService.getFileMetadata(id);
        Resource resource = fileService.getFileContent(id);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileMetadata.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }

    // ========== GET METADATA ==========

    @GetMapping("/{id}")
    public ResponseEntity<FileDTO> getFileMetadata(@PathVariable Long id) {
        log.debug("Getting metadata for file ID: {}", id);
        
        FileDTO fileMetadata = fileService.getFileMetadata(id);
        
        return ResponseEntity.ok(fileMetadata);
    }

    // ========== DELETE FILE (CONTENT AND METADATA) ==========

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        log.info("Deleting file with ID: {}", id);
        
        fileService.deleteFile(id);
        
        return ResponseEntity.noContent().build();
    }

    // ========== GET ALL FILES METADATA ==========

    @GetMapping
    public ResponseEntity<Page<FileDTO>> getAllFilesMetadata(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.debug("Getting all files metadata - page: {}, size: {}", page, size);
        
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.ASC : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<FileDTO> filesMetadata = fileService.getAllFilesMetadata(pageable);
        
        return ResponseEntity.ok(filesMetadata);
    }

    // ========== ADDITIONAL UTILITY ENDPOINTS ==========

    @PutMapping("/{id}")
    public ResponseEntity<FileDTO> updateFileMetadata(
            @PathVariable Long id,
            @Valid @RequestBody FileDTO fileDTO) {
        
        log.info("Updating metadata for file ID: {}", id);
        
        FileDTO updatedFile = fileService.updateFile(id, fileDTO);
        
        return ResponseEntity.ok(updatedFile);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkFileExists(@PathVariable Long id) {
        log.debug("Checking existence of file ID: {}", id);
        
        boolean exists = fileService.findOne(id).isPresent();
        
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<FileDTO> getFileInfo(@PathVariable Long id) {
        log.debug("Getting complete info for file ID: {}", id);
        
        return fileService.findOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
