package dz.sh.trc.hyflo.intelligence.network.service;

import dz.sh.trc.hyflo.intelligence.capability.NetworkInsightCapability;
import dz.sh.trc.hyflo.intelligence.network.dto.NetworkInsightRequestDTO;
import dz.sh.trc.hyflo.intelligence.network.dto.NetworkInsightResultDTO;
import dz.sh.trc.hyflo.intelligence.network.dto.NetworkOptimisationDTO;
import dz.sh.trc.hyflo.intelligence.network.port.NetworkInsightAgentPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Entry point for all AI-powered network insight use cases.
 *
 * <p>Implements {@link NetworkInsightCapability} — the contract declared
 * in the capability layer. Delegates all LLM calls through
 * {@link NetworkInsightAgentPort} — never calls Spring AI directly.</p>
 *
 * <h3>Dependency direction (strict):</h3>
 * <pre>
 *   NetworkInsightAgentController
 *               ↓
 *   NetworkInsightAgentService  (this — intelligence layer)
 *               ↓
 *   NetworkInsightAgentPort     (outbound port — intelligence layer)
 *               ↓ (injected at runtime)
 *   NetworkInsightAgentAdapter  (platform/ai — Spring AI adapter)
 * </pre>
 */
@Service
public class NetworkInsightAgentService implements NetworkInsightCapability {

    private static final Logger log =
            LoggerFactory.getLogger(NetworkInsightAgentService.class);

    private final NetworkInsightAgentPort networkInsightAgentPort;

    public NetworkInsightAgentService(NetworkInsightAgentPort networkInsightAgentPort) {
        this.networkInsightAgentPort =
                Objects.requireNonNull(networkInsightAgentPort,
                        "NetworkInsightAgentPort must not be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkInsightResultDTO analyse(NetworkInsightRequestDTO request) {
        Objects.requireNonNull(request, "NetworkInsightRequestDTO must not be null");
        log.info("[NetworkInsightAgentService] analyse — segment={} correlationId={}",
                request.networkSegmentId(), request.correlationId());
        return networkInsightAgentPort.analyse(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkOptimisationDTO optimise(NetworkInsightRequestDTO request) {
        Objects.requireNonNull(request, "NetworkInsightRequestDTO must not be null");
        log.info("[NetworkInsightAgentService] optimise — segment={} correlationId={}",
                request.networkSegmentId(), request.correlationId());
        return networkInsightAgentPort.optimise(request);
    }
}