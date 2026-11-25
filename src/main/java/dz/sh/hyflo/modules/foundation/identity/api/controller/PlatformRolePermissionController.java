/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PlatformRolePermissionController
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Controller
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.controller
 *
 * @Description : PlatformRolePermissionController for runtime role permission management.
 */

package dz.sh.hyflo.modules.foundation.identity.api.controller;

import java.sql.SQLException;
import java.util.UUID;
import dz.sh.hyflo.modules.foundation.identity.api.request.RolePermissionChangeRequest;
import dz.sh.hyflo.modules.foundation.identity.api.response.PlatformPermissionResponse;
import dz.sh.hyflo.modules.foundation.identity.application.service.PlatformRoleAdministrationService;
import dz.sh.hyflo.modules.foundation.identity.application.service.PlatformRoleAdministrationService.PermissionEntry;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.platform.exception.AuthenticationProblems;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestController
@RequestMapping("/v1/platform")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class PlatformRolePermissionController {
    private final PlatformRoleAdministrationService roles;

    public PlatformRolePermissionController(PlatformRoleAdministrationService roles) {
        this.roles = roles;
    }

    @GetMapping(value = "/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> permissions(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "afterId", defaultValue = "0") long afterId) {
        try {
            return ResponseEntity.ok().header("Cache-Control", "no-store")
                    .body(roles.permissions(principal, afterId).stream().map(PlatformRolePermissionController::response).toList());
        } catch (RuntimeException failure) { return failure(failure); }
    }

    @GetMapping(value = "/roles/{roleId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> rolePermissions(@AuthenticationPrincipal CurrentIdentity principal,
            @PathVariable("roleId") long roleId, @RequestParam(name = "afterId", defaultValue = "0") long afterId) {
        try {
            return ResponseEntity.ok().header("Cache-Control", "no-store")
                    .body(roles.rolePermissions(principal, roleId, afterId).stream().map(PlatformRolePermissionController::response).toList());
        } catch (RuntimeException failure) { return failure(failure); }
    }

    @PostMapping(value = "/roles/{roleId}/permissions/{permissionId}/associate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> associate(@AuthenticationPrincipal CurrentIdentity principal,
            @PathVariable("roleId") long roleId, @PathVariable("permissionId") long permissionId,
            @Valid @RequestBody RolePermissionChangeRequest request) {
        return change(principal, roleId, permissionId, request, true);
    }

    @PostMapping(value = "/roles/{roleId}/permissions/{permissionId}/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> remove(@AuthenticationPrincipal CurrentIdentity principal,
            @PathVariable("roleId") long roleId, @PathVariable("permissionId") long permissionId,
            @Valid @RequestBody RolePermissionChangeRequest request) {
        return change(principal, roleId, permissionId, request, false);
    }

    private ResponseEntity<?> change(CurrentIdentity principal, long roleId, long permissionId,
            RolePermissionChangeRequest request, boolean associate) {
        try {
            roles.changePermission(principal, roleId, permissionId, request.reason(), associate);
            return ResponseEntity.noContent().header("Cache-Control", "no-store").build();
        } catch (RuntimeException failure) { return failure(failure); }
    }

    private static PlatformPermissionResponse response(PermissionEntry p) {
        return new PlatformPermissionResponse(p.id(), p.code(), p.displayName(), p.actionCode(),
                p.resourceTypeCode(), p.platformPermission(), p.supported());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> malformed() { return problem(HttpStatus.BAD_REQUEST); }

    private static ResponseEntity<?> failure(RuntimeException failure) {
        if (failure instanceof AuthenticationException) return problem(HttpStatus.UNAUTHORIZED);
        if (failure instanceof AccessDeniedException) return problem(HttpStatus.FORBIDDEN);
        if (failure instanceof IllegalArgumentException) return problem(HttpStatus.BAD_REQUEST);
        if (failure instanceof PlatformRoleAdministrationService.RoleChangeRejected) return problem(HttpStatus.CONFLICT);
        for (Throwable cause = failure; cause != null; cause = cause.getCause()) {
            if (cause instanceof SQLException sql && sql.getErrorCode() == 1062) return problem(HttpStatus.CONFLICT);
        }
        return problem(HttpStatus.SERVICE_UNAVAILABLE);
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
