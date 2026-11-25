/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentitySetupController
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Controller
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.controller
 *
 * @Description : Exposes only the secured first-use installation endpoints.
 */

package dz.sh.hyflo.modules.foundation.identity.api.controller;

import java.sql.SQLException;
import java.util.UUID;

import dz.sh.hyflo.modules.foundation.identity.api.request.IdentitySetupRequest;
import dz.sh.hyflo.modules.foundation.identity.api.response.IdentitySetupStatusResponse;
import dz.sh.hyflo.modules.foundation.identity.application.service.IdentitySetupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/setup")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class IdentitySetupController {

    private final IdentitySetupService setup;

    public IdentitySetupController(IdentitySetupService setup) {
        this.setup = setup;
    }

    @Operation(summary = "Check first-use setup availability")
    @ApiResponse(responseCode = "200", description = "Setup availability",
            content = @Content(schema = @Schema(implementation = IdentitySetupStatusResponse.class)))
    @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> status() {
        try {
            return ResponseEntity.ok().header("Cache-Control", "no-store")
                    .body(new IdentitySetupStatusResponse(setup.available()));
        } catch (RuntimeException unavailable) {
            return problem(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Operation(summary = "Initialize first company and administrator",
            parameters = @Parameter(name = "X-Hyflo-Setup-Secret", in = ParameterIn.HEADER, required = true,
                    description = "Deployment-configured one-time installation authority"))
    @ApiResponse(responseCode = "204", description = "Initialized", content = @Content)
    @PostMapping(path = "/initialize", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> initialize(@Valid @RequestBody IdentitySetupRequest request, HttpServletRequest http) {
        var headers = http.getHeaders("X-Hyflo-Setup-Secret");
        if (!headers.hasMoreElements()) return problem(HttpStatus.FORBIDDEN);
        String secret = headers.nextElement();
        if (headers.hasMoreElements()) return problem(HttpStatus.FORBIDDEN);
        try {
            setup.initialize(secret, request.companyCode(), request.companyNameAr(), request.companyNameFr(),
                    request.companyNameEn(), request.username(), request.email(), request.password(),
                    request.initialPlatformRoleCode(), request.initialPlatformRoleName());
            return ResponseEntity.noContent().header("Cache-Control", "no-store").build();
        } catch (AccessDeniedException denied) {
            return problem(HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException invalid) {
            return problem(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException unavailable) {
            return problem(HttpStatus.CONFLICT);
        } catch (RuntimeException failure) {
            return problem(isConcurrentConflict(failure) ? HttpStatus.CONFLICT : HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private static boolean isConcurrentConflict(Throwable failure) {
        for (Throwable cause = failure; cause != null; cause = cause.getCause()) {
            if (cause instanceof SQLException sql
                    && (sql.getErrorCode() == 1062 || sql.getErrorCode() == 1213 || sql.getErrorCode() == 1205)) {
                return true;
            }
        }
        return false;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> malformedRequest() {
        return problem(HttpStatus.BAD_REQUEST);
    }

    private static ResponseEntity<?> problem(HttpStatus status) {
        String detail = switch (status) {
            case BAD_REQUEST -> "Invalid installation request";
            case FORBIDDEN -> "Installation access denied";
            case CONFLICT -> "Installation unavailable";
            default -> "Installation temporarily unavailable";
        };
        String code = switch (status) {
            case BAD_REQUEST -> "INVALID_REQUEST";
            case FORBIDDEN -> "ACCESS_DENIED";
            case CONFLICT -> "SETUP_UNAVAILABLE";
            default -> "SETUP_FAILURE";
        };
        var body = ProblemDetail.forStatusAndDetail(status, detail);
        body.setProperty("code", code);
        body.setProperty("correlationId", UUID.randomUUID().toString());
        return ResponseEntity.status(status).header("Cache-Control", "no-store")
                .contentType(MediaType.APPLICATION_PROBLEM_JSON).body(body);
    }
}
