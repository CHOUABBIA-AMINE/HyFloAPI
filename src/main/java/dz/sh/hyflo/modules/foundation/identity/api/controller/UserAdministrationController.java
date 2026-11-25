/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserAdministrationController
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Controller
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.controller
 *
 * @Description : Admits only authorized company-owned account operations.
 */

package dz.sh.hyflo.modules.foundation.identity.api.controller;

import java.util.UUID;
import java.util.function.Function;

import dz.sh.hyflo.modules.foundation.identity.api.mapper.UserAccountMapper;
import dz.sh.hyflo.modules.foundation.identity.api.request.CreateUserRequest;
import dz.sh.hyflo.modules.foundation.identity.api.request.LocalCredentialRequest;
import dz.sh.hyflo.modules.foundation.identity.api.request.UserLifecycleRequest;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.application.service.UserAdministrationService;
import dz.sh.hyflo.modules.foundation.identity.application.service.UserAdministrationService.AccountOperationRejected;
import dz.sh.hyflo.platform.exception.AuthenticationProblems;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("/v1/identity/users")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class UserAdministrationController {

    private final UserAdministrationService accounts;
    private final UserAccountMapper mapper;

    public UserAdministrationController(UserAdministrationService accounts, UserAccountMapper mapper) {
        this.accounts = accounts;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<?> list(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String company,
            @RequestParam(name = "afterId", defaultValue = "0") String afterId) {
        return execute(HttpStatus.OK, correlation -> accounts.list(principal, positive(company), identifier(afterId))
                .stream().map(mapper::toResponse).toList(), false);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> read(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String company, @PathVariable("userId") String userId) {
        return execute(HttpStatus.OK, correlation -> mapper.toResponse(accounts.read(principal, positive(company), positive(userId))), false);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String company, @Valid @RequestBody CreateUserRequest request) {
        return execute(HttpStatus.CREATED, correlation -> mapper.toResponse(accounts.create(principal, positive(company),
                request.username(), request.email(), request.userTypeCode(), request.reason(), correlation)), true);
    }

    @PostMapping(path = "/{userId}/activate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> activate(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String company, @PathVariable("userId") String userId,
            @Valid @RequestBody UserLifecycleRequest request) {
        return transition(principal, company, userId, "activate", request);
    }

    @PostMapping(path = "/{userId}/suspend", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> suspend(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String company, @PathVariable("userId") String userId,
            @Valid @RequestBody UserLifecycleRequest request) {
        return transition(principal, company, userId, "suspend", request);
    }

    @PostMapping(path = "/{userId}/reactivate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reactivate(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String company, @PathVariable("userId") String userId,
            @Valid @RequestBody UserLifecycleRequest request) {
        return transition(principal, company, userId, "reactivate", request);
    }

    @PostMapping(path = "/{userId}/disable", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> disable(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String company, @PathVariable("userId") String userId,
            @Valid @RequestBody UserLifecycleRequest request) {
        return transition(principal, company, userId, "disable", request);
    }

    @PostMapping(path = "/{userId}/credential/initialize", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> initializeCredential(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String company, @PathVariable("userId") String userId,
            @Valid @RequestBody LocalCredentialRequest request) {
        return execute(HttpStatus.NO_CONTENT, correlation -> {
            accounts.initializeCredential(principal, positive(company), positive(userId),
                    request.password(), request.reason(), correlation);
            return null;
        }, false);
    }

    @PostMapping(path = "/{userId}/credential/reset", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetCredential(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String company, @PathVariable("userId") String userId,
            @Valid @RequestBody LocalCredentialRequest request) {
        return execute(HttpStatus.NO_CONTENT, correlation -> {
            accounts.resetCredential(principal, positive(company), positive(userId),
                    request.password(), request.reason(), correlation);
            return null;
        }, false);
    }

    @PostMapping(path = "/{userId}/sessions/revoke", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> revokeAccountSessions(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "companyId", required = false) String company, @PathVariable("userId") String userId,
            @Valid @RequestBody UserLifecycleRequest request) {
        return execute(HttpStatus.NO_CONTENT, correlation -> {
            accounts.revokeAccountSessions(principal, positive(company), positive(userId), request.reason(), correlation);
            return null;
        }, false);
    }

    private ResponseEntity<?> transition(CurrentIdentity principal, String company, String userId, String operation,
            UserLifecycleRequest request) {
        return execute(HttpStatus.OK, correlation -> mapper.toResponse(accounts.transition(principal, positive(company),
                positive(userId), operation, request.reason(), correlation)), false);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> malformedRequest() {
        return problem(HttpStatus.BAD_REQUEST, UUID.randomUUID().toString());
    }

    private ResponseEntity<?> execute(HttpStatus success, Function<String, ?> action, boolean creation) {
        String correlation = UUID.randomUUID().toString();
        try {
            return ResponseEntity.status(success).header("Cache-Control", "no-store").body(action.apply(correlation));
        } catch (AuthenticationException unauthenticated) {
            return problem(HttpStatus.UNAUTHORIZED, correlation);
        } catch (AccessDeniedException forbidden) {
            return problem(HttpStatus.FORBIDDEN, correlation);
        } catch (AccountOperationRejected rejected) {
            return problem(HttpStatus.CONFLICT, correlation);
        } catch (DataIntegrityViolationException failure) {
            // Only a unique-key registration conflict is a domain rejection; evidence and other DB failures are unavailable.
            if (creation && duplicateKey(failure)) return problem(HttpStatus.CONFLICT, correlation);
            return problem(HttpStatus.SERVICE_UNAVAILABLE, correlation);
        } catch (IllegalArgumentException invalid) {
            return problem(HttpStatus.BAD_REQUEST, correlation);
        } catch (RuntimeException failure) {
            return problem(HttpStatus.SERVICE_UNAVAILABLE, correlation);
        }
    }

    private static boolean duplicateKey(Throwable failure) {
        for (Throwable cause = failure; cause != null && cause != cause.getCause(); cause = cause.getCause()) {
            if (cause instanceof java.sql.SQLException sql && sql.getErrorCode() == 1062) return true;
        }
        return false;
    }

    private static long identifier(String value) {
        if (value == null || !value.matches("[0-9]{1,19}")) throw new IllegalArgumentException("Invalid identifier");
        return Long.parseLong(value);
    }

    private static long positive(String value) {
        long id = identifier(value);
        if (id <= 0) throw new IllegalArgumentException("Invalid identifier");
        return id;
    }

    private static ResponseEntity<?> problem(HttpStatus status, String correlation) {
        var response = ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .header("Cache-Control", "no-store");
        if (status == HttpStatus.UNAUTHORIZED) response.header("WWW-Authenticate", "Bearer");
        if (status == HttpStatus.CONFLICT) {
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, "Account operation rejected");
            problem.setProperty("code", "ACCOUNT_OPERATION_REJECTED");
            problem.setProperty("correlationId", correlation);
            return response.body(problem);
        }
        return response.body(AuthenticationProblems.problem(status, correlation));
    }
}
