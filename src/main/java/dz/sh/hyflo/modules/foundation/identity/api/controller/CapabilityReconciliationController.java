/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : CapabilityReconciliationController
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Controller
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.controller
 *
 * @Description : CapabilityReconciliationController for controlled capability reconciliation.
 */

package dz.sh.hyflo.modules.foundation.identity.api.controller;

import java.util.UUID;
import dz.sh.hyflo.modules.foundation.identity.api.request.CapabilityReconciliationRequest;
import dz.sh.hyflo.modules.foundation.identity.api.response.CapabilityReconciliationResponse;
import dz.sh.hyflo.modules.foundation.identity.application.service.CapabilityReconciliationService;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.platform.exception.AuthenticationProblems;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/platform/capabilities/reconcile")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CapabilityReconciliationController {
    private final CapabilityReconciliationService reconciliation;

    public CapabilityReconciliationController(CapabilityReconciliationService reconciliation) {
        this.reconciliation = reconciliation;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reconcile(@AuthenticationPrincipal CurrentIdentity principal,
            @Valid @RequestBody CapabilityReconciliationRequest request) {
        try {
            var result = reconciliation.reconcile(principal, request.reason());
            return ResponseEntity.ok().header("Cache-Control", "no-store")
                    .body(new CapabilityReconciliationResponse(result.manifestId(), result.manifestDigest(),
                            result.insertedActionCodes(), result.insertedResourceTypeCodes(), result.insertedPermissionCodes()));
        } catch (AuthenticationException unauthenticated) {
            return problem(HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException denied) {
            return problem(HttpStatus.FORBIDDEN);
        } catch (CapabilityReconciliationService.Drift drift) {
            return problem(HttpStatus.CONFLICT);
        } catch (IllegalArgumentException invalid) {
            return problem(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException unavailable) {
            return problem(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> malformed() { return problem(HttpStatus.BAD_REQUEST); }

    private static ResponseEntity<?> problem(HttpStatus status) {
        String correlation = UUID.randomUUID().toString();
        ProblemDetail body = status == HttpStatus.CONFLICT
                ? ProblemDetail.forStatusAndDetail(status, "Capability definition conflict")
                : AuthenticationProblems.problem(status, correlation);
        if (status == HttpStatus.CONFLICT) {
            body.setProperty("code", "CAPABILITY_DEFINITION_CONFLICT");
            body.setProperty("correlationId", correlation);
        }
        var response = ResponseEntity.status(status).header("Cache-Control", "no-store")
                .contentType(MediaType.APPLICATION_PROBLEM_JSON);
        if (status == HttpStatus.UNAUTHORIZED) response.header("WWW-Authenticate", "Bearer");
        return response.body(body);
    }
}
