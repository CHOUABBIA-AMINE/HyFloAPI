/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DataIntegrityViolationException
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
 * Exception thrown when data integrity constraints are violated
 * Examples: unique constraint, foreign key constraint, not null constraint
 * 
 * @author CHOUABBIA Amine
 * @created 2025-12-20
 */
public class DataIntegrityViolationException extends IaasException {

	private static final long serialVersionUID = -9118244388301995538L;

	public DataIntegrityViolationException(String message) {
        super("DATA_INTEGRITY_VIOLATION", message, HttpStatus.CONFLICT);
    }
    
    public DataIntegrityViolationException(String constraintName, String details) {
        super("DATA_INTEGRITY_VIOLATION",
              String.format("Data integrity violation on '%s': %s", constraintName, details),
              HttpStatus.CONFLICT);
    }
    
    public DataIntegrityViolationException(String message, Throwable cause) {
        super("DATA_INTEGRITY_VIOLATION", message, HttpStatus.CONFLICT, cause);
    }
}
