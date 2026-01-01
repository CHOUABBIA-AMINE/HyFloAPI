/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AuthorizationException
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
 * Exception thrown when user lacks required permissions
 * 
 * @author CHOUABBIA Amine
 * @created 2025-12-20
 */
public class AuthorizationException extends IaasException {
    
	private static final long serialVersionUID = 2290049096512086346L;

	public AuthorizationException(String message) {
        super("AUTHORIZATION_DENIED", message, HttpStatus.FORBIDDEN);
    }
    
    public AuthorizationException(String resource, String action) {
        super("AUTHORIZATION_DENIED",
              String.format("You do not have permission to %s on %s", action, resource),
              HttpStatus.FORBIDDEN);
    }
    
    public static AuthorizationException insufficientPermissions() {
        return new AuthorizationException("Insufficient permissions to perform this action");
    }
}
