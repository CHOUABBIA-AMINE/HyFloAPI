/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FileService
 *	@CreatedOn	: 06-26-2023
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: System / Utility
 *
 **/

package dz.sh.trc.hyflo.system.utility.service;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.system.utility.dto.FileDTO;
import dz.sh.trc.hyflo.system.utility.model.File;
import dz.sh.trc.hyflo.system.utility.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class FileService extends GenericService<File, FileDTO, Long> {

    private final FileRepository fileRepository;
    private final Path fileStorageLocation;
    private final long maxFileSize;
    private final Set<String> allowedExtensions;

    // Allowed MIME types mapping
    private static final Map<String, String> CONTENT_TYPE_MAP = Map.ofEntries(
        Map.entry("pdf", "application/pdf"),
        Map.entry("txt", "text/plain"),
        Map.entry("csv", "text/csv"),
        Map.entry("jpg", "image/jpeg"),
        Map.entry("jpeg", "image/jpeg"),
        Map.entry("png", "image/png"),
        Map.entry("gif", "image/gif"),
        Map.entry("bmp", "image/bmp"),
        Map.entry("json", "application/json"),
        Map.entry("xml", "application/xml"),
        Map.entry("zip", "application/zip"),
        Map.entry("rar", "application/x-rar-compressed"),
        Map.entry("doc", "application/msword"),
        Map.entry("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        Map.entry("xls", "application/vnd.ms-excel"),
        Map.entry("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        Map.entry("ppt", "application/vnd.ms-powerpoint"),
        Map.entry("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation")
    );

    public FileService(
            FileRepository fileRepository,
            @Value("${file.upload-dir:uploads}") String uploadDir,
            @Value("${file.max-size:10485760}") long maxFileSize,
            @Value("${file.allowed-extensions:pdf,jpg,jpeg,png,gif,doc,docx,xls,xlsx,txt,zip}") String allowedExtensions) {
        this.fileRepository = fileRepository;
        this.maxFileSize = maxFileSize;
        this.allowedExtensions = new HashSet<>(Arrays.asList(allowedExtensions.toLowerCase().split(",")));
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("File storage location initialized: {}", this.fileStorageLocation);
            log.info("Max file size: {} bytes ({} MB)", maxFileSize, maxFileSize / (1024.0 * 1024));
            log.info("Allowed extensions: {}", this.allowedExtensions);
        } catch (Exception ex) {
            log.error("Could not create upload directory!", ex);
            throw new RuntimeException("Could not create upload directory!", ex);
        }
    }

    // ========== GENERIC SERVICE ABSTRACT METHOD IMPLEMENTATIONS ==========

    @Override
    protected JpaRepository<File, Long> getRepository() {
        return fileRepository;
    }

    @Override
    protected String getEntityName() {
        return "File";
    }

    @Override
    protected FileDTO toDTO(File entity) {
        return FileDTO.fromEntity(entity);
    }

    @Override
    protected File toEntity(FileDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(File entity, FileDTO dto) {
        dto.updateEntity(entity);
    }

    // ========== OVERRIDDEN METHODS WITH VALIDATION ==========

    @Override
    @Transactional
    public FileDTO create(FileDTO dto) {
        log.info("Creating new {} with validation", getEntityName());
        validateCreate(dto);
        return super.create(dto);
    }

    @Override
    @Transactional
    public FileDTO update(Long id, FileDTO dto) {
        log.info("Updating {} with id: {} with validation", getEntityName(), id);
        validateUpdate(id, dto);
        return super.update(id, dto);
    }

    /**
     * Override delete to ensure both metadata and physical file are removed
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting file (metadata and physical) with id: {}", id);
        deleteFilePhysically(id);
    }

    /**
     * Override getById to validate physical file existence
     */
    @Override
    @Transactional(readOnly = true)
    public FileDTO getById(Long id) {
        log.debug("Getting file by id: {} with physical validation", id);
        
        FileDTO dto = super.getById(id);
        
        // Log warning if physical file doesn't exist
        if (!physicalFileExistsInternal(dto.getPath())) {
            log.warn("Metadata exists but physical file missing for id: {} at path: {}", id, dto.getPath());
        }
        
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FileDTO> getAll(Pageable pageable) {
        log.debug("Getting all files with pagination");
        return super.getAll(pageable);
    }

    // ========== VALIDATION METHODS ==========

    protected void validateCreate(FileDTO dto) {
        log.debug("Validating File creation: {}", dto);
        
        if (fileRepository.existsByPath(dto.getPath())) {
            throw new IllegalArgumentException("File with path '" + dto.getPath() + "' already exists");
        }

        if (dto.getExtension() == null || dto.getExtension().trim().isEmpty()) {
            throw new IllegalArgumentException("File extension is required");
        }

        if (dto.getPath() == null || dto.getPath().trim().isEmpty()) {
            throw new IllegalArgumentException("File path is required");
        }

        if (dto.getSize() != null && dto.getSize() < 0) {
            throw new IllegalArgumentException("File size cannot be negative");
        }
        
        // Validate extension against whitelist
        if (!allowedExtensions.contains(dto.getExtension().toLowerCase())) {
            throw new IllegalArgumentException("File extension '" + dto.getExtension() + "' is not allowed. Allowed: " + allowedExtensions);
        }
    }

    protected void validateUpdate(Long id, FileDTO dto) {
        log.debug("Validating File update for id: {}", id);
        
        if (!fileRepository.existsById(id)) {
            throw new ResourceNotFoundException("File not found with id: " + id);
        }

        if (dto.getPath() != null) {
            fileRepository.findByPath(dto.getPath()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalArgumentException("File with path '" + dto.getPath() + "' already exists");
                }
            });
        }

        if (dto.getSize() != null && dto.getSize() < 0) {
            throw new IllegalArgumentException("File size cannot be negative");
        }
        
        // Validate extension if provided
        if (dto.getExtension() != null && !allowedExtensions.contains(dto.getExtension().toLowerCase())) {
            throw new IllegalArgumentException("File extension '" + dto.getExtension() + "' is not allowed. Allowed: " + allowedExtensions);
        }
    }

    /**
     * Validate uploaded file
     */
    private void validateUploadedFile(MultipartFile file) {
        // Check if file is empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }
        
        // Check file size
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException(
                String.format("File size (%d bytes) exceeds maximum allowed size (%d bytes)", 
                    file.getSize(), maxFileSize)
            );
        }
        
        // Validate filename
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("File name is required");
        }
        
        // Check for path traversal attempts
        if (originalFilename.contains("..") || originalFilename.contains("/") || originalFilename.contains("\\")) {
            throw new IllegalArgumentException("Invalid file name: " + originalFilename);
        }
        
        // Validate extension
        String extension = getFileExtension(originalFilename);
        if (!allowedExtensions.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException(
                "File extension '" + extension + "' is not allowed. Allowed extensions: " + allowedExtensions
            );
        }
    }

    // ========== PHYSICAL FILE STORAGE OPERATIONS ==========

    /**
     * Upload physical file and create metadata with enhanced validation
     */
    @Transactional
    public FileDTO uploadFile(MultipartFile file, String fileType) {
        log.info("Uploading file: {}", file.getOriginalFilename());
        
        Path tempFile = null;
        try {
            // Validate file
            validateUploadedFile(file);

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String uniqueFilename = generateUniqueFilename(originalFilename);
            
            // Create subdirectory based on date
            String dateSubdir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path targetLocation = fileStorageLocation.resolve(dateSubdir).normalize();
            Files.createDirectories(targetLocation);
            
            // Store physical file to temp location first
            tempFile = targetLocation.resolve(uniqueFilename + ".tmp");
            Path finalPath = targetLocation.resolve(uniqueFilename);
            
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }
            
            log.debug("File temporarily stored at: {}", tempFile);
            
            // Create metadata
            FileDTO fileDTO = FileDTO.builder()
                    .extension(extension)
                    .size(file.getSize())
                    .path(dateSubdir + "/" + uniqueFilename)
                    .fileType(fileType)
                    .build();
            
            FileDTO createdDTO = create(fileDTO);
            
            // Move temp file to final location
            Files.move(tempFile, finalPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("File uploaded successfully - ID: {}, Path: {}", createdDTO.getId(), finalPath);
            
            return createdDTO;
            
        } catch (IOException ex) {
            log.error("Failed to store file: {}", file.getOriginalFilename(), ex);
            // Clean up temp file if exists
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    log.warn("Failed to delete temp file: {}", tempFile, e);
                }
            }
            throw new RuntimeException("Failed to store file: " + file.getOriginalFilename(), ex);
        } catch (Exception ex) {
            log.error("Unexpected error during file upload: {}", file.getOriginalFilename(), ex);
            // Clean up temp file if exists
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    log.warn("Failed to delete temp file: {}", tempFile, e);
                }
            }
            throw ex;
        }
    }

    /**
     * Batch upload multiple files
     */
    @Transactional
    public List<FileDTO> uploadFiles(List<MultipartFile> files, String fileType) {
        log.info("Batch uploading {} files", files.size());
        
        List<FileDTO> uploadedFiles = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            try {
                FileDTO uploadedFile = uploadFile(file, fileType);
                uploadedFiles.add(uploadedFile);
            } catch (Exception ex) {
                String error = String.format("File %d (%s): %s", i + 1, file.getOriginalFilename(), ex.getMessage());
                errors.add(error);
                log.error("Failed to upload file in batch: {}", error, ex);
            }
        }
        
        if (!errors.isEmpty()) {
            log.warn("Batch upload completed with {} errors out of {} files", errors.size(), files.size());
        }
        
        return uploadedFiles;
    }

    /**
     * Download physical file
     */
    @Transactional(readOnly = true)
    public Resource downloadFile(Long fileId) {
        log.info("Downloading file with id: {}", fileId);
        
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + fileId));
        
        try {
            Path filePath = fileStorageLocation.resolve(file.getPath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                log.error("Physical file not found or not readable: {}", file.getPath());
                throw new ResourceNotFoundException("File not found or not readable: " + file.getPath());
            }
        } catch (MalformedURLException ex) {
            log.error("Invalid file path: {}", file.getPath(), ex);
            throw new RuntimeException("Invalid file path: " + file.getPath(), ex);
        }
    }

    /**
     * Delete physical file and metadata
     */
    @Transactional
    public void deleteFilePhysically(Long fileId) {
        log.info("Deleting file physically with id: {}", fileId);
        
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + fileId));
        
        try {
            // Delete physical file
            Path filePath = fileStorageLocation.resolve(file.getPath()).normalize();
            boolean deleted = Files.deleteIfExists(filePath);
            
            if (deleted) {
                log.info("Physical file deleted: {}", filePath);
            } else {
                log.warn("Physical file not found (already deleted?): {}", filePath);
            }
            
            // Delete metadata
            fileRepository.delete(file);
            log.info("File metadata deleted for id: {}", fileId);
            
        } catch (IOException ex) {
            log.error("Failed to delete physical file: {}", file.getPath(), ex);
            throw new RuntimeException("Failed to delete physical file: " + file.getPath(), ex);
        }
    }

    /**
     * Replace physical file content keeping same metadata
     */
    @Transactional
    public FileDTO replaceFile(Long fileId, MultipartFile newFile) {
        log.info("Replacing file content for id: {}", fileId);
        
        File existingFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + fileId));
        
        Path oldPath = null;
        try {
            // Validate new file
            validateUploadedFile(newFile);
            
            // Store old path for rollback
            oldPath = fileStorageLocation.resolve(existingFile.getPath()).normalize();
            Path backupPath = oldPath.getParent().resolve(oldPath.getFileName() + ".backup");
            
            // Backup existing file
            if (Files.exists(oldPath)) {
                Files.move(oldPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
            }
            
            // Store new file
            try (InputStream inputStream = newFile.getInputStream()) {
                Files.copy(inputStream, oldPath, StandardCopyOption.REPLACE_EXISTING);
            }
            
            // Update metadata
            String newExtension = getFileExtension(newFile.getOriginalFilename());
            existingFile.setExtension(newExtension);
            existingFile.setSize(newFile.getSize());
            fileRepository.save(existingFile);
            
            // Delete backup
            Files.deleteIfExists(backupPath);
            
            log.info("File replaced successfully for id: {}", fileId);
            return FileDTO.fromEntity(existingFile);
            
        } catch (Exception ex) {
            log.error("Failed to replace file for id: {}", fileId, ex);
            throw new RuntimeException("Failed to replace file: " + ex.getMessage(), ex);
        }
    }

    /**
     * Copy file (duplicate with new metadata)
     */
    @Transactional
    public FileDTO copyFile(Long fileId, String newFileType) {
        log.info("Copying file with id: {}", fileId);
        
        File sourceFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + fileId));
        
        try {
            Path sourcePath = fileStorageLocation.resolve(sourceFile.getPath()).normalize();
            
            if (!Files.exists(sourcePath)) {
                throw new ResourceNotFoundException("Physical file not found: " + sourceFile.getPath());
            }
            
            // Generate new path
            String dateSubdir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path targetLocation = fileStorageLocation.resolve(dateSubdir).normalize();
            Files.createDirectories(targetLocation);
            
            String uniqueFilename = generateUniqueFilename("copy_" + sourceFile.getId() + "." + sourceFile.getExtension());
            Path targetPath = targetLocation.resolve(uniqueFilename);
            
            // Copy physical file
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            // Create new metadata
            FileDTO newFileDTO = FileDTO.builder()
                    .extension(sourceFile.getExtension())
                    .size(sourceFile.getSize())
                    .path(dateSubdir + "/" + uniqueFilename)
                    .fileType(newFileType != null ? newFileType : sourceFile.getFileType())
                    .build();
            
            FileDTO createdDTO = create(newFileDTO);
            log.info("File copied successfully - Source ID: {}, New ID: {}", fileId, createdDTO.getId());
            
            return createdDTO;
            
        } catch (IOException ex) {
            log.error("Failed to copy file with id: {}", fileId, ex);
            throw new RuntimeException("Failed to copy file: " + ex.getMessage(), ex);
        }
    }

    /**
     * Check if physical file exists
     */
    @Transactional(readOnly = true)
    public boolean physicalFileExists(Long fileId) {
        log.debug("Checking physical file existence for id: {}", fileId);
        
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + fileId));
        
        return physicalFileExistsInternal(file.getPath());
    }

    /**
     * Get physical file size
     */
    @Transactional(readOnly = true)
    public Long getPhysicalFileSize(Long fileId) {
        log.debug("Getting physical file size for id: {}", fileId);
        
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + fileId));
        
        try {
            Path filePath = fileStorageLocation.resolve(file.getPath()).normalize();
            return Files.size(filePath);
        } catch (IOException ex) {
            log.error("Failed to get file size: {}", file.getPath(), ex);
            return null;
        }
    }

    /**
     * Cleanup orphaned files (files with no metadata)
     */
    @Transactional
    public OrphanCleanupResult cleanupOrphanedFiles() {
        log.info("Starting cleanup of orphaned files");
        
        List<String> deletedFiles = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        try {
            // Get all file paths from database
            Set<String> dbPaths = fileRepository.findAll().stream()
                    .map(File::getPath)
                    .collect(Collectors.toSet());
            
            // Walk through storage directory
            Files.walk(fileStorageLocation)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            String relativePath = fileStorageLocation.relativize(path).toString().replace("\\", "/");
                            
                            // Skip backup files
                            if (relativePath.endsWith(".backup") || relativePath.endsWith(".tmp")) {
                                return;
                            }
                            
                            // If file not in database, it's orphaned
                            if (!dbPaths.contains(relativePath)) {
                                Files.delete(path);
                                deletedFiles.add(relativePath);
                                log.info("Deleted orphaned file: {}", relativePath);
                            }
                        } catch (IOException ex) {
                            String error = "Failed to delete: " + path;
                            errors.add(error);
                            log.error(error, ex);
                        }
                    });
            
        } catch (IOException ex) {
            log.error("Failed to walk file storage directory", ex);
            errors.add("Failed to walk directory: " + ex.getMessage());
        }
        
        OrphanCleanupResult result = new OrphanCleanupResult(deletedFiles.size(), deletedFiles, errors);
        log.info("Orphaned files cleanup completed: {} deleted, {} errors", deletedFiles.size(), errors.size());
        
        return result;
    }

    /**
     * Find files with missing physical files
     */
    @Transactional(readOnly = true)
    public List<FileDTO> findFilesWithMissingPhysical() {
        log.info("Finding files with missing physical files");
        
        return fileRepository.findAll().stream()
                .filter(file -> !physicalFileExistsInternal(file.getPath()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ========== CONTENT TYPE UTILITY METHODS ==========

    /**
     * Get content type for file extension
     * Uses CONTENT_TYPE_MAP for supported extensions
     * 
     * @param extension File extension (without dot)
     * @return MIME content type or "application/octet-stream" as default
     */
    public String getContentType(String extension) {
        if (extension == null || extension.isEmpty()) {
            return "application/octet-stream";
        }
        return CONTENT_TYPE_MAP.getOrDefault(extension.toLowerCase(), "application/octet-stream");
    }

    /**
     * Get content type for a file by ID
     * 
     * @param fileId File ID
     * @return MIME content type
     */
    @Transactional(readOnly = true)
    public String getContentTypeById(Long fileId) {
        FileDTO file = getById(fileId);
        return getContentType(file.getExtension());
    }

    /**
     * Get all supported content types
     * 
     * @return Map of extension to content type
     */
    public Map<String, String> getSupportedContentTypes() {
        return new HashMap<>(CONTENT_TYPE_MAP);
    }

    /**
     * Check if content type is supported
     * 
     * @param extension File extension
     * @return true if content type is known
     */
    public boolean isContentTypeSupported(String extension) {
        if (extension == null) return false;
        return CONTENT_TYPE_MAP.containsKey(extension.toLowerCase());
    }

    // ========== HELPER METHODS ==========

    private boolean physicalFileExistsInternal(String relativePath) {
        try {
            Path filePath = fileStorageLocation.resolve(relativePath).normalize();
            return Files.exists(filePath);
        } catch (Exception ex) {
            log.error("Error checking file existence: {}", relativePath, ex);
            return false;
        }
    }

    private String generateUniqueFilename(String originalFilename) {
        String name = originalFilename.contains(".")
                ? originalFilename.substring(0, originalFilename.lastIndexOf('.'))
                : originalFilename;
        String extension = getFileExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return sanitizeFilename(name) + "_" + uuid + "." + extension;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    private String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    // ========== CUSTOM BUSINESS METHODS ==========

    @Transactional(readOnly = true)
    public FileDTO findByPath(String path) {
        log.debug("Finding file by path: {}", path);
        return fileRepository.findByPath(path)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with path: " + path));
    }

    @Transactional(readOnly = true)
    public List<FileDTO> findByExtension(String extension) {
        log.debug("Finding files by extension: {}", extension);
        return fileRepository.findByExtension(extension)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FileDTO> findByFileType(String fileType) {
        log.debug("Finding files by file type: {}", fileType);
        return fileRepository.findByFileType(fileType)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long getTotalStorageSize() {
        log.debug("Calculating total storage size");
        Long total = fileRepository.getTotalStorageSize();
        return total != null ? total : 0L;
    }

    @Transactional(readOnly = true)
    public FileStorageStats getStorageStats() {
        log.debug("Getting storage statistics");
        
        long totalFiles = fileRepository.count();
        Long totalSize = getTotalStorageSize();
        long maxSize = maxFileSize;
        Set<String> allowed = new HashSet<>(allowedExtensions);
        
        return new FileStorageStats(totalFiles, totalSize, maxSize, allowed);
    }

    // ========== INNER CLASSES ==========

    /**
     * File storage statistics
     */
    public record FileStorageStats(
            long totalFiles,
            Long totalSize,
            long maxFileSize,
            Set<String> allowedExtensions
    ) {
        public String getFormattedTotalSize() {
            return formatSize(totalSize);
        }
        
        public String getFormattedMaxFileSize() {
            return formatSize(maxFileSize);
        }
        
        private String formatSize(Long size) {
            if (size == null || size == 0) return "0 B";
            if (size < 1024) return size + " B";
            if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
            if (size < 1024 * 1024 * 1024) return String.format("%.1f MB", size / (1024.0 * 1024));
            return String.format("%.1f GB", size / (1024.0 * 1024 * 1024));
        }
    }

    /**
     * Orphan cleanup result
     */
    public record OrphanCleanupResult(
            int deletedCount,
            List<String> deletedFiles,
            List<String> errors
    ) {
        public boolean hasErrors() {
            return !errors.isEmpty();
        }
    }
}
