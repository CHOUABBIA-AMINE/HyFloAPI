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

package dz.sh.trc.hyflo.exception.infrastructure;

import dz.sh.trc.hyflo.exception.base.ApiException;
import dz.sh.trc.hyflo.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Kept for backward-compatibility only.
 * @deprecated Extend ApiException directly via a typed sub-class.
 */
@Deprecated(since = "phase-1-refactor", forRemoval = true)
public class IaasException extends ApiException {

    private static final long serialVersionUID = 1L;

    public IaasException(String message) {
        super(ErrorCode.SYS_INTERNAL_ERROR, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public IaasException(ErrorCode code, String message, HttpStatus status) {
        super(code, message, status);
    }

    public IaasException(ErrorCode code, String message, HttpStatus status, Throwable cause) {
        super(code, message, status, cause);
    }
}