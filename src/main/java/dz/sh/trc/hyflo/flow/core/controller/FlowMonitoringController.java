/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowMonitoringController
 * 	@CreatedOn	: 01-28-2026
 * 	@UpdatedOn	: 02-03-2026
 *
 * 	@Type		: Controller
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import dz.sh.trc.hyflo.flow.core.dto.*;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingWorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flow/core/monitoring")
@Tag(name = "Flow Monitoring", description = "Slot-centric flow monitoring and validation")
@RequiredArgsConstructor
public class FlowMonitoringController {
    
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
