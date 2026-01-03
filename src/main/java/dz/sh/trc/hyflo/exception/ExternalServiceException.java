/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ExternalServiceException
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
 * Exception thrown when external service calls fail
 * Examples: REST API calls, SOAP services, third-party integrations
 * 
 */
public class ExternalServiceException extends IaasException {
 
	private static final long serialVersionUID = 3993001737249328862L;
	private final String serviceName;
    
    public ExternalServiceException(String serviceName, String message) {
        super("EXTERNAL_SERVICE_ERROR",
              String.format("External service '%s' failed: %s", serviceName, message),
              HttpStatus.BAD_GATEWAY);
        this.serviceName = serviceName;
    }
    
    public ExternalServiceException(String serviceName, String message, Throwable cause) {
        super("EXTERNAL_SERVICE_ERROR",
              String.format("External service '%s' failed: %s", serviceName, message),
              HttpStatus.BAD_GATEWAY, cause);
        this.serviceName = serviceName;
    }
    
    public String getServiceName() {
        return serviceName;
    }
}
