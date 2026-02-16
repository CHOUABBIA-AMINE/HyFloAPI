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
 * 	@UpdatedOn	: 02-16-2026 - Simplified to single-line descriptions
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Workflow
 *
 * 	@Description: REST controller for flow reading workflow state transitions.
 * 	              Provides endpoints for validating and rejecting flow readings.
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

@Tag(
    name = "Flow Reading Workflow",
    description = "Workflow state transition operations for flow readings"
)
@SecurityRequirement(name = "bearer-auth")
@RestController
@RequestMapping("/flow/workflow/reading")
@RequiredArgsConstructor
@Slf4j
public class ReadingWorkflowController {
    
    private final ReadingWorkflowService readingWorkflowService;
    
    @Operation(
        summary = "Validate a flow reading",
        description = "Approves a submitted reading and transitions its status from SUBMITTED to VALIDATED. Records validator information and publishes validation event for notifications."
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
            responseCode = "400",
            description = "Invalid request parameters or business rule violation",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - insufficient permissions",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reading or validator not found",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @PostMapping("/{id}/validate")
    @PreAuthorize("hasAuthority('READING:VALIDATE')")
    public ResponseEntity<FlowReadingDTO> validateReading(
        @Parameter(
            description = "Reading ID to validate",
            required = true,
            example = "1"
        )
        @PathVariable Long id,
        
        @Parameter(
            description = "Employee ID of the validator",
            required = true,
            example = "42"
        )
        @RequestParam @NotNull(message = "Validator ID is required") Long validatedById
    ) {
        log.info("REST: Validating reading {} by employee {}", id, validatedById);
        FlowReadingDTO result = readingWorkflowService.validate(id, validatedById);
        return ResponseEntity.ok(result);
    }
    
    @Operation(
        summary = "Reject a flow reading",
        description = "Rejects a submitted reading with a mandatory reason and transitions its status from SUBMITTED to REJECTED. Records rejector information, appends rejection reason to notes, and publishes rejection event for notifications."
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
            responseCode = "400",
            description = "Invalid request parameters or missing rejection reason",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - insufficient permissions",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reading or rejector not found",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('READING:VALIDATE')")
    public ResponseEntity<FlowReadingDTO> rejectReading(
        @Parameter(
            description = "Reading ID to reject",
            required = true,
            example = "1"
        )
        @PathVariable Long id,
        
        @Parameter(
            description = "Employee ID of the rejector",
            required = true,
            example = "42"
        )
        @RequestParam @NotNull(message = "Rejector ID is required") Long rejectedById,
        
        @Parameter(
            description = "Detailed reason for rejecting the reading (minimum 3 characters)",
            required = true,
            example = "Flow rate value exceeds maximum pipeline capacity"
        )
        @RequestParam @NotBlank(message = "Rejection reason is required") String rejectionReason
    ) {
        log.info("REST: Rejecting reading {} by employee {}", id, rejectedById);
        FlowReadingDTO result = readingWorkflowService.reject(id, rejectedById, rejectionReason);
        return ResponseEntity.ok(result);
    }
}
