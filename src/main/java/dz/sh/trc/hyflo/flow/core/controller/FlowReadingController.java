/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowReadingController
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/hyflo/api/network/flow/readings")
@Tag(name = "Flow Readings", description = "Manual flow measurement readings management")
public class FlowReadingController extends GenericController<FlowReadingDTO, Long> {
    
    private final FlowReadingService flowReadingService;
    
    public FlowReadingController(FlowReadingService flowReadingService) {
        super(flowReadingService, "FlowReading");
        this.flowReadingService = flowReadingService;
    }
    
    @PostMapping("/submit")
    @Operation(summary = "Submit a new flow reading", 
               description = "Creates a new flow reading with SUBMITTED status")
    public ResponseEntity<FlowReadingDTO> submitReading(@RequestBody FlowReadingDTO dto) {
        return success(flowReadingService.submitReading(dto));
    }
    
    @PutMapping("/{id}/validate")
    @Operation(summary = "Validate a submitted reading", 
               description = "Supervisor validates a reading from same structure")
    public ResponseEntity<FlowReadingDTO> validateReading(
            @PathVariable Long id,
            @RequestParam Long validatorEmployeeId) {
        return success(flowReadingService.validateReading(id, validatorEmployeeId));
    }
    
    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject a submitted reading", 
               description = "Supervisor rejects a reading with reason")
    public ResponseEntity<FlowReadingDTO> rejectReading(
            @PathVariable Long id,
            @RequestParam Long validatorEmployeeId,
            @RequestBody Map<String, String> request) {
        String reason = request.get("reason");
        return success(flowReadingService.rejectReading(id, validatorEmployeeId, reason));
    }
    
    @GetMapping("/search/by-date")
    @Operation(summary = "Search readings by date range")
    public ResponseEntity<List<FlowReadingDTO>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return success(flowReadingService.findByDateRange(start, end));
    }
    
    @GetMapping("/search/by-infrastructure")
    @Operation(summary = "Search readings by infrastructure and date range")
    public ResponseEntity<List<FlowReadingDTO>> findByInfrastructure(
            @RequestParam Long infrastructureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return success(flowReadingService.findByInfrastructureAndDateRange(
            infrastructureId, start, end));
    }
    
    @GetMapping("/search/by-operator")
    @Operation(summary = "Search readings by operator and date range")
    public ResponseEntity<List<FlowReadingDTO>> findByOperator(
            @RequestParam Long operatorEmployeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return success(flowReadingService.findByOperatorAndDateRange(
            operatorEmployeeId, start, end));
    }
    
    @GetMapping("/search/by-structure")
    @Operation(summary = "Search readings by structure (regional directorate) and date range")
    public ResponseEntity<List<FlowReadingDTO>> findByStructure(
            @RequestParam Long structureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return success(flowReadingService.findByStructure(structureId, start, end));
    }
    
    @GetMapping("/pending-validation")
    @Operation(summary = "Get all readings pending validation")
    public ResponseEntity<List<FlowReadingDTO>> getPendingValidation() {
        return success(flowReadingService.findPendingValidation());
    }
    
    @GetMapping("/pending-validation/structure/{structureId}")
    @Operation(summary = "Get readings pending validation for a specific structure")
    public ResponseEntity<List<FlowReadingDTO>> getPendingValidationByStructure(
            @PathVariable Long structureId) {
        return success(flowReadingService.findPendingValidationByStructure(structureId));
    }
}
