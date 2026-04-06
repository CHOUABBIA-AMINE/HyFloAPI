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

import dz.sh.trc.hyflo.exception.base.ErrorCode;
import dz.sh.trc.hyflo.exception.base.ErrorResponse;
import dz.sh.trc.hyflo.exception.infrastructure.*;
import dz.sh.trc.hyflo.exception.resilience.RateLimitExceededException;
import dz.sh.trc.hyflo.exception.resilience.RequestTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestControllerAdvice
public class InfrastructureExceptionHandler {

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ErrorResponse> handleDb(DatabaseOperationException ex, WebRequest req) {
        log.error("DB error [{}]: {}", ex.getCode(), ex.getMessage(), ex);
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.of(ex, path(req)));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrity(DataIntegrityViolationException ex, WebRequest req) {
        log.error("Data integrity [{}]: {}", ex.getCode(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.of(ex, path(req)));
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternal(ExternalServiceException ex, WebRequest req) {
        log.error("External service '{}' error: {}", ex.getServiceName(), ex.getMessage(), ex);
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.of(ex, path(req)));
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock(OptimisticLockException ex, WebRequest req) {
        log.warn("Optimistic lock conflict: {}", ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.of(ex, path(req)));
    }

    @ExceptionHandler(FileOperationException.class)
    public ResponseEntity<ErrorResponse> handleFile(FileOperationException ex, WebRequest req) {
        log.error("File operation error [{}]: {}", ex.getCode(), ex.getMessage(), ex);
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.of(ex, path(req)));
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimit(RateLimitExceededException ex, WebRequest req) {
        log.warn("Rate limit exceeded, retry after {}s", ex.getRetryAfterSeconds());
        return ResponseEntity.status(ex.getHttpStatus())
                .header("Retry-After", String.valueOf(ex.getRetryAfterSeconds()))
                .body(ErrorResponse.of(ex, path(req)));
    }

    @ExceptionHandler(RequestTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeout(RequestTimeoutException ex, WebRequest req) {
        log.warn("Request timeout: {}", ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.of(ex, path(req)));
    }

    /** Bridge: java.util.concurrent.TimeoutException from @Async operations. */
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse> handleJavaTimeout(TimeoutException ex, WebRequest req) {
        log.warn("Async timeout: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body(simple(ErrorCode.RES_REQUEST_TIMEOUT, "Operation timed out", req));
    }

    /** Bridge: Spring multipart upload size limit. */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUpload(MaxUploadSizeExceededException ex, WebRequest req) {
        log.warn("Max upload size exceeded");
        return ResponseEntity.status(HttpStatus.CONTENT_TOO_LARGE)
                .body(simple(ErrorCode.FILE_SIZE_EXCEEDED,
                        "Uploaded file exceeds the maximum allowed size", req));
    }

    private ErrorResponse simple(ErrorCode code, String message, WebRequest req) {
        return ErrorResponse.builder()
                .timestamp(Instant.now()).correlationId(UUID.randomUUID().toString())
                .errorCode(code.getCode()).message(message).path(path(req)).build();
    }

    private String path(WebRequest req) {
        return req.getDescription(false).replace("uri=", "");
    }
}