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
import dz.sh.trc.hyflo.exception.security.AuthenticationException;
import dz.sh.trc.hyflo.exception.security.AuthorizationException;
import dz.sh.trc.hyflo.exception.security.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex, WebRequest req) {
        log.warn("Authentication failure: {}", ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.of(ex, path(req)));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorization(AuthorizationException ex, WebRequest req) {
        log.warn("Authorization failure: {}", ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.of(ex, path(req)));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handleToken(TokenException ex, WebRequest req) {
        log.warn("Token failure [{}]: {}", ex.getCode(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.of(ex, path(req)));
    }

    /** Bridge: Spring Security's AccessDeniedException. */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, WebRequest req) {
        log.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(simple(ErrorCode.RBAC_ACCESS_DENIED, ex.getMessage(), req));
    }

    /** Bridge: Spring Security's BadCredentialsException. */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, WebRequest req) {
        log.warn("Bad credentials attempt");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(simple(ErrorCode.AUTH_INVALID_CREDENTIALS,
                        ErrorCode.AUTH_INVALID_CREDENTIALS.getDefaultMessage(), req));
    }

    /** Bridge: disabled account. */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabled(DisabledException ex, WebRequest req) {
        log.warn("Disabled account login attempt");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(simple(ErrorCode.AUTH_ACCOUNT_DISABLED,
                        ErrorCode.AUTH_ACCOUNT_DISABLED.getDefaultMessage(), req));
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