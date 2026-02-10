/**
 *
 * 	@Author		: MEDJERAB Abir
 * 	@Name		: FlowWorkflowController
 * 	@CreatedOn	: 01-28-2026
 * 	@UpdatedOn	: 02-03-2026
 * 	@UpdatedOn	: 02-10-2026 - Renamed from FlowMonitoringController to FlowWorkflowController
 *
 * 	@Type		: Controller
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.flow.core.dto.command.ReadingSubmitRequestDTO;
import dz.sh.trc.hyflo.flow.core.dto.command.ReadingValidationRequestDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingWorkflowService;
import dz.sh.trc.hyflo.flow.intelligence.dto.monitoring.SlotCoverageRequestDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.monitoring.SlotCoverageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller for flow reading workflow operations.
 * 
 * Handles:
 * - Reading submission
 * - Reading validation (approve/reject)
 * - Slot coverage queries
 * 
 * Note: This controller handles WORKFLOW operations, not analytics.
 * For monitoring/analytics endpoints, see FlowMonitoringController in intelligence package.
 */
@RestController
@RequestMapping("/flow/core/workflow")
@Tag(name = "Flow Workflow", description = "Slot-centric flow reading workflow operations")
@RequiredArgsConstructor
public class FlowWorkflowController {
    
    private final FlowReadingWorkflowService workflowService;
    
    /**
     * Get slot coverage for date + slot + structure
     */
    @PostMapping("/slot-coverage")
    @Operation(summary = "Get slot coverage for date + slot + structure")
    public ResponseEntity<SlotCoverageResponseDTO> getSlotCoverage(
        @Valid @RequestBody SlotCoverageRequestDTO request
    ) {
        SlotCoverageResponseDTO response = workflowService.getSlotCoverage(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Submit or update a reading
     */
    @PostMapping("/readings/submit")
    @Operation(summary = "Submit or update a reading")
    public ResponseEntity<Void> submitReading(
        @Valid @RequestBody ReadingSubmitRequestDTO request
    ) {
        workflowService.submitReading(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    /**
     * Validate a reading (approve or reject)
     */
    @PostMapping("/readings/validate")
    @Operation(summary = "Validate a reading (approve or reject)")
    public ResponseEntity<Void> validateReading(
        @Valid @RequestBody ReadingValidationRequestDTO request
    ) {
        workflowService.validateReading(request);
        return ResponseEntity.ok().build();
    }
}
