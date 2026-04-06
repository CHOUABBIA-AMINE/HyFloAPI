/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ApiException
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Exception
 *	@Package	: Exception
 *
 **/

package dz.sh.trc.hyflo.exception.infrastructure;

import dz.sh.trc.hyflo.exception.base.ApiException;
import dz.sh.trc.hyflo.exception.base.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FileOperationException extends ApiException {

    private static final long serialVersionUID = 1L;

    private final String operation;
    private final String filePath;
    private final Long   fileId;

    public FileOperationException(String message) {
        super(ErrorCode.FILE_OPERATION_FAILED, message, HttpStatus.INTERNAL_SERVER_ERROR);
        this.operation = null; this.filePath = null; this.fileId = null;
    }

    public FileOperationException(String operation, String filePath, String message) {
        super(ErrorCode.FILE_OPERATION_FAILED,
              String.format("File operation '%s' on '%s' failed: %s", operation, filePath, message),
              HttpStatus.INTERNAL_SERVER_ERROR);
        this.operation = operation; this.filePath = filePath; this.fileId = null;
    }

    public FileOperationException(String operation, Long fileId, String message, Throwable cause) {
        super(ErrorCode.FILE_OPERATION_FAILED,
              String.format("File operation '%s' on fileId=%d failed: %s", operation, fileId, message),
              HttpStatus.INTERNAL_SERVER_ERROR, cause);
        this.operation = operation; this.filePath = null; this.fileId = fileId;
    }

    public FileOperationException(ErrorCode code, String message, HttpStatus status) {
        super(code, message, status);
        this.operation = null; this.filePath = null; this.fileId = null;
    }
}