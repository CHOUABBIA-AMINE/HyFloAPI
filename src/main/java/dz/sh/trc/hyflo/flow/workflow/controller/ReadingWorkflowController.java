/**
 *
 * 	@Author		: MEDJERAB Abir
 * 
 * 	@Name		: ReadingWorkflowController
 * 	@CreatedOn	: 01-28-2026
 * 	@UpdatedOn	: 02-03-2026
 * 	@UpdatedOn	: 02-10-2026 - Renamed from FlowMonitoringController to FlowWorkflowController
 * 	@UpdatedOn	: 02-10-2026 - Updated to match ReadingWorkflowService methods (SRP refactoring)
 * 	@UpdatedOn	: 02-16-2026 - Enhanced with comprehensive OpenAPI documentation
 * 	@UpdatedOn	: 02-16-2026 - Fixed text blocks to string concatenation
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Workflow
 *
 * 	@Description: REST controller for flow reading workflow state transitions.
 * 	              Provides endpoints for validating and rejecting flow readings.
 *
 * 	@Refactoring: Updated to use validate() and reject() methods
 * 	              
 * 	              Previous endpoints removed:
 * 	              - POST /slot-coverage (moved to intelligence module)
 * 	              - POST /readings/submit (use FlowReadingController CRUD)
 * 	              - POST /readings/validate (replaced with specific endpoints)
 * 	              
 * 	              New RESTful endpoints:
 * 	              - POST /readings/{id}/validate
 * 	              - POST /readings/{id}/reject
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.workflow.service.ReadingWorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for flow reading workflow operations.
 * 
 * This controller handles workflow state transitions:
 * - Validate readings (SUBMITTED → VALIDATED)
 * - Reject readings (SUBMITTED → REJECTED)
 * 
 * For CRUD operations, see FlowReadingController.
 * For analytics/monitoring, see FlowMonitoringController (intelligence module).
 * 
 * Architecture:
 * - This controller: Workflow transitions
 * - FlowReadingController: CRUD operations
 * - FlowMonitoringController: Analytics queries
 */
@Tag(
    name = "Flow Reading Workflow",
    description = "Workflow state transition operations for flow readings. Handles validation and rejection processes with automatic notification publishing."
)
@SecurityRequirement(name = "bearer-auth")
@RestController
@RequestMapping("/flow/workflow/reading")
@RequiredArgsConstructor
@Slf4j
public class ReadingWorkflowController {
    
    private final ReadingWorkflowService readingWorkflowService;
    
