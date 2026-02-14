/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ResourceNotFoundException
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
 * Exception thrown when a requested resource is not found
 */
public class ResourceNotFoundException extends IaasException {

    private static final long serialVersionUID = -1662769121516924958L;

	public ResourceNotFoundException(String resourceType, Object resourceId) {
        super(
            String.format("%s with ID %s not found", resourceType, resourceId),
            "RESOURCE_NOT_FOUND",
            HttpStatus.NOT_FOUND,
            resourceType, resourceId
        );
    }
	
	public ResourceNotFoundException(String resourceType) {
        super(
            String.format("%s with ID %s not found", resourceType, resourceType),
            "RESOURCE_NOT_FOUND",
            HttpStatus.NOT_FOUND,
            resourceType
        );
    }
}