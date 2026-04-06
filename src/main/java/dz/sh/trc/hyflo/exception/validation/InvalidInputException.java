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

import dz.sh.trc.hyflo.exception.base.ErrorCode;

public class InvalidInputException extends ValidationException {

    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(ErrorCode.VAL_INVALID_INPUT, message);
    }

    public InvalidInputException(String field, Object value, String reason) {
        super(ErrorCode.VAL_INVALID_INPUT,
              String.format("Invalid value '%s' for field '%s': %s", value, field, reason));
    }
}