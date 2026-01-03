/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: OptimisticLockException
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
 * Exception thrown when optimistic locking fails
 * Indicates concurrent modification conflict
 * 
 */
public class OptimisticLockException extends IaasException {

	private static final long serialVersionUID = 7494356977546879685L;

	public OptimisticLockException(String entityName, Object entityId) {
        super("OPTIMISTIC_LOCK_FAILURE",
              String.format("The %s with ID %s was modified by another user. Please refresh and try again.",
                          entityName, entityId),
              HttpStatus.CONFLICT);
    }
    
    public OptimisticLockException(String message) {
        super("OPTIMISTIC_LOCK_FAILURE", message, HttpStatus.CONFLICT);
    }
}
