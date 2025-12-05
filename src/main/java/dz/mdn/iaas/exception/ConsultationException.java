/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ConsultationException
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Exception
 *	@Package	: Exception
 *
 **/

package dz.mdn.iaas.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown for consultation-specific errors
 */
public class ConsultationException extends IaasException {

    private static final long serialVersionUID = 5512195457454064972L;

	public ConsultationException(String message) {
        super(message, "CONSULTATION_ERROR", HttpStatus.BAD_REQUEST);
    }

    public ConsultationException(String message, HttpStatus status) {
        super(message, "CONSULTATION_ERROR", status);
    }
}