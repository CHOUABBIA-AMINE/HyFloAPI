/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ApiException
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Exception
 *	@Package	: Exception
 *
 **/

package dz.sh.trc.hyflo.exception.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Canonical API error envelope.
 *
 * {
 *   "timestamp"     : "2026-04-06T14:00:00Z",
 *   "correlationId" : "d3b07384-...",
 *   "errorCode"     : "AUTH-001",
 *   "message"       : "Invalid username or password",
 *   "path"          : "/auth/login",
 *   "details"       : { "fieldErrors": { "password": "must not be blank" } }
 * }
 *
 * NOTE: timestamp is Instant (UTC) — not LocalDateTime (was timezone-ambiguous).
 * NOTE: details is suppressed from JSON when null/empty.
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final Instant             timestamp;
    private final String              correlationId;
    private final String              errorCode;
    private final String              message;
    private final String              path;
    private final Map<String, Object> details;

    /** Build a full response from any ApiException. */
    public static ErrorResponse of(ApiException ex, String path) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .correlationId(UUID.randomUUID().toString())
                .errorCode(ex.getCode())
                .message(ex.getMessage())
                .path(path)
                .build();
    }

    /** Fallback for truly unexpected errors — never leaks internal detail. */
    public static ErrorResponse unknown(String path) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .correlationId(UUID.randomUUID().toString())
                .errorCode(ErrorCode.SYS_INTERNAL_ERROR.getCode())
                .message(ErrorCode.SYS_INTERNAL_ERROR.getDefaultMessage())
                .path(path)
                .build();
    }
}