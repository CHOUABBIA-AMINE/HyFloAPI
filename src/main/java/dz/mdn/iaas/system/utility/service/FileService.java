/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FileService
 *	@CreatedOn	: 10-14-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: System / Utility
 *
 **/

package dz.mdn.iaas.system.utility.service;

import dz.mdn.iaas.system.utility.model.File;
import dz.mdn.iaas.system.utility.repository.FileRepository;
import dz.mdn.iaas.system.utility.dto.FileDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FileService {

    private final FileRepository fileRepository;

    @Value("${app.file.upload-dir:f:/files}")
    private String uploadDirectory;

    // ========== CREATE OPERATIONS ==========

    public FileDTO createFile(MultipartFile multipartFile, String fileType) {
        log.info("Creating file: {}", multipartFile.getOriginalFilename());

        try {
            // Extract file properties
            String extension = getFileExtension(multipartFile.getOriginalFilename());
            String filePath = generateFilePath(extension);
            long size = multipartFile.getSize();

            // Save file to disk
            saveFileToDisk(multipartFile, filePath);

            // Create entity with exact field mapping
            File file = new File();
            file.setExtension(extension); // F_01
            file.setSize(size); // F_02
            file.setPath(filePath); // F_03
            file.setFileType(fileType != null ? fileType : detectFileType(extension)); // F_04

            File savedFile = fileRepository.save(file);
            log.info("Successfully created file with ID: {}", savedFile.getId());

            return FileDTO.fromEntity(savedFile);

        } catch (IOException e) {
            log.error("Error creating file: {}", multipartFile.getOriginalFilename(), e);
            throw new RuntimeException("Failed to create file", e);
        }
    }

    // ========== READ OPERATIONS ==========

    @Transactional(readOnly = true)
    public FileDTO getFileMetadata(Long id) {
        log.debug("Getting metadata for file ID: {}", id);

        File file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + id));

        FileDTO dto = FileDTO.fromEntity(file);
        dto.setExists(Files.exists(Paths.get(file.getPath())));

        return dto;
    }

    /**
     * Get file content as Resource
     */
    @Transactional(readOnly = true)
    public Resource getFileContent(Long id) {
        log.debug("Getting content for file ID: {}", id);

        File file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + id));

        return loadFileAsResource(file.getPath());
    }

    @Transactional(readOnly = true)
    public Page<FileDTO> getAllFilesMetadata(Pageable pageable) {
        log.debug("Getting all files metadata with pagination");

        Page<File> files = fileRepository.findAll(pageable);
        return files.map(file -> {
            FileDTO dto = FileDTO.fromEntity(file);
            dto.setExists(Files.exists(Paths.get(file.getPath())));
            return dto;
        });
    }

    @Transactional(readOnly = true)
    public Optional<FileDTO> findOne(Long id) {
        log.debug("Finding file by ID: {}", id);

        return fileRepository.findById(id)
                .map(file -> {
                    FileDTO dto = FileDTO.fromEntity(file);
                    dto.setExists(Files.exists(Paths.get(file.getPath())));
                    return dto;
                });
    }

    // ========== UPDATE OPERATIONS ==========

    public FileDTO updateFile(Long id, FileDTO fileDTO) {
        log.info("Updating file with ID: {}", id);

        File existingFile = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + id));

        // Update modifiable fields
        if (fileDTO.getExtension() != null) {
            existingFile.setExtension(fileDTO.getExtension()); // F_01
        }
        if (fileDTO.getFileType() != null) {
            existingFile.setFileType(fileDTO.getFileType()); // F_04
        }
        // Note: path (F_03) and size (F_02) are typically not updated directly

        File updatedFile = fileRepository.save(existingFile);
        log.info("Successfully updated file with ID: {}", id);

        return FileDTO.fromEntity(updatedFile);
    }

    // ========== DELETE OPERATIONS ==========

    public void deleteFile(Long id) {
        log.info("Deleting file with ID: {}", id);

        File file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + id));

        try {
            // Delete physical file
            deletePhysicalFile(file.getPath());

            // Delete database record
            fileRepository.delete(file);

            log.info("Successfully deleted file with ID: {}", id);

        } catch (IOException e) {
            log.error("Error deleting file with ID: {}", id, e);
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    // ========== HELPER METHODS ==========

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    private String generateFilePath(String extension) {
        String filename = UUID.randomUUID().toString() + "." + extension;
        return Paths.get(uploadDirectory, filename).toString();
    }

    private void saveFileToDisk(MultipartFile multipartFile, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private Resource loadFileAsResource(String filePath) {
        try {
            Path path = Paths.get(filePath).normalize();
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable: " + filePath);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed file path: " + filePath, e);
        }
    }

    private void deletePhysicalFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.delete(path);
            log.debug("Deleted physical file: {}", filePath);
        }
    }

    private String detectFileType(String extension) {
        if (extension == null) return "unknown";

        return switch (extension.toLowerCase()) {
            case "pdf" -> "document";
            case "jpg", "jpeg", "png", "gif" -> "image";
            case "txt", "doc", "docx" -> "document";
            case "mp4", "avi", "mov" -> "video";
            case "mp3", "wav" -> "audio";
            case "zip", "rar" -> "archive";
            default -> "file";
        };
    }
    
    @Transactional(readOnly = true)
    public UrlResource getFileUrlResource(Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + id));
        try {
            Path path = Paths.get(file.getPath()).normalize();
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid file URI: " + file.getPath(), e);
        }
    }
}
