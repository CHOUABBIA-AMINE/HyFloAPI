/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ReadingValidationRequestDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 03-26-2026 — replace FlowReadingDTO (deleted) with FlowReadingReadDTO
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Workflow command DTO for validating (approving/rejecting) flow readings.
 *
 * The {@code reading} field is informational only — populated by the backend
 * for confirmation display purposes. Uses FlowReadingReadDTO (v2).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Request to approve or reject a submitted flow reading")
public class ReadingValidationRequestDTO {

    @NotNull(message = "Reading ID is required")
    @Schema(description = "Flow reading ID to validate", example = "789",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long readingId;

    @Schema(description = "Flow reading details (for confirmation/display purposes)")
    private FlowReadingReadDTO reading;

    @NotNull(message = "Validation action is required")
    @Schema(description = "Validation action", example = "APPROVE",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"APPROVE", "REJECT"})
    private String action;

    @NotNull(message = "Validator employee ID is required")
    @Schema(description = "Employee performing validation", example = "15",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long employeeId;

    @Schema(description = "Validator details (name, role) — populated by backend")
    private EmployeeDTO employee;

    @Size(max = 500, message = "Comments must not exceed 500 characters")
    @Schema(description = "Validation comments (required for REJECT)",
            example = "Pressure reading appears abnormal, please verify sensor",
            maxLength = 500)
    private String comments;
}
