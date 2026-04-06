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

public class DatabaseOperationException extends ApiException {

    private static final long serialVersionUID = 1L;

    public DatabaseOperationException(String message) {
        super(ErrorCode.INF_DATABASE_ERROR, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public DatabaseOperationException(String message, Throwable cause) {
        super(ErrorCode.INF_DATABASE_ERROR, message, HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

    public DatabaseOperationException(String operation, String details) {
        super(ErrorCode.INF_DATABASE_ERROR,
              String.format("Database operation '%s' failed: %s", operation, details),
              HttpStatus.INTERNAL_SERVER_ERROR);
    }
}