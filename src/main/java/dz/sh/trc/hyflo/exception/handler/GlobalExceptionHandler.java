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

package dz.sh.trc.hyflo.exception.handler;

import dz.sh.trc.hyflo.exception.base.ApiException;
import dz.sh.trc.hyflo.exception.base.ErrorCode;
import dz.sh.trc.hyflo.exception.base.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.UUID;

/**
 * Slim last-resort orchestrator.
 * Handles anything NOT caught by the four specific sub-handlers above.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Catch-all for any typed ApiException that missed a specific handler. */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, WebRequest req) {
        log.error("Unhandled ApiException [{}]: {}", ex.getCode(), ex.getMessage(), ex);
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.of(ex, path(req)));
    }

    // ── JPA bridging ─────────────────────────────────────────────────────────

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex, WebRequest req) {
        log.warn("JPA EntityNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(simple(ErrorCode.BUS_RESOURCE_NOT_FOUND, ex.getMessage(), req));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccess(DataAccessException ex, WebRequest req) {
        log.error("Spring DataAccessException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(simple(ErrorCode.INF_DATABASE_ERROR, "Database error occurred", req));
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLocking(
            OptimisticLockingFailureException ex, WebRequest req) {
        log.warn("Spring OptimisticLockingFailureException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(simple(ErrorCode.INF_OPTIMISTIC_LOCK_FAILURE,
                        "Concurrent modification detected. Please refresh and retry.", req));
    }

    // ── Spring MVC bridging ──────────────────────────────────────────────────

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandler(NoHandlerFoundException ex, WebRequest req) {
        log.warn("No handler found: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(simple(ErrorCode.BUS_RESOURCE_NOT_FOUND,
                        "No endpoint found for " + ex.getHttpMethod() + " " + ex.getRequestURL(), req));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, WebRequest req) {
        log.warn("Method not supported: {}", ex.getMethod());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(simple(ErrorCode.VAL_METHOD_NOT_ALLOWED,
                        "HTTP method '" + ex.getMethod() + "' is not supported for this endpoint", req));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaType(
            HttpMediaTypeNotSupportedException ex, WebRequest req) {
        log.warn("Unsupported media type: {}", ex.getContentType());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(simple(ErrorCode.VAL_MEDIA_TYPE_UNSUPPORTED,
                        "Content type '" + ex.getContentType() + "' is not supported", req));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(
            HttpMessageNotReadableException ex, WebRequest req) {
        log.warn("Unreadable request body: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(simple(ErrorCode.VAL_INVALID_INPUT,
                        "Request body is missing or malformed", req));
    }

    // ── Final safety net ─────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, WebRequest req) {
        log.error("Unexpected exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.unknown(path(req)));
    }

    // ── Utilities ────────────────────────────────────────────────────────────

    private ErrorResponse simple(ErrorCode code, String message, WebRequest req) {
        return ErrorResponse.builder()
                .timestamp(Instant.now()).correlationId(UUID.randomUUID().toString())
                .errorCode(code.getCode()).message(message).path(path(req)).build();
    }

    private String path(WebRequest req) {
        return req.getDescription(false).replace("uri=", "");
    }
}