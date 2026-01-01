/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: InvalidInputException
 *	@CreatedOn	: 12-20-2025
 *
 *	@Type		: Exception
 *	@Layer		: Exception
 *	@Package	: Exception
 *
 **/

package dz.mdn.iaas.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when input data is invalid
 * Different from validation - used for semantic/logical errors
 * 
 * @author CHOUABBIA Amine
 * @created 2025-12-20
 */
public class InvalidInputException extends IaasException {

	private static final long serialVersionUID = 133399249184414493L;

	public InvalidInputException(String message) {
        super("INVALID_INPUT", message, HttpStatus.BAD_REQUEST);
    }
    
    public InvalidInputException(String fieldName, String issue) {
        super("INVALID_INPUT",
              String.format("Invalid value for field '%s': %s", fieldName, issue),
              HttpStatus.BAD_REQUEST);
    }
    
    public InvalidInputException(String message, Throwable cause) {
        super("INVALID_INPUT", message, HttpStatus.BAD_REQUEST, cause);
    }
}
