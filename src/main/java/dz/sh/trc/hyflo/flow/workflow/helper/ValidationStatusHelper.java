/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ValidationStatusHelper
 * 	@CreatedOn	: 01-28-2026
 * 	@UpdatedOn	: 02-03-2026
 *
 * 	@Type		: Helper
 * 	@Layer		: Helper
 * 	@Package	: Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.helper;

import dz.sh.trc.hyflo.flow.core.model.FlowReading;

/**
 * Helper to work with ValidationStatus-based workflow
 * FIX #2: Enhanced with immutability checks for APPROVED readings
 */
public class ValidationStatusHelper {
    
    // Standard status codes
    public static final String NOT_RECORDED = "NOT_RECORDED";
    public static final String DRAFT = "DRAFT";
    public static final String SUBMITTED = "SUBMITTED";
    public static final String APPROVED = "APPROVED";
    public static final String REJECTED = "REJECTED";
    
    /**
     * Get status code from reading (handles null reading)
     */
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
     * FIX #2: Check if reading is editable (APPROVED and SUBMITTED are immutable)
     * Only DRAFT, REJECTED, or NOT_RECORDED can be modified
     */
    public static boolean isEditable(FlowReading reading) {
        String code = getStatusCode(reading);
        return NOT_RECORDED.equals(code) || 
               DRAFT.equals(code) || 
               REJECTED.equals(code);
    }
    
    /**
     * Check if reading can be submitted
     */
    public static boolean canSubmit(FlowReading reading) {
        String code = getStatusCode(reading);
        return DRAFT.equals(code) || REJECTED.equals(code);
    }
    
    /**
     * Check if reading can be validated
     */
    public static boolean canValidate(FlowReading reading) {
        String code = getStatusCode(reading);
        return SUBMITTED.equals(code);
    }
    
    /**
     * Check if transition is allowed
     */
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
                false; // FIX #2: Terminal state - no transitions allowed
            
            default -> false;
        };
    }
    
    /**
     * Get display name for status code
     */
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
    
    /**
     * Check if status is submitted or approved
     */
    public static boolean isComplete(String code) {
        return SUBMITTED.equals(code) || APPROVED.equals(code);
    }
    
    /**
     * FIX #2: Check if status is terminal (no more transitions allowed)
     */
    public static boolean isTerminal(String code) {
        return APPROVED.equals(code);
    }
    
    /**
     * Check if status is immutable
     */
    public static boolean isImmutable(String code) {
        return APPROVED.equals(code) || SUBMITTED.equals(code);
    }
}
