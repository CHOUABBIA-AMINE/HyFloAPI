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

/**
 * Replaces all scattered JWT token errors.
 * Use the static factories for the most common cases.
 */
public class TokenException extends ApiException {

    private static final long serialVersionUID = 1L;

    public TokenException(ErrorCode code, String message) {
        super(code, message, HttpStatus.UNAUTHORIZED);
    }

    public TokenException(ErrorCode code, String message, Throwable cause) {
        super(code, message, HttpStatus.UNAUTHORIZED, cause);
    }

    public static TokenException invalid(String detail) {
        return new TokenException(ErrorCode.AUTH_TOKEN_INVALID,
                "JWT token is invalid: " + detail);
    }

    public static TokenException expired() {
        return new TokenException(ErrorCode.AUTH_TOKEN_EXPIRED,
                ErrorCode.AUTH_TOKEN_EXPIRED.getDefaultMessage());
    }

    public static TokenException missing() {
        return new TokenException(ErrorCode.AUTH_TOKEN_MISSING,
                ErrorCode.AUTH_TOKEN_MISSING.getDefaultMessage());
    }
}