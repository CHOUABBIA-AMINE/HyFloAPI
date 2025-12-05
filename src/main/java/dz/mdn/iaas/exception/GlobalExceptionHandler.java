/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: GlobalExceptionHandler
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Controller
 *	@Package	: Exception
 *
 **/

package dz.mdn.iaas.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global Exception Handler for the RAAS application
 * Provides centralized exception handling with consistent error responses
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Error Response DTO for consistent error formatting
     */
    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        private String errorCode;
        private String message;
        private String path;
        private LocalDateTime timestamp;
        private Map<String, Object> details;

        public ErrorResponse(String errorCode, String message, String path) {
            this.errorCode = errorCode;
            this.message = message;
            this.path = path;
            this.timestamp = LocalDateTime.now();
            this.details = new HashMap<>();
        }
    }

    /**
     * Handle custom RAAS exceptions
     */
    @ExceptionHandler(IaasException.class)
    public ResponseEntity<ErrorResponse> handleRaasException(IaasException ex, WebRequest request) {
        log.error("RAAS Exception occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );

        if (ex.getArgs() != null && ex.getArgs().length > 0) {
            errorResponse.getDetails().put("context", ex.getArgs());
        }

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handle validation errors from @Valid annotations
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        log.warn("Validation error occurred: {}", ex.getMessage());

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    FieldError::getDefaultMessage,
                    (existing, replacement) -> existing + "; " + replacement
                ));

        ErrorResponse errorResponse = new ErrorResponse(
            "VALIDATION_ERROR",
            "Validation failed for one or more fields",
            request.getDescription(false).replace("uri=", "")
        );
        errorResponse.getDetails().put("fieldErrors", fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle all other uncaught exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred. Please contact support if the problem persists.",
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}