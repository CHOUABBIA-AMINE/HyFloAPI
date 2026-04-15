package dz.sh.trc.hyflo.intelligence.flow.controller;

import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnalysisRequestDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnalysisResultDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnomalyReportDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowForecastDTO;
import dz.sh.trc.hyflo.intelligence.flow.service.FlowAnomalyAgentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing AI-powered flow intelligence endpoints.
 *
 * <p>URI base: {@code /intelligence/flow} — consistent with the HyFloAPI
 * module-based URI convention. No new endpoint format introduced.</p>
 *
 * <p>All methods delegate directly to {@link FlowAnomalyAgentService}.
 * Zero business logic in this controller.</p>
 */
@RestController
@RequestMapping("/intelligence/flow")
public class FlowAnomalyAgentController {

    private final FlowAnomalyAgentService flowAnomalyAgentService;

    public FlowAnomalyAgentController(FlowAnomalyAgentService flowAnomalyAgentService) {
        this.flowAnomalyAgentService = flowAnomalyAgentService;
    }

    /**
     * Analyses a set of flow readings and returns a structured AI narrative
     * with health score and anomaly flag.
     *
     * <p>POST /intelligence/flow/analyse</p>
     */
    @PostMapping("/analyse")
    @PreAuthorize("hasAuthority('INTELLIGENCE_FLOW_READ')")
    public ResponseEntity<FlowAnalysisResultDTO> analyse(
            @Valid @RequestBody FlowAnalysisRequestDTO request) {
        return ResponseEntity.ok(flowAnomalyAgentService.analyse(request));
    }

    /**
     * Detects anomalies in a set of flow readings.
     *
     * <p>POST /intelligence/flow/anomalies</p>
     */
    @PostMapping("/anomalies")
    @PreAuthorize("hasAuthority('INTELLIGENCE_FLOW_READ')")
    public ResponseEntity<FlowAnomalyReportDTO> detectAnomalies(
            @Valid @RequestBody FlowAnalysisRequestDTO request) {
        return ResponseEntity.ok(flowAnomalyAgentService.detectAnomalies(request));
    }

    /**
     * Generates a short-term AI-powered flow forecast.
     *
     * <p>POST /intelligence/flow/forecast</p>
     */
    @PostMapping("/forecast")
    @PreAuthorize("hasAuthority('INTELLIGENCE_FLOW_READ')")
    public ResponseEntity<FlowForecastDTO> forecast(
            @Valid @RequestBody FlowAnalysisRequestDTO request) {
        return ResponseEntity.ok(flowAnomalyAgentService.forecast(request));
    }
}