/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: RateLimitExceededException
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
 * Exception thrown when API rate limit is exceeded
 * 
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
