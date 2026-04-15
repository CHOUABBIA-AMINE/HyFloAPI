package dz.sh.trc.hyflo.platform.ai.memory;

import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;

/**
 * Thrown by {@link SessionIsolationEnforcer} when a session access
 * attempt violates isolation rules.
 *
 * <p>Extends {@link AgentExecutionException} with {@code isTransient=false}:
 * session isolation violations are deterministic security decisions,
 * not infrastructure failures. Retrying will produce the same result.</p>
 *
 * <h3>Violation types:</h3>
 * <ul>
 *   <li>{@link ViolationType#SESSION_NOT_FOUND} — the session ID does not
 *       exist in the registry (may have been evicted or never created).</li>
 *   <li>{@link ViolationType#SESSION_EXPIRED} — the session existed but
 *       has exceeded its TTL.</li>
 *   <li>{@link ViolationType#OWNERSHIP_MISMATCH} — the requesting userId
 *       does not match the session's registered owner.</li>
 *   <li>{@link ViolationType#NULL_SESSION_ID} — the caller passed a null
 *       or blank session ID (programming error in the call site).</li>
 * </ul>
 *
 * <h3>Fail-closed contract:</h3>
 * <p>Every violation type produces this exception. There is no "soft" or
 * "warning" mode for session isolation. Either the access is permitted or
 * this exception is thrown.</p>
 */
public class SessionIsolationException extends AgentExecutionException {

    /**
     * The reason the isolation check failed.
     */
    public enum ViolationType {
        SESSION_NOT_FOUND,
        SESSION_EXPIRED,
        OWNERSHIP_MISMATCH,
        NULL_SESSION_ID
    }

    private final ViolationType violationType;
    private final String        sessionId;
    private final String        requestingUserId;

    public SessionIsolationException(ViolationType violationType,
                                      String sessionId,
                                      String requestingUserId,
                                      String correlationId) {
        super(buildMessage(violationType, sessionId, requestingUserId),
              correlationId);   // non-transient by default in this constructor
        this.violationType    = violationType;
        this.sessionId        = sessionId;
        this.requestingUserId = requestingUserId;
    }

    public ViolationType getViolationType()    { return violationType; }
    public String        getSessionId()        { return sessionId; }
    public String        getRequestingUserId() { return requestingUserId; }

    private static String buildMessage(ViolationType type,
                                        String sessionId,
                                        String userId) {
        return switch (type) {
            case SESSION_NOT_FOUND  ->
                    "Session '" + sessionId + "' not found or expired for userId='" + userId + "'.";
            case SESSION_EXPIRED    ->
                    "Session '" + sessionId + "' has expired.";
            case OWNERSHIP_MISMATCH ->
                    "Session '" + sessionId + "' is not owned by userId='" + userId + "'.";
            case NULL_SESSION_ID    ->
                    "Session ID is null or blank (userId='" + userId + "').";
        };
    }
}