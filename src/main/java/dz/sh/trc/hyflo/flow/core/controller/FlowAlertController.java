/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowAlertController
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-21-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.FlowAlertDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/hyflo/api/flow/alerts")
@Tag(name = "Flow Alerts", description = "Flow alert and notification management")
public class FlowAlertController extends GenericController<FlowAlertDTO, Long> {
    
    private final FlowAlertService flowAlertService;
    
    public FlowAlertController(FlowAlertService flowAlertService) {
        super(flowAlertService, "FlowAlert");
        this.flowAlertService = flowAlertService;
    }
    
    @PutMapping("/{id}/acknowledge")
    @Operation(summary = "Acknowledge an alert")
    public ResponseEntity<FlowAlertDTO> acknowledgeAlert(
            @PathVariable Long id,
            @RequestParam Long employeeId) {
        return success(flowAlertService.acknowledgeAlert(id, employeeId));
    }
    
    @PutMapping("/{id}/resolve")
    @Operation(summary = "Resolve an alert")
    public ResponseEntity<FlowAlertDTO> resolveAlert(
            @PathVariable Long id,
            @RequestParam Long employeeId,
            @RequestBody Map<String, String> request) {
        String notes = request.get("resolutionNotes");
        return success(flowAlertService.resolveAlert(id, employeeId, notes));
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get all active alerts")
    public ResponseEntity<List<FlowAlertDTO>> getActiveAlerts() {
        return success(flowAlertService.findActiveAlerts());
    }
    
    @GetMapping("/infrastructure/{infrastructureId}/active")
    @Operation(summary = "Get active alerts for infrastructure")
    public ResponseEntity<List<FlowAlertDTO>> getActiveAlertsByInfrastructure(
            @PathVariable Long infrastructureId) {
        return success(flowAlertService.findActiveAlertsByInfrastructure(infrastructureId));
    }
}
