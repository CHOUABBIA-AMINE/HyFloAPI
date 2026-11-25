/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PlatformRoleController
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Controller
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.controller
 *
 * @Description : PlatformRoleController for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.api.controller;

import java.sql.SQLException;
import java.util.UUID;

import dz.sh.hyflo.modules.foundation.identity.api.request.CreatePlatformRoleRequest;
import dz.sh.hyflo.modules.foundation.identity.api.response.PlatformRoleResponse;
import dz.sh.hyflo.modules.foundation.identity.application.service.PlatformRoleAdministrationService;
import dz.sh.hyflo.modules.foundation.identity.application.service.PlatformRoleAdministrationService.RoleEntry;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.platform.exception.AuthenticationProblems;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/platform/roles")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class PlatformRoleController {
    private final PlatformRoleAdministrationService roles;

    public PlatformRoleController(PlatformRoleAdministrationService roles) {
        this.roles = roles;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> list(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "afterId", required = false) String afterId) {
        try {
            if (afterId != null && !afterId.matches("[0-9]{1,19}")) throw new IllegalArgumentException("Invalid cursor");
            long cursor = afterId == null ? 0 : Long.parseLong(afterId);
            return ResponseEntity.ok().header("Cache-Control", "no-store")
                    .body(roles.list(principal, cursor).stream().map(PlatformRoleController::response).toList());
        } catch (AuthenticationException unauthenticated) {
            return problem(HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException denied) {
            return problem(HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException invalid) {
            return problem(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException unavailable) {
            return problem(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@AuthenticationPrincipal CurrentIdentity principal,
            @Valid @RequestBody CreatePlatformRoleRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).header("Cache-Control", "no-store")
                    .body(response(roles.create(principal, request.code(), request.displayName(),
                            request.platformRole(), request.reason())));
        } catch (AuthenticationException unauthenticated) {
            return problem(HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException denied) {
            return problem(HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException invalid) {
            return problem(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException failure) {
            return problem(duplicate(failure) ? HttpStatus.CONFLICT : HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private static boolean duplicate(Throwable failure) {
        for (Throwable cause = failure; cause != null; cause = cause.getCause()) {
            if (cause instanceof SQLException sql && sql.getErrorCode() == 1062) return true;
        }
        return false;
    }

    private static PlatformRoleResponse response(RoleEntry role) {
        return new PlatformRoleResponse(role.id(), role.code(), role.displayName(), role.enabled(), role.platformRole());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> malformed() {
        return problem(HttpStatus.BAD_REQUEST);
    }

    private static ResponseEntity<?> problem(HttpStatus status) {
        String correlation = UUID.randomUUID().toString();
        ProblemDetail body = status == HttpStatus.CONFLICT
                ? ProblemDetail.forStatusAndDetail(status, "Role operation rejected")
                : AuthenticationProblems.problem(status, correlation);
        if (status == HttpStatus.CONFLICT) {
            body.setProperty("code", "ROLE_OPERATION_REJECTED");
            body.setProperty("correlationId", correlation);
        }
        var response = ResponseEntity.status(status).header("Cache-Control", "no-store")
                .contentType(MediaType.APPLICATION_PROBLEM_JSON);
        if (status == HttpStatus.UNAUTHORIZED) response.header("WWW-Authenticate", "Bearer");
        return response.body(body);
    }
}
