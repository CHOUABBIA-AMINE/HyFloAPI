/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DatabaseOperationException
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
 * Exception thrown when database operations fail
 * Examples: connection failures, query timeouts, transaction failures
 * 
 * @author CHOUABBIA Amine
 * @created 2025-12-20
 */
public class DatabaseOperationException extends IaasException {
    
	private static final long serialVersionUID = -3761122917852546359L;

	public DatabaseOperationException(String message) {
        super("DATABASE_ERROR", message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    public DatabaseOperationException(String message, Throwable cause) {
        super("DATABASE_ERROR", message, HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }
    
    public DatabaseOperationException(String operation, String details) {
        super("DATABASE_ERROR", 
              String.format("Database operation '%s' failed: %s", operation, details),
              HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
