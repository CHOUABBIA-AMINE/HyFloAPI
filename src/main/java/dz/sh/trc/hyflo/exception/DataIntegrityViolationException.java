/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: DataIntegrityViolationException
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
 * Exception thrown when data integrity constraints are violated
 * Examples: unique constraint, foreign key constraint, not null constraint
 * 
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
