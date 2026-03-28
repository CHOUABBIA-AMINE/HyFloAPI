/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ValidationStatusHelper
 * 	@CreatedOn	: 01-28-2026
 * 	@UpdatedOn	: 02-03-2026
 * 	@UpdatedOn	: 03-28-2026 — refactor: merged from flow.workflow.helper into flow.workflow.util
 *
 * 	@Type		: Helper
 * 	@Layer		: Util
 * 	@Package	: Flow / Workflow / Util
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.util;

import dz.sh.trc.hyflo.flow.core.model.FlowReading;

/**
 * Helper to work with ValidationStatus-based workflow.
 * FIX #2: Enhanced with immutability checks for APPROVED readings.
 */
public class ValidationStatusHelper {

    public static final String NOT_RECORDED = "NOT_RECORDED";
    public static final String DRAFT = "DRAFT";
    public static final String SUBMITTED = "SUBMITTED";
    public static final String APPROVED = "APPROVED";
    public static final String REJECTED = "REJECTED";

    public static String getStatusCode(FlowReading reading) {
        if (reading == null) {
            return NOT_RECORDED;
        }
        if (reading.getValidationStatus() == null) {
            return DRAFT;
        }
        return reading.getValidationStatus().getCode();
    }

    /**
     * FIX #2: APPROVED and SUBMITTED are immutable.
     * Only DRAFT, REJECTED, or NOT_RECORDED can be modified.
     */
    public static boolean isEditable(FlowReading reading) {
        String code = getStatusCode(reading);
        return NOT_RECORDED.equals(code) ||
               DRAFT.equals(code) ||
               REJECTED.equals(code);
    }

    public static boolean canSubmit(FlowReading reading) {
        String code = getStatusCode(reading);
        return DRAFT.equals(code) || REJECTED.equals(code);
    }

    public static boolean canValidate(FlowReading reading) {
        String code = getStatusCode(reading);
        return SUBMITTED.equals(code);
    }

    public static boolean canTransition(String fromCode, String toCode, String userRole) {
        if (fromCode == null) {
            fromCode = NOT_RECORDED;
        }
        return switch (fromCode) {
            case NOT_RECORDED, DRAFT ->
                SUBMITTED.equals(toCode) && "OPERATOR".equals(userRole);
            case SUBMITTED ->
                (APPROVED.equals(toCode) || REJECTED.equals(toCode)) &&
                ("SUPERVISOR".equals(userRole) || "MANAGER".equals(userRole));
            case REJECTED ->
                SUBMITTED.equals(toCode) && "OPERATOR".equals(userRole);
            case APPROVED ->
                false;
            default -> false;
        };
    }

    public static String getDisplayName(String code) {
        return switch (code) {
            case NOT_RECORDED -> "Not Recorded";
            case DRAFT -> "Draft";
            case SUBMITTED -> "Submitted";
            case APPROVED -> "Approved";
            case REJECTED -> "Rejected";
            default -> code;
        };
    }

    public static boolean isComplete(String code) {
        return SUBMITTED.equals(code) || APPROVED.equals(code);
    }

    /** FIX #2: APPROVED is the only terminal state — no transitions allowed. */
    public static boolean isTerminal(String code) {
        return APPROVED.equals(code);
    }

    public static boolean isImmutable(String code) {
        return APPROVED.equals(code) || SUBMITTED.equals(code);
    }
}
