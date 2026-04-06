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

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Canonical error-code registry for HyFloAPI.
 * Convention:  DOMAIN-NNN  (gaps intentional for future codes)
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ── SYS ──────────────────────────────────────────────────────────────────
    SYS_INTERNAL_ERROR          ("SYS-001", "An unexpected internal error occurred"),
    SYS_NOT_IMPLEMENTED         ("SYS-002", "Feature not yet implemented"),
    SYS_SERVICE_UNAVAILABLE     ("SYS-003", "Service temporarily unavailable"),

    // ── AUTH ─────────────────────────────────────────────────────────────────
    AUTH_INVALID_CREDENTIALS    ("AUTH-001", "Invalid username or password"),
    AUTH_TOKEN_INVALID          ("AUTH-002", "JWT token is invalid or malformed"),
    AUTH_TOKEN_EXPIRED          ("AUTH-003", "JWT token has expired"),
    AUTH_TOKEN_MISSING          ("AUTH-004", "Authorization token is missing"),
    AUTH_USER_NOT_FOUND         ("AUTH-005", "Authenticated user not found"),
    AUTH_USERNAME_ALREADY_EXISTS("AUTH-006", "Username already in use"),
    AUTH_EMAIL_ALREADY_EXISTS   ("AUTH-007", "Email address already in use"),
    AUTH_REFRESH_TOKEN_INVALID  ("AUTH-008", "Refresh token is invalid or not found"),
    AUTH_ACCOUNT_DISABLED       ("AUTH-009", "User account is disabled"),

    // ── RBAC ─────────────────────────────────────────────────────────────────
    RBAC_ACCESS_DENIED          ("RBAC-001", "Access denied: insufficient permissions"),
    RBAC_ROLE_NOT_FOUND         ("RBAC-002", "Role not found"),
    RBAC_PERMISSION_NOT_FOUND   ("RBAC-003", "Permission not found"),
    RBAC_GROUP_NOT_FOUND        ("RBAC-004", "Group not found"),
    RBAC_USER_NOT_FOUND         ("RBAC-005", "User not found in RBAC context"),
    RBAC_ROLE_ALREADY_EXISTS    ("RBAC-006", "Role already exists"),

    // ── BUS ──────────────────────────────────────────────────────────────────
    BUS_RESOURCE_NOT_FOUND      ("BUS-001", "Requested resource not found"),
    BUS_VALIDATION_FAILED       ("BUS-002", "Business validation failed"),
    BUS_CONSULTATION_ERROR      ("BUS-003", "Consultation operation failed"),
    BUS_DUPLICATE_RESOURCE      ("BUS-004", "Resource already exists"),
    BUS_STATE_TRANSITION_INVALID("BUS-005", "Invalid state transition"),

    // ── VAL ──────────────────────────────────────────────────────────────────
    VAL_INVALID_INPUT           ("VAL-001", "Input value is invalid"),
    VAL_MISSING_FIELD           ("VAL-002", "Required field is missing"),
    VAL_CONSTRAINT_VIOLATION    ("VAL-003", "Constraint violation"),
    VAL_TYPE_MISMATCH           ("VAL-004", "Type mismatch for request parameter"),
    VAL_METHOD_NOT_ALLOWED      ("VAL-005", "HTTP method not allowed"),
    VAL_MEDIA_TYPE_UNSUPPORTED  ("VAL-006", "Media type not supported"),

    // ── INF ──────────────────────────────────────────────────────────────────
    INF_DATABASE_ERROR          ("INF-001", "Database operation failed"),
    INF_DATA_INTEGRITY_VIOLATION("INF-002", "Data integrity constraint violated"),
    INF_EXTERNAL_SERVICE_ERROR  ("INF-003", "External service call failed"),
    INF_OPTIMISTIC_LOCK_FAILURE ("INF-004", "Concurrent modification conflict"),

    // ── RES ──────────────────────────────────────────────────────────────────
    RES_RATE_LIMIT_EXCEEDED     ("RES-001", "Rate limit exceeded"),
    RES_REQUEST_TIMEOUT         ("RES-002", "Request processing timed out"),

    // ── AUDIT ────────────────────────────────────────────────────────────────
    AUDIT_EVENT_NOT_FOUND       ("AUDIT-001", "Audit event not found"),
    AUDIT_PERSISTENCE_ERROR     ("AUDIT-002", "Failed to persist audit event"),

    // ── FLOW ─────────────────────────────────────────────────────────────────
    FLOW_WORKFLOW_ERROR         ("FLOW-001", "Workflow execution error"),
    FLOW_STATE_INVALID          ("FLOW-002", "Workflow is in an invalid state"),

    // ── FILE ─────────────────────────────────────────────────────────────────
    FILE_OPERATION_FAILED       ("FILE-001", "File operation failed"),
    FILE_NOT_FOUND              ("FILE-002", "File not found"),
    FILE_SIZE_EXCEEDED          ("FILE-003", "Uploaded file exceeds maximum size"),
    FILE_TYPE_NOT_ALLOWED       ("FILE-004", "File type not allowed");

    private final String code;
    private final String defaultMessage;
}