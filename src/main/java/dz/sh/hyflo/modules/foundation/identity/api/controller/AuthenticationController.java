/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuthenticationController
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Controller
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.controller
 *
 * @Description : Exposes local authentication, own-session lifecycle and current identity.
 */

package dz.sh.hyflo.modules.foundation.identity.api.controller;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

import dz.sh.hyflo.modules.foundation.identity.api.mapper.AuthenticationMapper;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        return response.body(AuthenticationProblems.problem(status, correlation));
    }
}
