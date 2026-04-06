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

package dz.sh.trc.hyflo.exception.security;

import dz.sh.trc.hyflo.exception.base.ApiException;
import dz.sh.trc.hyflo.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends ApiException {

    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(ErrorCode.AUTH_INVALID_CREDENTIALS, message, HttpStatus.UNAUTHORIZED);
    }

    public AuthenticationException(ErrorCode code, String message) {
        super(code, message, HttpStatus.UNAUTHORIZED);
    }

    public AuthenticationException(ErrorCode code, String message, Throwable cause) {
        super(code, message, HttpStatus.UNAUTHORIZED, cause);
    }
}