package dz.sh.trc.hyflo.intelligence.capability;

import dz.sh.trc.hyflo.intelligence.network.dto.NetworkInsightRequestDTO;
import dz.sh.trc.hyflo.intelligence.network.dto.NetworkInsightResultDTO;
import dz.sh.trc.hyflo.intelligence.network.dto.NetworkOptimisationDTO;

/**
 * AI capability contract for pipeline network insight and optimisation intelligence.
 *
 * <p>Defines what the network domain expects from an AI capability:
 * topology analysis, operational insights, and optimisation suggestions
 * for the hydrocarbon pipeline network.</p>
 *
 * <p>Implemented by {@code intelligence.network.service.NetworkInsightService}.
 * The service delegates LLM calls through
 * {@code intelligence.network.port.NetworkInsightPort}.</p>
 */
public interface NetworkInsightCapability extends AiCapability {

    /**
     * Analyses the current state of a pipeline network segment and returns
     * AI-generated operational insights.
     *
     * @param request network context including topology, flow states, and alerts
     * @return structured insight result with narrative and key indicators
     */
    NetworkInsightResultDTO analyse(NetworkInsightRequestDTO request);

    /**
     * Suggests optimisation actions for a network segment based on current
     * operational data and AI pattern recognition.
     *
     * @param request network context
     * @return structured optimisation suggestions with priority ranking
     */
    NetworkOptimisationDTO optimise(NetworkInsightRequestDTO request);
}