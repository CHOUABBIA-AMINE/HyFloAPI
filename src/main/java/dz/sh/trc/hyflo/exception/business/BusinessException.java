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



package dz.sh.trc.hyflo.exception.business;

import dz.sh.trc.hyflo.exception.base.ApiException;
import dz.sh.trc.hyflo.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;

public class BusinessException extends ApiException {

    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(ErrorCode.BUS_VALIDATION_FAILED, message, HttpStatus.BAD_REQUEST);
    }

    public BusinessException(ErrorCode code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST);
    }

    public BusinessException(ErrorCode code, String message, HttpStatus status) {
        super(code, message, status);
    }
}