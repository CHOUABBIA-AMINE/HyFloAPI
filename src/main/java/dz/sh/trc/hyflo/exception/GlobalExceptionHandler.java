/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: GlobalExceptionHandler
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Exception
 *	@Package	: Exception
 *
 **/

package dz.sh.trc.hyflo.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Enhanced Global Exception Handler for the IAAS application
 * Provides comprehensive centralized exception handling with consistent error responses
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

    // ========================================
    // CUSTOM IAAS EXCEPTIONS
    // ========================================

    /**
     * Handle custom IAAS exceptions
     */
    @ExceptionHandler(IaasException.class)
    public ResponseEntity<ErrorResponse> handleIaasException(IaasException ex, WebRequest request) {
        log.error("IAAS Exception occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            extractPath(request)
        );

        if (ex.getArgs() != null && ex.getArgs().length > 0) {
            errorResponse.getDetails().put("context", ex.getArgs());
        }

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handle Resource Not Found exceptions
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        log.warn("Resource not found: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            "RESOURCE_NOT_FOUND",
            ex.getMessage(),
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle Business Validation exceptions
     */
    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessValidationException(
            BusinessValidationException ex, WebRequest request) {
        
        log.warn("Business validation failed: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            "BUSINESS_VALIDATION_ERROR",
            ex.getMessage(),
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // ========================================
    // VALIDATION EXCEPTIONS
    // ========================================

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
                    fieldError -> fieldError.getDefaultMessage() != null ? 
                        fieldError.getDefaultMessage() : "Invalid value",
                    (existing, replacement) -> existing + "; " + replacement
                ));

        ErrorResponse errorResponse = new ErrorResponse(
            "VALIDATION_ERROR",
            "Validation failed for one or more fields",
            extractPath(request)
        );
        errorResponse.getDetails().put("fieldErrors", fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle constraint violation exceptions
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {

        log.warn("Constraint violation: {}", ex.getMessage());

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        Map<String, String> errors = violations.stream()
                .collect(Collectors.toMap(
                    violation -> violation.getPropertyPath().toString(),
                    ConstraintViolation::getMessage,
                    (existing, replacement) -> existing + "; " + replacement
                ));

        ErrorResponse errorResponse = new ErrorResponse(
            "CONSTRAINT_VIOLATION",
            "Constraint violation occurred",
            extractPath(request)
        );
        errorResponse.getDetails().put("violations", errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle missing request parameters
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(
            MissingServletRequestParameterException ex, WebRequest request) {

        log.warn("Missing request parameter: {}", ex.getParameterName());

        ErrorResponse errorResponse = new ErrorResponse(
            "MISSING_PARAMETER",
            String.format("Required parameter '%s' is missing", ex.getParameterName()),
            extractPath(request)
        );
        errorResponse.getDetails().put("parameterName", ex.getParameterName());
        errorResponse.getDetails().put("parameterType", ex.getParameterType());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle method argument type mismatch
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        log.warn("Type mismatch for parameter: {}", ex.getName());

        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(),
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

        ErrorResponse errorResponse = new ErrorResponse(
            "TYPE_MISMATCH",
            message,
            extractPath(request)
        );
        errorResponse.getDetails().put("parameterName", ex.getName());
        errorResponse.getDetails().put("rejectedValue", ex.getValue());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // ========================================
    // DATABASE EXCEPTIONS
    // ========================================

    /**
     * Handle data integrity violations (unique constraints, foreign keys, etc.)
     */
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            org.springframework.dao.DataIntegrityViolationException ex, WebRequest request) {

        log.error("Data integrity violation: {}", ex.getMessage(), ex);

        String message = "Data integrity constraint violated";
        String rootMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        if (rootMessage != null) {
            if (rootMessage.contains("unique constraint") || rootMessage.contains("UNIQUE")) {
                message = "A record with this value already exists";
            } else if (rootMessage.contains("foreign key") || rootMessage.contains("FOREIGN KEY")) {
                message = "Cannot complete operation due to related records";
            } else if (rootMessage.contains("not null") || rootMessage.contains("NOT NULL")) {
                message = "Required field cannot be null";
            }
        }

        ErrorResponse errorResponse = new ErrorResponse(
            "DATA_INTEGRITY_VIOLATION",
            message,
            extractPath(request)
        );
        errorResponse.getDetails().put("technicalDetails", rootMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handle optimistic locking failures
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockingFailure(
            OptimisticLockingFailureException ex, WebRequest request) {

        log.warn("Optimistic locking failure: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            "OPTIMISTIC_LOCK_FAILURE",
            "The record was modified by another user. Please refresh and try again.",
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handle JPA entity not found
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex, WebRequest request) {

        log.warn("Entity not found: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            "ENTITY_NOT_FOUND",
            ex.getMessage() != null ? ex.getMessage() : "Requested entity not found",
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle general database access exceptions
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(
            DataAccessException ex, WebRequest request) {

        log.error("Database access error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
            "DATABASE_ERROR",
            "A database error occurred. Please try again or contact support.",
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ========================================
    // SECURITY EXCEPTIONS
    // ========================================

    /**
     * Handle Spring Security authentication exceptions
     */
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            org.springframework.security.core.AuthenticationException ex, WebRequest request) {

        log.warn("Authentication failed: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            "AUTHENTICATION_FAILED",
            "Authentication failed. Please check your credentials.",
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle bad credentials
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex, WebRequest request) {

        log.warn("Bad credentials provided");

        ErrorResponse errorResponse = new ErrorResponse(
            "INVALID_CREDENTIALS",
            "Invalid username or password",
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle access denied (authorization)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex, WebRequest request) {

        log.warn("Access denied: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            "ACCESS_DENIED",
            "You do not have permission to access this resource",
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // ========================================
    // HTTP/REQUEST EXCEPTIONS
    // ========================================

    /**
     * Handle unsupported HTTP method
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {

        log.warn("Method not supported: {}", ex.getMethod());

        String message = String.format("HTTP method '%s' is not supported for this endpoint. Supported methods: %s",
                ex.getMethod(),
                ex.getSupportedHttpMethods());

        ErrorResponse errorResponse = new ErrorResponse(
            "METHOD_NOT_SUPPORTED",
            message,
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Handle unsupported media type
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, WebRequest request) {

        log.warn("Media type not supported: {}", ex.getContentType());

        ErrorResponse errorResponse = new ErrorResponse(
            "MEDIA_TYPE_NOT_SUPPORTED",
            String.format("Media type '%s' is not supported. Supported types: %s",
                    ex.getContentType(),
                    ex.getSupportedMediaTypes()),
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Handle malformed JSON requests
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJson(
            HttpMessageNotReadableException ex, WebRequest request) {

        log.warn("Malformed JSON request: {}", ex.getMessage());

        String message = "Malformed JSON request";
        Throwable cause = ex.getCause();

        if (cause instanceof JsonMappingException) {
            message = "JSON mapping error: " + cause.getMessage();
        } else if (cause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) cause;
            message = String.format("Invalid value '%s' for field '%s'",
                    ife.getValue(),
                    ife.getPath().get(0).getFieldName());
        }

        ErrorResponse errorResponse = new ErrorResponse(
            "MALFORMED_JSON",
            message,
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle 404 Not Found
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            NoHandlerFoundException ex, WebRequest request) {

        log.warn("No handler found for: {} {}", ex.getHttpMethod(), ex.getRequestURL());

        ErrorResponse errorResponse = new ErrorResponse(
            "ENDPOINT_NOT_FOUND",
            String.format("No endpoint found for %s %s", ex.getHttpMethod(), ex.getRequestURL()),
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle file upload size exceeded
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex, WebRequest request) {

        log.warn("File upload size exceeded");

        ErrorResponse errorResponse = new ErrorResponse(
            "FILE_SIZE_EXCEEDED",
            "File size exceeds maximum allowed size",
            extractPath(request)
        );
        errorResponse.getDetails().put("maxSize", ex.getMaxUploadSize());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONTENT_TOO_LARGE);
    }

    // ========================================
    // TIMEOUT EXCEPTIONS
    // ========================================

    /**
     * Handle timeout exceptions
     */
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeout(
            TimeoutException ex, WebRequest request) {

        log.error("Request timeout: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            "REQUEST_TIMEOUT",
            "The request timed out. Please try again.",
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.REQUEST_TIMEOUT);
    }

    // ========================================
    // FALLBACK EXCEPTION HANDLER
    // ========================================

    /**
     * Handle all other uncaught exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred. Please contact support if the problem persists.",
            extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ========================================
    // UTILITY METHODS
    // ========================================

    /**
     * Extract clean path from WebRequest
     */
    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
