/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ApiException
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Exception
 *	@Package	: Exception
 *
 **/

package dz.sh.trc.hyflo.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;
/**
 * Abstract root for every typed exception in HyFloAPI.
 *
 * Constructor argument order (canonical):
 *   errorCode  → machine-readable token   (e.g. "AUTH-001")
 *   message    → human-readable sentence
 *   httpStatus → HTTP status code to send to the client
 */
@Getter
public abstract class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ErrorCode  errorCode;
    private final HttpStatus httpStatus;
    private final Object[]   args;

    protected ApiException(ErrorCode errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode  = errorCode;
        this.httpStatus = httpStatus;
        this.args       = new Object[0];
    }

    protected ApiException(ErrorCode errorCode, String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode  = errorCode;
        this.httpStatus = httpStatus;
        this.args       = new Object[0];
    }

    protected ApiException(ErrorCode errorCode, String message, HttpStatus httpStatus, Object... args) {
        super(message);
        this.errorCode  = errorCode;
        this.httpStatus = httpStatus;
        this.args       = args;
    }

    /** Returns the string code for logging/serialisation (e.g. "AUTH-001"). */
    public String getCode() {
        return errorCode.getCode();
    }
}