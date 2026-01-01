/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RaasException
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Exception
 *	@Package	: Exception
 *
 **/

package dz.sh.trc.hyflo.exception;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Exception thrown when file operations fail.
 * Provides detailed information about file operation failures including
 * operation type, file details, and contextual information.
 */
@Getter
public class FileOperationException extends RuntimeException {

	private static final long serialVersionUID = -197986999591243411L;
	
	private final String operation;
    private final String filePath;
    private final Long fileId;
    private final String fileName;
    private final LocalDateTime timestamp;
    private final String errorCode;
    private final String details;

    /**
     * Constructor with message only
     */
    public FileOperationException(String message) {
        super(message);
        this.operation = null;
        this.filePath = null;
        this.fileId = null;
        this.fileName = null;
        this.timestamp = LocalDateTime.now();
        this.errorCode = "FILE_OPERATION_ERROR";
        this.details = null;
    }

    /**
     * Constructor with message and cause
     */
    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
        this.operation = null;
        this.filePath = null;
        this.fileId = null;
        this.fileName = null;
        this.timestamp = LocalDateTime.now();
        this.errorCode = "FILE_OPERATION_ERROR";
        this.details = null;
    }

    /**
     * Constructor with detailed operation information
     */
    public FileOperationException(String message, String operation, String filePath) {
        super(message);
        this.operation = operation;
        this.filePath = filePath;
        this.fileId = null;
        this.fileName = extractFileNameFromPath(filePath);
        this.timestamp = LocalDateTime.now();
        this.errorCode = generateErrorCode(operation);
        this.details = null;
    }

    /**
     * Constructor with detailed operation information and cause
     */
    public FileOperationException(String message, String operation, String filePath, Throwable cause) {
        super(message, cause);
        this.operation = operation;
        this.filePath = filePath;
        this.fileId = null;
        this.fileName = extractFileNameFromPath(filePath);
        this.timestamp = LocalDateTime.now();
        this.errorCode = generateErrorCode(operation);
        this.details = null;
    }

    /**
     * Constructor with file ID information
     */
    public FileOperationException(String message, String operation, Long fileId, String filePath) {
        super(message);
        this.operation = operation;
        this.filePath = filePath;
        this.fileId = fileId;
        this.fileName = extractFileNameFromPath(filePath);
        this.timestamp = LocalDateTime.now();
        this.errorCode = generateErrorCode(operation);
        this.details = null;
    }

    /**
     * Constructor with complete information
     */
    public FileOperationException(String message, String operation, Long fileId, String filePath, 
                                String details, Throwable cause) {
        super(message, cause);
        this.operation = operation;
        this.filePath = filePath;
        this.fileId = fileId;
        this.fileName = extractFileNameFromPath(filePath);
        this.timestamp = LocalDateTime.now();
        this.errorCode = generateErrorCode(operation);
        this.details = details;
    }

    /**
     * Static factory methods for common file operations
     */
    public static FileOperationException uploadFailed(String fileName, Throwable cause) {
        return new FileOperationException(
            "Failed to upload file: " + fileName, 
            "UPLOAD", 
            fileName, 
            cause
        );
    }

    public static FileOperationException downloadFailed(Long fileId, String filePath, Throwable cause) {
        return new FileOperationException(
            "Failed to download file with ID: " + fileId, 
            "DOWNLOAD", 
            fileId, 
            filePath, 
            null, 
            cause
        );
    }

    public static FileOperationException deleteFailed(Long fileId, String filePath, Throwable cause) {
        return new FileOperationException(
            "Failed to delete file with ID: " + fileId, 
            "DELETE", 
            fileId, 
            filePath, 
            null, 
            cause
        );
    }

    public static FileOperationException readFailed(Long fileId, String filePath, Throwable cause) {
        return new FileOperationException(
            "Failed to read file with ID: " + fileId, 
            "READ", 
            fileId, 
            filePath, 
            null, 
            cause
        );
    }

    public static FileOperationException writeFailed(Long fileId, String filePath, Throwable cause) {
        return new FileOperationException(
            "Failed to write to file with ID: " + fileId, 
            "WRITE", 
            fileId, 
            filePath, 
            null, 
            cause
        );
    }

    public static FileOperationException copyFailed(String sourcePath, String targetPath, Throwable cause) {
        return new FileOperationException(
            "Failed to copy file from " + sourcePath + " to " + targetPath, 
            "COPY", 
            sourcePath, 
            cause
        );
    }

    public static FileOperationException moveFailed(String sourcePath, String targetPath, Throwable cause) {
        return new FileOperationException(
            "Failed to move file from " + sourcePath + " to " + targetPath, 
            "MOVE", 
            sourcePath, 
            cause
        );
    }

    public static FileOperationException validationFailed(String fileName, String reason) {
        return new FileOperationException(
            "File validation failed for " + fileName + ": " + reason, 
            "VALIDATION", 
            fileName
        );
    }

    public static FileOperationException accessDenied(String filePath) {
        return new FileOperationException(
            "Access denied to file: " + filePath, 
            "ACCESS_DENIED", 
            filePath
        );
    }

    public static FileOperationException fileNotFound(String filePath) {
        return new FileOperationException(
            "File not found: " + filePath, 
            "FILE_NOT_FOUND", 
            filePath
        );
    }

    public static FileOperationException diskSpaceFull(String filePath) {
        return new FileOperationException(
            "Insufficient disk space for file operation: " + filePath, 
            "DISK_SPACE_FULL", 
            filePath
        );
    }

    public static FileOperationException corruptedFile(Long fileId, String filePath) {
        return new FileOperationException(
            "File is corrupted or unreadable with ID: " + fileId, 
            "CORRUPTED", 
            fileId, 
            filePath, 
            "File integrity check failed", 
            null
        );
    }

    public static FileOperationException sizeLimitExceeded(String fileName, long actualSize, long maxSize) {
        return new FileOperationException(
            "File size limit exceeded for " + fileName + ". Size: " + actualSize + ", Limit: " + maxSize, 
            "SIZE_LIMIT_EXCEEDED", 
            fileName
        );
    }

    public static FileOperationException unsupportedFormat(String fileName, String extension) {
        return new FileOperationException(
            "Unsupported file format: " + extension + " for file: " + fileName, 
            "UNSUPPORTED_FORMAT", 
            fileName
        );
    }

    public static FileOperationException concurrentModification(Long fileId, String filePath) {
        return new FileOperationException(
            "Concurrent modification detected for file with ID: " + fileId, 
            "CONCURRENT_MODIFICATION", 
            fileId, 
            filePath, 
            "Another process is modifying this file", 
            null
        );
    }

    /**
     * Helper method to extract file name from path
     */
    private String extractFileNameFromPath(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        
        // Handle both Unix and Windows path separators
        int lastSlash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        
        if (lastSlash >= 0 && lastSlash < path.length() - 1) {
            return path.substring(lastSlash + 1);
        }
        
        return path; // Return the whole path if no separator found
    }

    /**
     * Generate error code based on operation type
     */
    private String generateErrorCode(String operation) {
        if (operation == null) {
            return "FILE_OPERATION_ERROR";
        }
        
        return "FILE_" + operation.toUpperCase() + "_ERROR";
    }

    /**
     * Get formatted error message with all details
     */
    public String getDetailedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("File operation failed");
        
        if (operation != null) {
            sb.append(" - Operation: ").append(operation);
        }
        
        if (fileId != null) {
            sb.append(" - File ID: ").append(fileId);
        }
        
        if (fileName != null) {
            sb.append(" - File: ").append(fileName);
        }
        
        if (filePath != null && !filePath.equals(fileName)) {
            sb.append(" - Path: ").append(filePath);
        }
        
        sb.append(" - Time: ").append(timestamp);
        
        if (details != null) {
            sb.append(" - Details: ").append(details);
        }
        
        sb.append(" - Error: ").append(getMessage());
        
        if (getCause() != null) {
            sb.append(" - Cause: ").append(getCause().getMessage());
        }
        
        return sb.toString();
    }

    /**
     * Check if this is a recoverable error
     */
    public boolean isRecoverable() {
        return switch (errorCode) {
            case "FILE_DISK_SPACE_FULL_ERROR", 
                 "FILE_CONCURRENT_MODIFICATION_ERROR", 
                 "FILE_ACCESS_DENIED_ERROR" -> false;
            case "FILE_UPLOAD_ERROR", 
                 "FILE_DOWNLOAD_ERROR", 
                 "FILE_READ_ERROR", 
                 "FILE_WRITE_ERROR" -> true;
            default -> true;
        };
    }

    /**
     * Check if this error should be retried
     */
    public boolean shouldRetry() {
        return switch (errorCode) {
            case "FILE_CONCURRENT_MODIFICATION_ERROR", 
                 "FILE_READ_ERROR", 
                 "FILE_WRITE_ERROR" -> true;
            case "FILE_NOT_FOUND_ERROR", 
                 "FILE_CORRUPTED_ERROR", 
                 "FILE_SIZE_LIMIT_EXCEEDED_ERROR", 
                 "FILE_UNSUPPORTED_FORMAT_ERROR" -> false;
            default -> false;
        };
    }

    /**
     * Get suggested action for this error
     */
    public String getSuggestedAction() {
        return switch (errorCode) {
            case "FILE_NOT_FOUND_ERROR" -> 
                "Verify the file path exists and is accessible";
            case "FILE_ACCESS_DENIED_ERROR" -> 
                "Check file permissions and user access rights";
            case "FILE_DISK_SPACE_FULL_ERROR" -> 
                "Free up disk space or use a different storage location";
            case "FILE_SIZE_LIMIT_EXCEEDED_ERROR" -> 
                "Reduce file size or increase the size limit";
            case "FILE_UNSUPPORTED_FORMAT_ERROR" -> 
                "Convert the file to a supported format";
            case "FILE_CORRUPTED_ERROR" -> 
                "Obtain a new copy of the file from the source";
            case "FILE_CONCURRENT_MODIFICATION_ERROR" -> 
                "Wait and retry the operation";
            case "FILE_UPLOAD_ERROR" -> 
                "Check network connection and file integrity, then retry";
            case "FILE_DOWNLOAD_ERROR" -> 
                "Verify file exists and network connectivity, then retry";
            default -> 
                "Contact system administrator if the problem persists";
        };
    }

    /**
     * Convert to a user-friendly message
     */
    public String getUserFriendlyMessage() {
        return switch (errorCode) {
            case "FILE_NOT_FOUND_ERROR" -> 
                "The requested file could not be found.";
            case "FILE_ACCESS_DENIED_ERROR" -> 
                "You don't have permission to access this file.";
            case "FILE_DISK_SPACE_FULL_ERROR" -> 
                "Not enough storage space available.";
            case "FILE_SIZE_LIMIT_EXCEEDED_ERROR" -> 
                "The file is too large to be processed.";
            case "FILE_UNSUPPORTED_FORMAT_ERROR" -> 
                "This file format is not supported.";
            case "FILE_CORRUPTED_ERROR" -> 
                "The file appears to be corrupted or damaged.";
            case "FILE_UPLOAD_ERROR" -> 
                "Failed to upload the file. Please try again.";
            case "FILE_DOWNLOAD_ERROR" -> 
                "Failed to download the file. Please try again.";
            case "FILE_CONCURRENT_MODIFICATION_ERROR" -> 
                "The file is currently being modified by another process.";
            default -> 
                "An unexpected error occurred while processing the file.";
        };
    }

    @Override
    public String toString() {
        return String.format(
            "FileOperationException{operation='%s', fileId=%s, fileName='%s', filePath='%s', " +
            "errorCode='%s', timestamp=%s, message='%s'}",
            operation, fileId, fileName, filePath, errorCode, timestamp, getMessage()
        );
    }
}
