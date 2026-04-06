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

package dz.sh.trc.hyflo.exception.business;

import dz.sh.trc.hyflo.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(ErrorCode.BUS_RESOURCE_NOT_FOUND, message, HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String resourceType, Object id) {
        super(ErrorCode.BUS_RESOURCE_NOT_FOUND,
              String.format("%s with id '%s' was not found", resourceType, id),
              HttpStatus.NOT_FOUND);
    }
}