    /**
     * Validate a flow reading
     * 
     * Transitions reading from SUBMITTED to VALIDATED status.
     * Records validator information and publishes validation event.
     * 
     * Workflow: SUBMITTED → VALIDATED
     * 
     * @param id Reading ID to validate
     * @param validatedById Employee ID of the validator
     * @return Updated reading DTO with VALIDATED status
     */
    @Operation(
        summary = "Validate a flow reading",
        description = "Approves a submitted reading and transitions its status to VALIDATED.\n\n" +
            "**Workflow Transition:** SUBMITTED → VALIDATED\n\n" +
            "**Process:**\n" +
            "- Updates reading validation status to VALIDATED\n" +
            "- Records validator employee information (validatedBy, validatedAt)\n" +
            "- Publishes ReadingValidatedEvent for notification system\n" +
            "- Sends notification to original recorder\n\n" +
            "**Business Rules:**\n" +
            "- Reading must exist in the system\n" +
            "- Validator employee must exist and have appropriate permissions\n" +
            "- VALIDATED status must be configured in validation_status table\n" +
            "- Only SUBMITTED readings can be validated\n\n" +
            "**Notifications:**\n" +
            "- Recorder receives notification of validation approval\n" +
            "- Event published via Spring ApplicationEventPublisher\n" +
            "- Integration with generic notification system\n\n" +
            "**Use Cases:**\n" +
            "- Operational supervisor approving field readings\n" +
            "- Quality control officer validating measurement data\n" +
            "- Automated validation after passing data integrity checks"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reading validated successfully. Returns updated reading with VALIDATED status and validator information.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FlowReadingDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters or business rule violation (e.g., reading already validated, invalid status transition)",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - insufficient permissions to validate readings",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reading not found with provided ID, or validator employee not found, or VALIDATED status not configured",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error during validation process or event publishing",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @PostMapping("/{id}/validate")
    @PreAuthorize("hasAuthority('READING:VALIDATE')")
    public ResponseEntity<FlowReadingDTO> validateReading(
        @Parameter(
            description = "Unique identifier of the reading to validate",
            required = true,
            example = "1"
        )
        @PathVariable Long id,
        
        @Parameter(
            description = "Employee ID of the validator performing the approval. Must correspond to an existing employee with validation permissions.",
            required = true,
            example = "42"
        )
        @RequestParam @NotNull(message = "Validator ID is required") Long validatedById
    ) {
        log.info("REST: Validating reading {} by employee {}", id, validatedById);
        FlowReadingDTO result = readingWorkflowService.validate(id, validatedById);
        return ResponseEntity.ok(result);
    }
    
    /**
     * Reject a flow reading
     * 
     * Transitions reading from SUBMITTED to REJECTED status.
     * Records rejector information, appends rejection reason to notes, 
     * and publishes rejection event.
     * 
     * Workflow: SUBMITTED → REJECTED
     * 
     * @param id Reading ID to reject
     * @param rejectedById Employee ID of the rejector
     * @param rejectionReason Reason for rejection (required)
     * @return Updated reading DTO with REJECTED status
     */
    @Operation(
        summary = "Reject a flow reading",
        description = "Rejects a submitted reading with a mandatory reason and transitions its status to REJECTED.\n\n" +
            "**Workflow Transition:** SUBMITTED → REJECTED\n\n" +
            "**Process:**\n" +
            "- Updates reading validation status to REJECTED\n" +
            "- Records rejector employee information (validatedBy, validatedAt)\n" +
            "- Appends rejection reason to reading notes for audit trail\n" +
            "- Publishes ReadingRejectedEvent for notification system\n" +
            "- Sends notification to original recorder\n\n" +
            "**Business Rules:**\n" +
            "- Reading must exist in the system\n" +
            "- Rejector employee must exist and have appropriate permissions\n" +
            "- Rejection reason is mandatory (minimum 3 characters)\n" +
            "- REJECTED status must be configured in validation_status table\n" +
            "- Only SUBMITTED readings can be rejected\n\n" +
            "**Rejection Reason:**\n" +
            "- Appended to reading notes with timestamp and rejector information\n" +
            "- Format: \"=== REJECTION === Rejected by: [Name] ([ID]) Date: [Timestamp] Reason: [Reason]\"\n" +
            "- Provides audit trail for quality control\n" +
            "- Helps recorder understand what needs correction\n\n" +
            "**Notifications:**\n" +
            "- Recorder receives notification with rejection reason\n" +
            "- Event published via Spring ApplicationEventPublisher\n" +
            "- Integration with generic notification system\n\n" +
            "**Use Cases:**\n" +
            "- Readings with suspicious values requiring verification\n" +
            "- Data quality issues detected during review\n" +
            "- Incomplete or missing measurement information\n" +
            "- Equipment malfunction suspected\n" +
            "- Values outside acceptable operational ranges\n\n" +
            "**Future Enhancement:**\n" +
            "- Rejected readings can be resubmitted after correction\n" +
            "- Resubmission workflow will be added in future version"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reading rejected successfully. Returns updated reading with REJECTED status, rejector information, and rejection reason in notes.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FlowReadingDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters (missing rejection reason, invalid status transition, or business rule violation)",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - insufficient permissions to reject readings",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reading not found with provided ID, or rejector employee not found, or REJECTED status not configured",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error during rejection process or event publishing",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('READING:VALIDATE')")
    public ResponseEntity<FlowReadingDTO> rejectReading(
        @Parameter(
            description = "Unique identifier of the reading to reject",
            required = true,
            example = "1"
        )
        @PathVariable Long id,
        
        @Parameter(
            description = "Employee ID of the rejector performing the rejection. Must correspond to an existing employee with validation permissions.",
            required = true,
            example = "42"
        )
        @RequestParam @NotNull(message = "Rejector ID is required") Long rejectedById,
        
        @Parameter(
            description = "Detailed reason for rejecting the reading. Must explain what is wrong with the data so the recorder can make corrections. Minimum 3 characters.",
            required = true,
            example = "Flow rate value (2500 m³/h) exceeds maximum pipeline capacity (2000 m³/h). Please verify measurement equipment calibration."
        )
        @RequestParam @NotBlank(message = "Rejection reason is required") String rejectionReason
    ) {
        log.info("REST: Rejecting reading {} by employee {} with reason: {}", 
                 id, rejectedById, rejectionReason);
        FlowReadingDTO result = readingWorkflowService.reject(id, rejectedById, rejectionReason);
        return ResponseEntity.ok(result);
    }

    // ========== MIGRATION NOTES ==========

    /**
     * ⚠️ ENDPOINT CHANGES (SRP Refactoring)
     * 
     * The following endpoints were removed in this refactoring:
     * 
     * 1. POST /slot-coverage
     *    → MOVED to intelligence module (FlowIntelligenceController)
     *    → Rationale: Slot coverage is an analytics feature, not a workflow operation
     *    → New location: /flow/intelligence/slot-coverage
     * 
     * 2. POST /readings/submit
     *    → USE FlowReadingController.create() instead
     *    → Rationale: CRUD operations belong in FlowReadingController
     *    → New location: POST /flow/core/readings
     * 
     * 3. POST /readings/validate (with ReadingValidationRequestDTO)
     *    → REPLACED with two specific endpoints:
     *      - POST /readings/{id}/validate
     *      - POST /readings/{id}/reject
     *    → Rationale: Clearer RESTful semantics and separation of approve/reject actions
     * 
     * **New endpoint structure:**
     * - POST /flow/workflow/reading/{id}/validate?validatedById={id}
     * - POST /flow/workflow/reading/{id}/reject?rejectedById={id}&rejectionReason={reason}
     * 
     * **Benefits of SRP refactoring:**
     * - Clear separation of concerns (CRUD vs Workflow vs Analytics)
     * - Easier testing with focused service mocks
     * - Better maintainability with isolated business logic
     * - Future-proof for additional workflow states (PENDING, REVIEWING)
     * - Improved API discoverability and documentation
     */
}
