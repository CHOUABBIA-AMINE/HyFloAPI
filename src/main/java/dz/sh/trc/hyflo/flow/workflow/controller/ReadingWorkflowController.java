/**
 *
 * 	@Author		: MEDJERAB Abir
 * 
 * 	@Name		: FlowWorkflowController
 * 	@CreatedOn	: 01-28-2026
 * 	@UpdatedOn	: 02-03-2026
 * 	@UpdatedOn	: 02-10-2026 - Renamed from FlowMonitoringController to FlowWorkflowController
 * 	@UpdatedOn	: 02-10-2026 - Updated to match FlowReadingWorkflowService methods (SRP refactoring)
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
@RestController
@RequestMapping("/flow/workflow/reading")
@Tag(name = "Flow Workflow", description = "Flow reading workflow state transition operations")
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
    @PostMapping("/readings/{id}/validate")
    @Operation(
        summary = "Validate a flow reading",
        description = "Approves a submitted reading, transitions status to VALIDATED, and records validator information"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reading validated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FlowReadingDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reading or employee not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content
        )
    })
    public ResponseEntity<FlowReadingDTO> validateReading(
        @Parameter(description = "Reading ID to validate", required = true)
        @PathVariable Long id,
        
        @Parameter(description = "Employee ID of the validator", required = true)
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
    @PostMapping("/readings/{id}/reject")
    @Operation(
        summary = "Reject a flow reading",
        description = "Rejects a submitted reading with a reason, transitions status to REJECTED, and notifies recorder"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reading rejected successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FlowReadingDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reading or employee not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters or missing rejection reason",
            content = @Content
        )
    })
    public ResponseEntity<FlowReadingDTO> rejectReading(
        @Parameter(description = "Reading ID to reject", required = true)
        @PathVariable Long id,
        
        @Parameter(description = "Employee ID of the rejector", required = true)
        @RequestParam @NotNull(message = "Rejector ID is required") Long rejectedById,
        
        @Parameter(description = "Reason for rejection", required = true)
        @RequestParam @NotBlank(message = "Rejection reason is required") String rejectionReason
    ) {
        log.info("REST: Rejecting reading {} by employee {} with reason: {}", 
                 id, rejectedById, rejectionReason);
        FlowReadingDTO result = readingWorkflowService.reject(id, rejectedById, rejectionReason);
        return ResponseEntity.ok(result);
    }

    // ========== MIGRATION NOTES ==========

    /**
     * ⚠️ ENDPOINT CHANGES
     * 
     * The following endpoints were removed in this refactoring:
     * 
     * 1. POST /slot-coverage
     *    → MOVED to intelligence module (FlowIntelligenceController)
     *    → Slot coverage is an analytics feature, not a workflow operation
     * 
     * 2. POST /readings/submit
     *    → USE FlowReadingController.create() instead
     *    → CRUD operations belong in FlowReadingController
     * 
     * 3. POST /readings/validate (with ReadingValidationRequestDTO)
     *    → REPLACED with two specific endpoints:
     *      - POST /readings/{id}/validate
     *      - POST /readings/{id}/reject
     *    → Clearer RESTful semantics
     * 
     * New endpoint structure:
     * - POST /flow/core/workflow/readings/{id}/validate?validatedById={id}
     * - POST /flow/core/workflow/readings/{id}/reject?rejectedById={id}&rejectionReason={reason}
     */
}
