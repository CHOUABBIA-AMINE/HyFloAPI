/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RaasException
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Exception
 *	@Package	: Exception
 *
 **/

package dz.sh.trc.hyflo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base class for all custom exceptions in the RAAS system
 * Provides consistent error handling with HTTP status codes and error codes
 */
@Getter
public abstract class IaasException extends RuntimeException {

    private static final long serialVersionUID = -8910571031591546957L;
    
	private final String errorCode;
    private final HttpStatus httpStatus;
    private final Object[] args;

    protected IaasException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.args = new Object[0];
    }

    protected IaasException(String message, String errorCode, HttpStatus httpStatus, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.args = args;
    }

    protected IaasException(String message, Throwable cause, String errorCode, HttpStatus httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.args = new Object[0];
    }
}