/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: RequestTimeoutException
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
 * Exception thrown when request processing exceeds timeout
 * 
 */
public class RequestTimeoutException extends IaasException {
    
	private static final long serialVersionUID = 1719756819850217324L;

	public RequestTimeoutException(String operation) {
        super("REQUEST_TIMEOUT",
              String.format("Operation '%s' timed out. Please try again.", operation),
              HttpStatus.REQUEST_TIMEOUT);
    }
    
    public RequestTimeoutException(String operation, long timeoutMillis) {
        super("REQUEST_TIMEOUT",
              String.format("Operation '%s' timed out after %d ms", operation, timeoutMillis),
              HttpStatus.REQUEST_TIMEOUT);
    }
}
