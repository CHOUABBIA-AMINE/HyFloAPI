package dz.sh.trc.hyflo.flow.core.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;

/**
 * Workflow command DTO for validating (approving/rejecting) flow readings.
 * 
 * NOTE: Intentionally does NOT extend GenericDTO because:
 * - Represents a validation action, not a persistent entity
 * - Triggers state machine transition in FlowReadingWorkflowService
 * 
 * PATTERN: Dual representation (ID + nested DTO)
 * - readingId: Used by backend to fetch and lock the reading
 * - reading: Optional, populated by backend for confirmation display
 * - employeeId: Used by backend for audit trail
 * - employee: Populated by backend for validator info display
 * 
 * @see dz.sh.trc.hyflo.flow.core.service.FlowReadingWorkflowService#validateReading
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Request to approve or reject a submitted flow reading")
public class ReadingValidationRequestDTO {
    
    // ========== READING CONTEXT (ID + nested DTO) ==========
    
    @NotNull(message = "Reading ID is required")
    @Schema(description = "Flow reading ID to validate", example = "789", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long readingId;
    
    @Schema(description = "Flow reading details (for confirmation/display purposes)")
    private FlowReadingDTO reading;
    
    // ========== VALIDATION ACTION ==========
    
    @NotNull(message = "Validation action is required")
    @Schema(description = "Validation action", example = "APPROVE", requiredMode = Schema.RequiredMode.REQUIRED, 
            allowableValues = {"APPROVE", "REJECT"})
    private String action; // "APPROVE" or "REJECT"
    
    // ========== VALIDATOR CONTEXT (ID + nested DTO) ==========
    
    @NotNull(message = "Validator employee ID is required")
    @Schema(description = "Employee performing validation", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long employeeId;
    
    @Schema(description = "Validator details (name, role)")
    private EmployeeDTO employee;
    
    // ========== COMMENTS ==========
    
    @Size(max = 500, message = "Comments must not exceed 500 characters")
    @Schema(description = "Validation comments (required for REJECT)", 
            example = "Pressure reading appears abnormal, please verify sensor",
            maxLength = 500)
    private String comments; // Required if action = REJECT
}
