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

package dz.sh.trc.hyflo.exception.validation;

import dz.sh.trc.hyflo.exception.base.ApiException;
import dz.sh.trc.hyflo.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class ValidationException extends ApiException {

    private static final long serialVersionUID = 1L;
    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(ErrorCode.VAL_CONSTRAINT_VIOLATION, message, HttpStatus.BAD_REQUEST);
        this.fieldErrors = Map.of();
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(ErrorCode.VAL_CONSTRAINT_VIOLATION, message, HttpStatus.BAD_REQUEST);
        this.fieldErrors = fieldErrors != null ? fieldErrors : Map.of();
    }

    public ValidationException(ErrorCode code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST);
        this.fieldErrors = Map.of();
    }

    public Map<String, String> getFieldErrors() { return fieldErrors; }
}