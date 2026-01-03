/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: JwtAuthenticationEntryPoint
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-06-2025
 *
 *	@Type		: Class
 *	@Layer		: Security
 *	@Package	: Configuration / Jwt
 *
 **/

package dz.sh.trc.hyflo.configuration.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT Authentication Entry Point
 * Handles unauthorized access attempts
 * 
 * FIXED: Removed ObjectMapper dependency - using PrintWriter instead
 * This eliminates Jackson dependency issues
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Use PrintWriter instead of ObjectMapper
        PrintWriter writer = response.getWriter();
        writer.println("{");
        writer.println("  \"status\": " + HttpServletResponse.SC_UNAUTHORIZED + ",");
        writer.println("  \"error\": \"Unauthorized\",");
        writer.println("  \"message\": \"" + escapeJson(authException.getMessage()) + "\",");
        writer.println("  \"path\": \"" + escapeJson(request.getServletPath()) + "\"");
        writer.println("}");
        writer.flush();
    }

    /**
     * Escape JSON special characters to prevent injection
     */
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}