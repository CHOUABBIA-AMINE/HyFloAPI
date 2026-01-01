/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RateLimitExceededException
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
 * Exception thrown when API rate limit is exceeded
 * 
 * @author CHOUABBIA Amine
 * @created 2025-12-20
 */
public class RateLimitExceededException extends IaasException {

	private static final long serialVersionUID = -4176756287457068691L;
	private final int retryAfterSeconds;
    
    public RateLimitExceededException(int retryAfterSeconds) {
        super("RATE_LIMIT_EXCEEDED",
              String.format("Rate limit exceeded. Please retry after %d seconds.", retryAfterSeconds),
              HttpStatus.TOO_MANY_REQUESTS);
        this.retryAfterSeconds = retryAfterSeconds;
    }
    
    public RateLimitExceededException(String message, int retryAfterSeconds) {
        super("RATE_LIMIT_EXCEEDED", message, HttpStatus.TOO_MANY_REQUESTS);
        this.retryAfterSeconds = retryAfterSeconds;
    }
    
    public int getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
