/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RequestTimeoutException
 *	@CreatedOn	: 12-20-2025
 *
 *	@Type		: Exception
 *	@Layer		: Exception
 *	@Package	: Exception
 *
 **/

package dz.mdn.iaas.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when request processing exceeds timeout
 * 
 * @author CHOUABBIA Amine
 * @created 2025-12-20
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
