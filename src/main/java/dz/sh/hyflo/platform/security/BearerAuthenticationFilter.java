/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : BearerAuthenticationFilter
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Security
 * @Module      : Platform / Security
 * @Package     : dz.sh.hyflo.platform.security
 *
 * @Description : Enforces signed access tokens against live Identity sessions.
 */

package dz.sh.hyflo.platform.security;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService;
import dz.sh.hyflo.platform.exception.AuthenticationProblems;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class BearerAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService tokens;
    private final SessionAuthenticationService sessions;

    public BearerAuthenticationFilter(JwtTokenService tokens, SessionAuthenticationService sessions) {
        this.tokens = tokens;
        this.sessions = sessions;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            chain.doFilter(request, response);
            return;
        }
        if (!authorization.regionMatches(true, 0, "Bearer ", 0, 7)) {
            AuthenticationProblems.write(response, HttpStatus.UNAUTHORIZED);
            return;
        }
        try {
            Instant now = Instant.now();
            var token = tokens.verify(authorization.substring(7), now);
            var identity = sessions.resolve(token.userId(), token.sessionId(), now).orElse(null);
            if (identity == null) {
                AuthenticationProblems.write(response, HttpStatus.UNAUTHORIZED);
                return;
            }
            var context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(identity, null, List.of()));
            SecurityContextHolder.setContext(context);
        } catch (IllegalArgumentException invalid) {
            SecurityContextHolder.clearContext();
            AuthenticationProblems.write(response, HttpStatus.UNAUTHORIZED);
            return;
        } catch (RuntimeException unavailable) {
            SecurityContextHolder.clearContext();
            AuthenticationProblems.write(response, HttpStatus.SERVICE_UNAVAILABLE);
            return;
        }
        chain.doFilter(request, response);
    }
}
