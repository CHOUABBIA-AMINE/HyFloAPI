/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: FlowEventController
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
import dz.sh.trc.hyflo.flow.core.dto.FlowEventDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/hyflo/api/flow/events")
@Tag(name = "Flow Events", description = "Flow operational event management")
public class FlowEventController extends GenericController<FlowEventDTO, Long> {
    
    private final FlowEventService flowEventService;
    
    public FlowEventController(FlowEventService flowEventService) {
        super(flowEventService, "FlowEvent");
        this.flowEventService = flowEventService;
    }
    
    @GetMapping("/infrastructure/{infrastructureId}")
    @Operation(summary = "Get events for infrastructure")
    public ResponseEntity<List<FlowEventDTO>> getEventsByInfrastructure(
            @PathVariable Long infrastructureId) {
        return success(flowEventService.findByInfrastructure(infrastructureId));
    }
    
    @GetMapping("/active/flow-impact")
    @Operation(summary = "Get active events with flow impact")
    public ResponseEntity<List<FlowEventDTO>> getActiveEventsWithFlowImpact() {
        return success(flowEventService.findActiveEventsWithFlowImpact());
    }
}
