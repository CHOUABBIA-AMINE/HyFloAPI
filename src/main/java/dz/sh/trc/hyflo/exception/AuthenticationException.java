/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuthenticationException
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Exception
 *	@Package	: Exception
 *
 **/

package dz.sh.trc.hyflo.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication fails
 * 
 */
public class AuthenticationException extends IaasException {
    
	private static final long serialVersionUID = 1170503594699323948L;

	public AuthenticationException(String message) {
        super("AUTHENTICATION_FAILED", message, HttpStatus.UNAUTHORIZED);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super("AUTHENTICATION_FAILED", message, HttpStatus.UNAUTHORIZED, cause);
    }
    
    public static AuthenticationException invalidCredentials() {
        return new AuthenticationException("Invalid username or password");
    }
    
    public static AuthenticationException tokenExpired() {
        return new AuthenticationException("Authentication token has expired");
    }
    
    public static AuthenticationException tokenInvalid() {
        return new AuthenticationException("Invalid authentication token");
    }
}
