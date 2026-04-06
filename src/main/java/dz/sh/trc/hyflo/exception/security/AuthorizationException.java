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

public class AuthorizationException extends ApiException {

    private static final long serialVersionUID = 1L;

    public AuthorizationException(String message) {
        super(ErrorCode.RBAC_ACCESS_DENIED, message, HttpStatus.FORBIDDEN);
    }

    public AuthorizationException(ErrorCode code, String message) {
        super(code, message, HttpStatus.FORBIDDEN);
    }
}