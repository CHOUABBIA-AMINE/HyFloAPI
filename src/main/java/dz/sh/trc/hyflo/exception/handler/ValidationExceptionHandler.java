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
import dz.sh.trc.hyflo.exception.validation.InvalidInputException;
import dz.sh.trc.hyflo.exception.validation.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex, WebRequest req) {
        log.warn("Validation [{}]: {}", ex.getCode(), ex.getMessage());
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(Instant.now()).correlationId(UUID.randomUUID().toString())
                .errorCode(ex.getCode()).message(ex.getMessage()).path(path(req))
                .details(ex.getFieldErrors().isEmpty() ? null : Map.of("fieldErrors", ex.getFieldErrors()))
                .build();
        return ResponseEntity.status(ex.getHttpStatus()).body(body);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInput(InvalidInputException ex, WebRequest req) {
        log.warn("Invalid input: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.of(ex, path(req)));
    }

    /** @Valid on controller method parameters. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgument(MethodArgumentNotValidException ex, WebRequest req) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "invalid",
                        (a, b) -> a + "; " + b));
        log.warn("@Valid failure: {}", fieldErrors);
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .timestamp(Instant.now()).correlationId(UUID.randomUUID().toString())
                .errorCode(ErrorCode.VAL_CONSTRAINT_VIOLATION.getCode())
                .message("Validation failed for one or more fields").path(path(req))
                .details(Map.of("fieldErrors", fieldErrors)).build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex, WebRequest req) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (a, b) -> a + "; " + b));
        log.warn("Constraint violations: {}", errors);
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .timestamp(Instant.now()).correlationId(UUID.randomUUID().toString())
                .errorCode(ErrorCode.VAL_CONSTRAINT_VIOLATION.getCode())
                .message("Constraint violation").path(path(req))
                .details(Map.of("violations", errors)).build());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(
            MissingServletRequestParameterException ex, WebRequest req) {
        log.warn("Missing param: {}", ex.getParameterName());
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .timestamp(Instant.now()).correlationId(UUID.randomUUID().toString())
                .errorCode(ErrorCode.VAL_MISSING_FIELD.getCode())
                .message("Required parameter '" + ex.getParameterName() + "' is missing")
                .path(path(req))
                .details(Map.of("parameterName", ex.getParameterName(),
                                "parameterType", ex.getParameterType())).build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest req) {
        String msg = String.format("Invalid value '%s' for parameter '%s' (expected %s)",
                ex.getValue(), ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "?");
        log.warn("Type mismatch: {}", msg);
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .timestamp(Instant.now()).correlationId(UUID.randomUUID().toString())
                .errorCode(ErrorCode.VAL_TYPE_MISMATCH.getCode())
                .message(msg).path(path(req)).build());
    }

    private String path(WebRequest req) {
        return req.getDescription(false).replace("uri=", "");
    }
}