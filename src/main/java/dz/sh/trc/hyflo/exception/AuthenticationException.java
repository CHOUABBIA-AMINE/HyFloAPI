/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AuthenticationException
 *	@CreatedOn	: 12-20-2025
 *
 *	@Type		: Exception
 *	@Layer		: Exception
 *	@Package	: Exception
 *
 **/

package dz.sh.trc.hyflo.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication fails
 * 
 * @author CHOUABBIA Amine
 * @created 2025-12-20
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
