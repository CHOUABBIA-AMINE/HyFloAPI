package dz.sh.trc.hyflo.intelligence.network.port;

import dz.sh.trc.hyflo.intelligence.network.dto.NetworkInsightRequestDTO;
import dz.sh.trc.hyflo.intelligence.network.dto.NetworkInsightResultDTO;
import dz.sh.trc.hyflo.intelligence.network.dto.NetworkOptimisationDTO;

/**
 * Outbound port connecting the network intelligence service to the AI platform layer.
 *
 * <p>This interface is the boundary between {@code intelligence/network} and
 * the LLM adapter in {@code platform/ai}. The service depends on this port
 * exclusively — never on Spring AI classes directly.</p>
 *
 * <p>The live implementation is
 * {@code dz.sh.trc.hyflo.platform.ai.adapter.NetworkInsightAgentAdapter},
 * injected when {@code hyflo.ai.enabled=true}.
 * The no-op fallback {@code NoOpNetworkInsightAgentAdapter} is injected otherwise.</p>
 */
public interface NetworkInsightAgentPort {

    /**
     * Analyses network topology and operational state to produce insight.
     *
     * @param request network context including node states and active alerts
     * @return structured insight with health score and key indicators
     */
    NetworkInsightResultDTO analyse(NetworkInsightRequestDTO request);

    /**
     * Suggests optimisation actions for the network segment.
     *
     * @param request network context
     * @return structured optimisation suggestions with priority ranking
     */
    NetworkOptimisationDTO optimise(NetworkInsightRequestDTO request);
}