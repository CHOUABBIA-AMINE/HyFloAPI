/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: OptimisticLockException
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
 * Exception thrown when optimistic locking fails
 * Indicates concurrent modification conflict
 * 
 * @author CHOUABBIA Amine
 * @created 2025-12-20
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
