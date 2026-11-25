/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuthorizationController
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Controller
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.controller
 *
 * @Description : Exposes own company permissions and permission-protected shared Identity catalogs.
 */

package dz.sh.hyflo.modules.foundation.identity.api.controller;

import java.util.UUID;
import java.util.function.Supplier;

import dz.sh.hyflo.modules.foundation.identity.api.mapper.AuthorizationMapper;
import dz.sh.hyflo.modules.foundation.identity.application.service.EffectiveAuthorizationService;
import dz.sh.hyflo.modules.foundation.identity.application.service.IdentityCatalogService;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.platform.exception.AuthenticationProblems;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class AuthorizationController {

    private final EffectiveAuthorizationService authorization;
    private final IdentityCatalogService catalogs;
    private final AuthorizationMapper mapper;

    public AuthorizationController(EffectiveAuthorizationService authorization, IdentityCatalogService catalogs,
            AuthorizationMapper mapper) {
        this.authorization = authorization;
        this.catalogs = catalogs;
        this.mapper = mapper;
    }

    @GetMapping("/me/permissions")
    public ResponseEntity<?> ownPermissions(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String companyId) {
        return read(companyId, () -> mapper.toPermissions(authorization.ownPermissions(principal, parseCompany(companyId))));
    }

    @GetMapping("/identity/user-statuses")
    public ResponseEntity<?> userStatuses(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String companyId) {
        return read(companyId, () -> catalogs.userStatuses(principal, parseCompany(companyId)).stream()
                .map(mapper::toStatus).toList());
    }

    @GetMapping("/identity/roles")
    public ResponseEntity<?> roles(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String companyId,
            @RequestParam(name = "afterId", required = false) String afterId) {
        return readPage(companyId, afterId, null, () -> catalogs.roles(principal, parseCompany(companyId),
                parseCursor(afterId)).stream().map(mapper::toRole).toList());
    }

    @GetMapping("/identity/permissions")
    public ResponseEntity<?> permissions(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String companyId,
            @RequestParam(name = "afterId", required = false) String afterId) {
        return readPage(companyId, afterId, null, () -> catalogs.permissions(principal, parseCompany(companyId),
                parseCursor(afterId)).stream().map(mapper::toPermission).toList());
    }

    @GetMapping("/identity/roles/{roleId}/permissions")
    public ResponseEntity<?> rolePermissions(@AuthenticationPrincipal CurrentIdentity principal,
            @PathVariable("roleId") String roleId,
            @RequestParam(name = "companyId", required = false) String companyId,
            @RequestParam(name = "afterId", required = false) String afterId) {
        return readPage(companyId, afterId, roleId, () -> catalogs.rolePermissions(principal, parseCompany(companyId),
                parseCompany(roleId), parseCursor(afterId)).stream().map(mapper::toPermission).toList());
    }

    private ResponseEntity<?> readPage(String companyId, String afterId, String roleId, Supplier<?> operation) {
        try {
            parseCursor(afterId);
            if (roleId != null) {
                parseCompany(roleId);
            }
        } catch (IllegalArgumentException invalid) {
            return problem(HttpStatus.BAD_REQUEST);
        }
        return read(companyId, operation);
    }

    private static long parseCursor(String value) {
        if (value == null) {
            return 0;
        }
        if (!value.matches("[0-9]{1,19}")) {
            throw new IllegalArgumentException("Invalid catalog cursor");
        }
        return Long.parseLong(value);
    }

    private ResponseEntity<?> read(String selector, Supplier<?> operation) {
        try {
            parseCompany(selector);
        } catch (IllegalArgumentException invalid) {
            return problem(HttpStatus.BAD_REQUEST);
        }
        try {
            return ResponseEntity.ok().header("Cache-Control", "no-store").body(operation.get());
        } catch (AuthenticationException unauthenticated) {
            return problem(HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException forbidden) {
            return problem(HttpStatus.FORBIDDEN);
        } catch (RuntimeException unavailable) {
            return problem(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private static long parseCompany(String value) {
        if (value == null || !value.matches("[0-9]{1,19}")) {
            throw new IllegalArgumentException("Invalid company selector");
        }
        long id = Long.parseLong(value);
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid company selector");
        }
        return id;
    }

    private static ResponseEntity<?> problem(HttpStatus status) {
        var response = ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .header("Cache-Control", "no-store");
        if (status == HttpStatus.UNAUTHORIZED) {
            response.header("WWW-Authenticate", "Bearer");
        }
        return response.body(AuthenticationProblems.problem(status, UUID.randomUUID().toString()));
    }
}
