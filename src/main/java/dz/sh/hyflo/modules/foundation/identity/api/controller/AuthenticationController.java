/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuthenticationController
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Controller
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.controller
 *
 * @Description : Exposes local authentication, own-session lifecycle, own password change and current identity.
 */

package dz.sh.hyflo.modules.foundation.identity.api.controller;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

import dz.sh.hyflo.modules.foundation.identity.api.mapper.AuthenticationMapper;
import dz.sh.hyflo.modules.foundation.identity.api.request.ChangeOwnEmailRequest;
import dz.sh.hyflo.modules.foundation.identity.api.response.OwnProfileResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ProblemDetail;
import dz.sh.hyflo.modules.foundation.identity.api.request.ChangePasswordRequest;
import dz.sh.hyflo.modules.foundation.identity.api.request.LoginRequest;
import dz.sh.hyflo.modules.foundation.identity.api.request.RefreshRequest;
import dz.sh.hyflo.modules.foundation.identity.api.response.CurrentIdentityResponse;
import dz.sh.hyflo.modules.foundation.identity.application.service.AuthenticationService;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.platform.security.LoginAttemptLimiter;
import dz.sh.hyflo.platform.exception.AuthenticationProblems;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class AuthenticationController {

    private final AuthenticationService authentication;
    private final AuthenticationMapper mapper;
    private final LoginAttemptLimiter limiter;

    public AuthenticationController(AuthenticationService authentication, AuthenticationMapper mapper, LoginAttemptLimiter limiter) {
        this.authentication = authentication;
        this.mapper = mapper;
        this.limiter = limiter;
    }

    @PostMapping(path = "/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String correlation = UUID.randomUUID().toString();
        String username;
        try {
            username = User.normalizeUsername(request.username());
            if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
                return problem(HttpStatus.BAD_REQUEST, correlation, 0);
            }
        } catch (IllegalArgumentException invalid) {
            return problem(HttpStatus.BAD_REQUEST, correlation, 0);
        }

        long retry = limiter.retryAfter(httpRequest.getRemoteAddr(), username, Instant.now());
        if (retry > 0) {
            return problem(HttpStatus.TOO_MANY_REQUESTS, correlation, retry);
        }
        try {
            var result = authentication.login(username, request.password(), correlation);
            if (result.isEmpty()) {
                return problem(HttpStatus.UNAUTHORIZED, correlation, 0);
            }
            return ResponseEntity.ok().header("Cache-Control", "no-store")
                    .header("Pragma", "no-cache").body(mapper.toTokenResponse(result.orElseThrow()));
        } catch (RuntimeException unavailable) {
            return problem(HttpStatus.SERVICE_UNAVAILABLE, correlation, 0);
        }
    }

    @PostMapping(path = "/auth/refresh", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshRequest request, HttpServletRequest httpRequest) {
        String correlation = UUID.randomUUID().toString();
        long retry = limiter.retryAfterRefresh(httpRequest.getRemoteAddr(), Instant.now());
        if (retry > 0) return problem(HttpStatus.TOO_MANY_REQUESTS, correlation, retry);
        try {
            var result = authentication.refresh(request.refreshToken(), correlation);
            if (result.isEmpty()) return problem(HttpStatus.UNAUTHORIZED, correlation, 0);
            return ResponseEntity.ok().header("Cache-Control", "no-store")
                    .header("Pragma", "no-cache").body(mapper.toTokenResponse(result.orElseThrow()));
        } catch (RuntimeException unavailable) {
            return problem(HttpStatus.SERVICE_UNAVAILABLE, correlation, 0);
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal CurrentIdentity identity) {
        String correlation = UUID.randomUUID().toString();
        try {
            if (!authentication.logout(identity, correlation)) return problem(HttpStatus.UNAUTHORIZED, correlation, 0);
            return ResponseEntity.noContent().header("Cache-Control", "no-store").build();
        } catch (RuntimeException unavailable) {
            return problem(HttpStatus.SERVICE_UNAVAILABLE, correlation, 0);
        }
    }

    @PostMapping(path = "/auth/password/change", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal CurrentIdentity identity,
            @Valid @RequestBody ChangePasswordRequest request, HttpServletRequest httpRequest) {
        String correlation = UUID.randomUUID().toString();
        if (identity == null) return problem(HttpStatus.UNAUTHORIZED, correlation, 0);
        if (!AuthenticationService.validPasswordChangeInput(request.currentPassword(), request.newPassword())) {
            return problem(HttpStatus.BAD_REQUEST, correlation, 0);
        }
        long retry = limiter.retryAfter(httpRequest.getRemoteAddr(), identity.username(), Instant.now());
        if (retry > 0) return problem(HttpStatus.TOO_MANY_REQUESTS, correlation, retry);
        try {
            if (!authentication.changePassword(identity, request.currentPassword(), request.newPassword(), correlation)) {
                return problem(HttpStatus.UNAUTHORIZED, correlation, 0);
            }
            return ResponseEntity.noContent().header("Cache-Control", "no-store").build();
        } catch (RuntimeException unavailable) {
            return problem(HttpStatus.SERVICE_UNAVAILABLE, correlation, 0);
        }
    }

    @GetMapping("/auth/sessions")
    public ResponseEntity<?> ownSessions(@AuthenticationPrincipal CurrentIdentity identity,
            @RequestParam(name = "afterId", required = false) String afterId) {
        String correlation = UUID.randomUUID().toString();
        long cursor;
        try {
            cursor = afterId == null ? 0 : parseSessionNumber(afterId);
        } catch (IllegalArgumentException invalid) {
            return problem(HttpStatus.BAD_REQUEST, correlation, 0);
        }
        try {
            return ResponseEntity.ok().header("Cache-Control", "no-store")
                    .body(authentication.ownSessions(identity, cursor).stream()
                            .map(mapper::toOwnSessionResponse).toList());
        } catch (AuthenticationException unauthenticated) {
            return problem(HttpStatus.UNAUTHORIZED, correlation, 0);
        } catch (RuntimeException unavailable) {
            return problem(HttpStatus.SERVICE_UNAVAILABLE, correlation, 0);
        }
    }

    @PostMapping("/auth/sessions/{sessionId}/revoke")
    public ResponseEntity<?> revokeOwnSession(@AuthenticationPrincipal CurrentIdentity identity,
            @PathVariable("sessionId") String sessionId) {
        String correlation = UUID.randomUUID().toString();
        long target;
        try {
            target = parseSessionNumber(sessionId);
            if (target <= 0) throw new IllegalArgumentException("Invalid session ID");
        } catch (IllegalArgumentException invalid) {
            return problem(HttpStatus.BAD_REQUEST, correlation, 0);
        }
        try {
            authentication.revokeOwnSession(identity, target, correlation);
            return ResponseEntity.noContent().header("Cache-Control", "no-store").build();
        } catch (AuthenticationException unauthenticated) {
            return problem(HttpStatus.UNAUTHORIZED, correlation, 0);
        } catch (AccessDeniedException denied) {
            return problem(HttpStatus.FORBIDDEN, correlation, 0);
        } catch (RuntimeException unavailable) {
            return problem(HttpStatus.SERVICE_UNAVAILABLE, correlation, 0);
        }
    }

    private static long parseSessionNumber(String value) {
        if (!value.matches("[0-9]{1,19}")) {
            throw new IllegalArgumentException("Invalid session number");
        }
        return Long.parseLong(value);
    }

    @GetMapping("/me/profile")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = OwnProfileResponse.class)))
    public ResponseEntity<?> ownProfile(@AuthenticationPrincipal CurrentIdentity identity, HttpServletRequest httpRequest) {
        String correlation = UUID.randomUUID().toString();
        if (!httpRequest.getParameterMap().isEmpty()) return problem(HttpStatus.BAD_REQUEST, correlation, 0);
        try {
            return ResponseEntity.ok().header("Cache-Control", "no-store")
                    .body(mapper.toOwnProfileResponse(authentication.ownProfile(identity)));
        } catch (AuthenticationException unauthenticated) {
            return problem(HttpStatus.UNAUTHORIZED, correlation, 0);
        } catch (RuntimeException unavailable) {
            return problem(HttpStatus.SERVICE_UNAVAILABLE, correlation, 0);
        }
    }

    @PostMapping(path = "/me/profile/email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = OwnProfileResponse.class)))
    public ResponseEntity<?> changeOwnEmail(@AuthenticationPrincipal CurrentIdentity identity,
            @Valid @RequestBody ChangeOwnEmailRequest request, HttpServletRequest httpRequest) {
        String correlation = UUID.randomUUID().toString();
        if (identity == null) return problem(HttpStatus.UNAUTHORIZED, correlation, 0);
        if (!httpRequest.getParameterMap().isEmpty()
                || !AuthenticationService.validOwnEmailInput(request.email(), request.currentPassword())) {
            return problem(HttpStatus.BAD_REQUEST, correlation, 0);
        }
        long retry = limiter.retryAfter(httpRequest.getRemoteAddr(), identity.username(), Instant.now());
        if (retry > 0) return problem(HttpStatus.TOO_MANY_REQUESTS, correlation, retry);
        try {
            var result = authentication.changeOwnEmail(identity, request.email(), request.currentPassword(), correlation);
            if (result.isEmpty()) return problem(HttpStatus.UNAUTHORIZED, correlation, 0);
            return ResponseEntity.ok().header("Cache-Control", "no-store")
                    .body(mapper.toOwnProfileResponse(result.orElseThrow()));
        } catch (AuthenticationException unauthenticated) {
            return problem(HttpStatus.UNAUTHORIZED, correlation, 0);
        } catch (AccessDeniedException denied) {
            return problem(HttpStatus.FORBIDDEN, correlation, 0);
        } catch (RuntimeException failure) {
            return problem(emailCollision(failure) ? HttpStatus.CONFLICT : HttpStatus.SERVICE_UNAVAILABLE, correlation, 0);
        }
    }

    private static boolean emailCollision(Throwable failure) {
        for (Throwable cause = failure; cause != null; cause = cause.getCause()) {
            if (cause instanceof java.sql.SQLException sql && sql.getErrorCode() == 1062
                    && sql.getMessage() != null && sql.getMessage().contains("T_10_01_08_UK_02")) return true;
        }
        return false;
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentIdentityResponse> me(@AuthenticationPrincipal CurrentIdentity identity) {
        return ResponseEntity.ok().header("Cache-Control", "no-store").body(mapper.toCurrentIdentityResponse(identity));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> malformedRequest() {
        return problem(HttpStatus.BAD_REQUEST, UUID.randomUUID().toString(), 0);
    }

    private ResponseEntity<?> problem(HttpStatus status, String correlation, long retry) {
        var response = ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .header("Cache-Control", "no-store");
        if (status == HttpStatus.UNAUTHORIZED) {
            response.header("WWW-Authenticate", "Bearer");
        }
        if (retry > 0) {
            response.header("Retry-After", Long.toString(retry));
        }
        if (status == HttpStatus.CONFLICT) {
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, "Account operation rejected");
            problem.setProperty("code", "ACCOUNT_OPERATION_REJECTED");
            problem.setProperty("correlationId", correlation);
            return response.body(problem);
        }
        return response.body(AuthenticationProblems.problem(status, correlation));
    }
}
