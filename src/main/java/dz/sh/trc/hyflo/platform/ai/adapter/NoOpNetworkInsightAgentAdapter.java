package dz.sh.trc.hyflo.platform.ai.adapter;

import dz.sh.trc.hyflo.intelligence.network.dto.NetworkInsightRequestDTO;
import dz.sh.trc.hyflo.intelligence.network.dto.NetworkInsightResultDTO;
import dz.sh.trc.hyflo.intelligence.network.dto.NetworkOptimisationDTO;
import dz.sh.trc.hyflo.intelligence.network.port.NetworkInsightPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

/** No-op fallback for {@link NetworkInsightPort}. Active when {@code hyflo.ai.enabled=false}. */
@Component
@ConditionalOnProperty(name = "hyflo.ai.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpNetworkInsightAgentAdapter implements NetworkInsightPort {

    @Override
    public NetworkInsightResultDTO analyse(NetworkInsightRequestDTO request) {
        return new NetworkInsightResultDTO(
                request.segmentId(),
                "Network insight unavailable — hyflo.ai.enabled=false.",
                "NOMINAL", List.of(), 0, "NONE", 0, 0, Instant.now(), request.correlationId());
    }

    @Override
    public NetworkOptimisationDTO optimise(NetworkInsightRequestDTO request) {
        return new NetworkOptimisationDTO(
                request.segmentId(),
                "Network optimisation unavailable — hyflo.ai.enabled=false.",
                List.of(), 0.0, "NONE", Instant.now(), request.correlationId());
    }
}