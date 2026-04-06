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

public class OptimisticLockException extends ApiException {

    private static final long serialVersionUID = 1L;

    public OptimisticLockException(String entityName, Object entityId) {
        super(ErrorCode.INF_OPTIMISTIC_LOCK_FAILURE,
              String.format("The %s with id '%s' was modified by another session. Refresh and retry.",
                      entityName, entityId),
              HttpStatus.CONFLICT);
    }

    public OptimisticLockException(String message) {
        super(ErrorCode.INF_OPTIMISTIC_LOCK_FAILURE, message, HttpStatus.CONFLICT);
    }
}