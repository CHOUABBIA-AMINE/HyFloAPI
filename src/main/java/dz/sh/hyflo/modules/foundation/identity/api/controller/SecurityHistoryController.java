/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : SecurityHistoryController
 * @CreatedOn   : 11-26-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Controller
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.controller
 *
 * @Description : Exposes seven explicitly authorized, bounded Identity history collections.
 */

package dz.sh.hyflo.modules.foundation.identity.api.controller;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import dz.sh.hyflo.modules.foundation.identity.api.mapper.SecurityHistoryMapper;
import dz.sh.hyflo.modules.foundation.identity.api.response.SecurityHistoryResponse;
import dz.sh.hyflo.modules.foundation.identity.application.service.SecurityHistoryService;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.platform.exception.AuthenticationProblems;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityHistoryController {
    private final SecurityHistoryService history;
    private final SecurityHistoryMapper mapper;

    public SecurityHistoryController(SecurityHistoryService history, SecurityHistoryMapper mapper) {
        this.history = history;
        this.mapper = mapper;
    }

    @GetMapping(path = "/identity/security-history/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Authorized history page",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SecurityHistoryResponse.Authentication.class))))
    public ResponseEntity<?> authentication(@Parameter(hidden = true) @AuthenticationPrincipal CurrentIdentity principal,
            @Parameter(required = true) @RequestParam(name = "companyId", required = false) String companyId,
            @RequestParam(name = "afterId", required = false) String afterId,
            HttpServletRequest request) {
        return read(request, true, () -> history.authentication(principal, positive(companyId), cursor(afterId)).stream()
                .map(mapper::authentication).toList());
    }

    @GetMapping(path = "/identity/security-history/account-lifecycle", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Authorized history page",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SecurityHistoryResponse.AccountLifecycle.class))))
    public ResponseEntity<?> accountLifecycle(@Parameter(hidden = true) @AuthenticationPrincipal CurrentIdentity principal,
            @Parameter(required = true) @RequestParam(name = "companyId", required = false) String companyId,
            @RequestParam(name = "afterId", required = false) String afterId,
            HttpServletRequest request) {
        return read(request, true, () -> history.accountLifecycle(principal, positive(companyId), cursor(afterId)).stream()
                .map(mapper::accountLifecycle).toList());
    }

    @GetMapping(path = "/identity/security-history/employee-associations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Authorized history page",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SecurityHistoryResponse.EmployeeAssociation.class))))
    public ResponseEntity<?> employeeAssociations(@Parameter(hidden = true) @AuthenticationPrincipal CurrentIdentity principal,
            @Parameter(required = true) @RequestParam(name = "companyId", required = false) String companyId,
            @RequestParam(name = "afterId", required = false) String afterId,
            HttpServletRequest request) {
        return read(request, true, () -> history.employeeAssociations(principal, positive(companyId), cursor(afterId)).stream()
                .map(mapper::employeeAssociations).toList());
    }

    @GetMapping(path = "/platform/security-history/role-definitions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Authorized history page",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SecurityHistoryResponse.RoleDefinition.class))))
    public ResponseEntity<?> roleDefinitions(@Parameter(hidden = true) @AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "afterId", required = false) String afterId,
            HttpServletRequest request) {
        return read(request, false, () -> history.roleDefinitions(principal, cursor(afterId)).stream()
                .map(mapper::roleDefinitions).toList());
    }

    @GetMapping(path = "/platform/security-history/group-definitions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Authorized history page",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SecurityHistoryResponse.GroupDefinition.class))))
    public ResponseEntity<?> groupDefinitions(@Parameter(hidden = true) @AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "afterId", required = false) String afterId,
            HttpServletRequest request) {
        return read(request, false, () -> history.groupDefinitions(principal, cursor(afterId)).stream()
                .map(mapper::groupDefinitions).toList());
    }

    @GetMapping(path = "/platform/security-history/capability-reconciliations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Authorized history page",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SecurityHistoryResponse.CapabilityReconciliation.class))))
    public ResponseEntity<?> capabilityReconciliations(@Parameter(hidden = true) @AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "afterId", required = false) String afterId,
            HttpServletRequest request) {
        return read(request, false, () -> history.capabilityReconciliations(principal, cursor(afterId)).stream()
                .map(mapper::capabilityReconciliations).toList());
    }

    @GetMapping(path = "/platform/security-history/unattributed-authentication", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Authorized history page",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SecurityHistoryResponse.UnattributedAuthentication.class))))
    public ResponseEntity<?> unattributedAuthentication(@Parameter(hidden = true) @AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "afterId", required = false) String afterId,
            HttpServletRequest request) {
        return read(request, false, () -> history.unattributedAuthentication(principal, cursor(afterId)).stream()
                .map(mapper::unattributedAuthentication).toList());
    }

    private ResponseEntity<?> read(HttpServletRequest request, boolean company, Supplier<?> operation) {
        try {
            Set<String> allowed = company ? Set.of("companyId", "afterId") : Set.of("afterId");
            Map<String, String[]> parameters = request.getParameterMap();
            if (!allowed.containsAll(parameters.keySet()) || parameters.values().stream().anyMatch(v -> v.length != 1)
                    || request.getContentLengthLong() > 0 || request.getHeader("Transfer-Encoding") != null
                    || request.getInputStream().read() != -1)
                return problem(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok().header("Cache-Control", "no-store").body(operation.get());
        } catch (IllegalArgumentException invalid) {
            return problem(HttpStatus.BAD_REQUEST);
        } catch (AuthenticationException unauthenticated) {
            return problem(HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException forbidden) {
            return problem(HttpStatus.FORBIDDEN);
        } catch (RuntimeException | java.io.IOException unavailable) {
            return problem(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private static long cursor(String value) {
        if (value == null) return 0;
        if (!value.matches("[0-9]{1,19}")) throw new IllegalArgumentException("Invalid cursor");
        return Long.parseLong(value);
    }

    private static long positive(String value) {
        if (value == null) throw new IllegalArgumentException("Company required");
        long id = cursor(value);
        if (id <= 0) throw new IllegalArgumentException("Invalid company");
        return id;
    }

    private static ResponseEntity<?> problem(HttpStatus status) {
        var response = ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .header("Cache-Control", "no-store");
        if (status == HttpStatus.UNAUTHORIZED) response.header("WWW-Authenticate", "Bearer");
        return response.body(AuthenticationProblems.problem(status, UUID.randomUUID().toString()));
    }
}
