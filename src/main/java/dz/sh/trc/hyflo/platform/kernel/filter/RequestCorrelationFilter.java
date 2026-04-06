/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : RequestCorrelationFilter
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Configuration / Filter
 *  @Package    : Configuration
 *
 *  H8 — MDC correlation filter.
 *  Injects X-Request-ID and reading-id into SLF4J MDC on every request.
 *  Clears MDC in finally block to prevent context leakage.
 *
 **/

package dz.sh.trc.hyflo.platform.kernel.filter;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Servlet filter that injects correlation identifiers into SLF4J MDC.
 *
 * <h3>What it injects:</h3>
 * <ul>
 *   <li>{@code requestId}  — taken from {@code X-Request-ID} header if present;
 *       otherwise a random UUID is generated and added to the response header.</li>
 *   <li>{@code readingId}  — extracted from URI path segments matching the pattern
 *       {@code /readings/{id}} or {@code /reading/{id}}. Present only on reading-specific
 *       endpoints.</li>
 * </ul>
 *
 * <h3>Why this matters:</h3>
 * In a multi-threaded Spring Boot deployment (or any future microservice split),
 * log lines from approval, event publishing, and async derived generation all land
 * in different threads. Having a shared {@code requestId} and {@code readingId}
 * in each log line enables log aggregation tools (ELK, Loki, Datadog) to reconstruct
 * the full approval/rejection timeline without custom parsing.
 *
 * <h3>Lifecycle guarantee:</h3>
 * MDC is always cleared in the {@code finally} block of
 * {@link #doFilterInternal}, preventing MDC key leakage to subsequent requests
 * reusing the same thread from the servlet container thread pool.
 *
 * <h3>Order:</h3>
 * {@code @Order(1)} ensures this filter runs before Spring Security filters,
 * meaning {@code requestId} is available in security audit logs too.
 */
@Slf4j
@Component
@Order(1)
public class RequestCorrelationFilter extends OncePerRequestFilter {

    private static final String MDC_REQUEST_ID  = "requestId";
    private static final String MDC_READING_ID  = "readingId";
    private static final String HEADER_REQUEST_ID = "X-Request-ID";

    /**
     * Matches URI path segments of the form {@code /readings/123} or {@code /reading/123}.
     * Group 1 captures the numeric ID.
     * Examples matched:
     * <ul>
     *   <li>{@code /api/v2/flow/readings/42}</li>
     *   <li>{@code /api/v2/flow/readings/42/approve}</li>
     *   <li>{@code /flow/core/reading/42}</li>
     * </ul>
     */
    private static final Pattern READING_ID_PATTERN =
            Pattern.compile("/readings?/(\\d+)", Pattern.CASE_INSENSITIVE);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestId = resolveRequestId(request, response);
        String readingId = extractReadingId(request.getRequestURI());

        try {
            MDC.put(MDC_REQUEST_ID, requestId);
            if (readingId != null) {
                MDC.put(MDC_READING_ID, readingId);
            }

            filterChain.doFilter(request, response);

        } finally {
            // ALWAYS clear MDC to prevent context leakage across thread pool reuse
            MDC.remove(MDC_REQUEST_ID);
            MDC.remove(MDC_READING_ID);
        }
    }

    // -----------------------------------------------------------------------
    //  Helpers
    // -----------------------------------------------------------------------

    /**
     * Returns the incoming {@code X-Request-ID} header value if present and non-blank;
     * otherwise generates a new UUID and echoes it in the response header so the
     * caller can correlate their own logs.
     */
    private String resolveRequestId(HttpServletRequest request, HttpServletResponse response) {
        String incoming = request.getHeader(HEADER_REQUEST_ID);
        if (incoming != null && !incoming.isBlank()) {
            return incoming;
        }
        String generated = UUID.randomUUID().toString();
        response.setHeader(HEADER_REQUEST_ID, generated);
        return generated;
    }

    /**
     * Extracts a reading ID from the request URI using {@link #READING_ID_PATTERN}.
     *
     * @param uri the raw request URI (e.g., {@code /api/v2/flow/readings/42/approve})
     * @return the reading ID as a string, or {@code null} if not a reading-specific path
     */
    private String extractReadingId(String uri) {
        if (uri == null) return null;
        Matcher matcher = READING_ID_PATTERN.matcher(uri);
        return matcher.find() ? matcher.group(1) : null;
    }
}
