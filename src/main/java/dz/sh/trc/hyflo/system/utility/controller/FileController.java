/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : FileController
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-16-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : System / Utility
 *
 **/

package dz.sh.trc.hyflo.system.utility.controller;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.utility.dto.FileDTO;
import dz.sh.trc.hyflo.system.utility.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * REST Controller for file metadata management and storage operations.
 * Supports file upload, download, and metadata tracking.
 */
@Tag(
    name = "File Management",
    description = "API for file upload, download, metadata management, and secure file storage operations"
)
@SecurityRequirement(name = "bearer-auth")
@RestController
@RequestMapping("/system/utility/files")
@Slf4j
public class FileController extends GenericController<FileDTO, Long> {

    private final FileService fileService;

    public FileController(FileService fileService) {
        super(fileService, "File");
        this.fileService = fileService;
    }

    @Operation(
        summary = "Get file metadata by ID",
        description = "Retrieve file metadata including storage location, size, type, and extension. Does not return file content."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File metadata found", content = @Content(schema = @Schema(implementation = FileDTO.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "File not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('FILE:READ')")
    public ResponseEntity<FileDTO> getById(
        @Parameter(description = "File ID", required = true, example = "1") @PathVariable Long id
    ) {
        return super.getById(id);
    }

    @Operation(
        summary = "Get all files with pagination",
        description = "Retrieve paginated list of all file metadata records. Useful for file management dashboards."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Files retrieved", content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('FILE:READ')")
    public ResponseEntity<Page<FileDTO>> getAll(
        @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
        @Parameter(description = "Sort direction", example = "desc") @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Operation(
        summary = "Upload file",
        description = "Upload a new file to the system. Generates unique storage path, validates file type and size, and stores metadata. Maximum file size and allowed extensions are configured server-side."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "File uploaded successfully", content = @Content(schema = @Schema(implementation = FileDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid file or file too large", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "415", description = "Unsupported file type", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "File upload failed - storage error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('FILE:UPLOAD')")
    public ResponseEntity<FileDTO> uploadFile(
        @Parameter(description = "File to upload", required = true) @RequestParam("file") MultipartFile file,
        @Parameter(description = "File type category", example = "DOCUMENT") @RequestParam(required = false) String fileType
    ) {
        log.info("Uploading file: {}", file.getOriginalFilename());
        FileDTO uploadedFile = fileService.uploadFile(file, fileType);
        return created(uploadedFile);
    }

    @Operation(
        summary = "Download file",
        description = "Download file content by ID. Returns file as attachment. Validates user permissions and file accessibility."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File download started", content = @Content(mediaType = "application/octet-stream")),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "File not found or deleted from storage", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "File read error or storage unavailable", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/download/{id}")
    @PreAuthorize("hasAuthority('FILE:DOWNLOAD')")
    public ResponseEntity<Resource> downloadFile(
        @Parameter(description = "File ID to download", required = true, example = "1") @PathVariable Long id
    ) {
        log.info("Downloading file ID: {}", id);
        
        FileDTO fileDTO = fileService.getById(id);
        Resource resource = fileService.downloadFile(id);
        String contentType = fileService.getContentType(fileDTO.getExtension());
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + "." + fileDTO.getExtension() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }

    @Operation(
        summary = "Get file by path",
        description = "Retrieve file metadata by its storage path. Useful for verifying file existence and getting metadata."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File found", content = @Content(schema = @Schema(implementation = FileDTO.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "File not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/by-path")
    @PreAuthorize("hasAuthority('FILE:READ')")
    public ResponseEntity<FileDTO> getByPath(
        @Parameter(description = "File path", required = true, example = "/uploads/documents/2026/01/pipeline-report.pdf") @RequestParam String path
    ) {
        log.debug("Getting file by path: {}", path);
        FileDTO file = fileService.findByPath(path);
        return success(file);
    }

    @Operation(
        summary = "Get files by extension",
        description = "Retrieve all files with a specific extension. Useful for filtered file listings (e.g., all PDFs, all images)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Files retrieved", content = @Content(schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/extension/{extension}")
    @PreAuthorize("hasAuthority('FILE:READ')")
    public ResponseEntity<List<FileDTO>> getByExtension(
        @Parameter(description = "File extension", required = true, example = "pdf") @PathVariable String extension
    ) {
        log.debug("Getting files by extension: {}", extension);
        List<FileDTO> files = fileService.findByExtension(extension);
        return success(files);
    }

    @Operation(
        summary = "Get files by type",
        description = "Retrieve all files of a specific type/category. Useful for filtered file listings (e.g., all documents, all images)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Files retrieved", content = @Content(schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/type/{fileType}")
    @PreAuthorize("hasAuthority('FILE:READ')")
    public ResponseEntity<List<FileDTO>> getByFileType(
        @Parameter(description = "File type", required = true, example = "DOCUMENT") @PathVariable String fileType
    ) {
        log.debug("Getting files by type: {}", fileType);
        List<FileDTO> files = fileService.findByFileType(fileType);
        return success(files);
    }

    @Operation(
        summary = "Update file metadata",
        description = "Update file metadata (path, type, etc.). Does not modify file content."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File metadata updated", content = @Content(schema = @Schema(implementation = FileDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid file metadata", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "File not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('FILE:MANAGE')")
    public ResponseEntity<FileDTO> update(@PathVariable Long id, @Valid @RequestBody FileDTO dto) {
        return super.update(id, dto);
    }

    @Operation(
        summary = "Delete file",
        description = "Delete file and its metadata. Permanently removes file from storage. This operation cannot be undone."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "File deleted successfully", content = @Content),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "File not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "File deletion failed - storage error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('FILE:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting file ID: {}", id);
        return super.delete(id);
    }
}
