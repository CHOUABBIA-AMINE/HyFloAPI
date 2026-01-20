/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: FlowThresholdController
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

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowThresholdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/hyflo/api/flow/thresholds")
@Tag(name = "Flow Thresholds", description = "Flow threshold configuration management")
public class FlowThresholdController extends GenericController<FlowThresholdDTO, Long> {
    
    private final FlowThresholdService flowThresholdService;
    
    public FlowThresholdController(FlowThresholdService flowThresholdService) {
        super(flowThresholdService, "FlowThreshold");
        this.flowThresholdService = flowThresholdService;
    }
    
    @GetMapping("/infrastructure/{infrastructureId}/active")
    @Operation(summary = "Get active thresholds for infrastructure")
    public ResponseEntity<List<FlowThresholdDTO>> getActiveByInfrastructure(
            @PathVariable Long infrastructureId) {
        return success(flowThresholdService.findActiveByInfrastructure(infrastructureId));
    }
    
    @GetMapping("/infrastructure/{infrastructureId}/type/{measurementTypeCode}/parameter/{parameter}")
    @Operation(summary = "Get active thresholds by infrastructure, type and parameter")
    public ResponseEntity<List<FlowThresholdDTO>> getActiveByInfrastructureAndType(
            @PathVariable Long infrastructureId,
            @PathVariable String measurementTypeCode,
            @PathVariable String parameter) {
        return success(flowThresholdService.findActiveByInfrastructureAndType(
            infrastructureId, measurementTypeCode, parameter));
    }
}
