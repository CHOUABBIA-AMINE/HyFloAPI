/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuthenticationProblems
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Exception
 * @Module      : Platform / Exception
 * @Package     : dz.sh.hyflo.platform.exception
 *
 * @Description : Constructs safe authentication and access rejection responses.
 */

package dz.sh.hyflo.platform.exception;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public final class AuthenticationProblems {

    private AuthenticationProblems() {}

    public static ProblemDetail problem(HttpStatus status, String correlationId) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail(status));
        problem.setProperty("code", code(status));
        problem.setProperty("correlationId", correlationId);
        return problem;
    }

    public static void write(HttpServletResponse response, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/problem+json");
        response.setHeader("Cache-Control", "no-store");
        if (status == HttpStatus.UNAUTHORIZED) {
            response.setHeader("WWW-Authenticate", "Bearer");
        }
        // All interpolated values are fixed server literals or a generated UUID.
        response.getWriter().write("{\"type\":\"about:blank\",\"status\":" + status.value()
                + ",\"title\":\"" + status.getReasonPhrase() + "\",\"detail\":\"" + detail(status)
                + "\",\"code\":\"" + code(status) + "\",\"correlationId\":\"" + UUID.randomUUID() + "\"}");
    }

    private static String code(HttpStatus status) {
        return switch (status) {
            case BAD_REQUEST -> "INVALID_REQUEST";
            case UNAUTHORIZED -> "AUTHENTICATION_REQUIRED";
            case FORBIDDEN -> "ACCESS_DENIED";
            case TOO_MANY_REQUESTS -> "LOGIN_RATE_LIMITED";
            default -> "IDENTITY_UNAVAILABLE";
        };
    }

    private static String detail(HttpStatus status) {
        return switch (status) {
            case BAD_REQUEST -> "Invalid authentication request";
            case UNAUTHORIZED -> "Authentication failed or is required";
            case FORBIDDEN -> "Access denied";
            case TOO_MANY_REQUESTS -> "Too many login attempts";
            default -> "Identity temporarily unavailable";
        };
    }
}
