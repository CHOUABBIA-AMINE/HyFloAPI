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
import org.springframework.http.HttpStatus;

public class DataIntegrityViolationException extends ApiException {

    private static final long serialVersionUID = 1L;

    public DataIntegrityViolationException(String message) {
        super(ErrorCode.INF_DATA_INTEGRITY_VIOLATION, message, HttpStatus.CONFLICT);
    }

    public DataIntegrityViolationException(String constraintName, String details) {
        super(ErrorCode.INF_DATA_INTEGRITY_VIOLATION,
              String.format("Data integrity violation on '%s': %s", constraintName, details),
              HttpStatus.CONFLICT);
    }

    public DataIntegrityViolationException(String message, Throwable cause) {
        super(ErrorCode.INF_DATA_INTEGRITY_VIOLATION, message, HttpStatus.CONFLICT, cause);
    }
}