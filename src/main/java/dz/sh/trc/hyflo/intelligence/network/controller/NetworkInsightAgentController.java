package dz.sh.trc.hyflo.intelligence.network.controller;

import dz.sh.trc.hyflo.intelligence.network.dto.NetworkInsightRequestDTO;
import dz.sh.trc.hyflo.intelligence.network.dto.NetworkInsightResultDTO;
import dz.sh.trc.hyflo.intelligence.network.dto.NetworkOptimisationDTO;
import dz.sh.trc.hyflo.intelligence.network.service.NetworkInsightAgentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing AI-powered network intelligence endpoints.
 *
 * <p>URI base: {@code /intelligence/network} — consistent with HyFloAPI
 * module-based URI convention. No new endpoint format introduced.</p>
 *
 * <p>Zero business logic in this controller. Full delegation to
 * {@link NetworkInsightAgentService}.</p>
 */
@RestController
@RequestMapping("/intelligence/network")
public class NetworkInsightAgentController {

    private final NetworkInsightAgentService networkInsightAgentService;

    public NetworkInsightAgentController(
            NetworkInsightAgentService networkInsightAgentService) {
        this.networkInsightAgentService = networkInsightAgentService;
    }

    /**
     * Analyses a network segment and returns AI-generated operational insight.
     *
     * <p>POST /intelligence/network/analyse</p>
     */
    @PostMapping("/analyse")
    @PreAuthorize("hasAuthority('INTELLIGENCE_NETWORK_READ')")
    public ResponseEntity<NetworkInsightResultDTO> analyse(
            @Valid @RequestBody NetworkInsightRequestDTO request) {
        return ResponseEntity.ok(networkInsightAgentService.analyse(request));
    }

    /**
     * Generates AI-powered optimisation suggestions for a network segment.
     *
     * <p>POST /intelligence/network/optimise</p>
     */
    @PostMapping("/optimise")
    @PreAuthorize("hasAuthority('INTELLIGENCE_NETWORK_READ')")
    public ResponseEntity<NetworkOptimisationDTO> optimise(
            @Valid @RequestBody NetworkInsightRequestDTO request) {
        return ResponseEntity.ok(networkInsightAgentService.optimise(request));
    }
}