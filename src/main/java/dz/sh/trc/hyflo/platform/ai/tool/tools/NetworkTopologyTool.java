package dz.sh.trc.hyflo.platform.ai.tool.tools;

import dz.sh.trc.hyflo.network.core.service.PipelineService;
import dz.sh.trc.hyflo.network.core.service.PipelineSystemService;
import dz.sh.trc.hyflo.network.core.service.PipelineSegmentService;
import dz.sh.trc.hyflo.platform.ai.tool.AiTool;
import dz.sh.trc.hyflo.platform.ai.tool.ToolFunction;
import dz.sh.trc.hyflo.platform.ai.tool.ToolResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * AI tool: retrieves pipeline network topology and segment configuration.
 *
 * <h3>Delegation:</h3>
 * <ul>
 *   <li>{@link PipelineService} — pipeline metadata (id, name, type, status).</li>
 *   <li>{@link PipelineSegmentService} — segment-level detail (length, diameter,
 *       material, connected nodes).</li>
 *   <li>{@link PipelineSystemService} — system-level topology (pipeline system
 *       hierarchy, upstream/downstream relationships).</li>
 * </ul>
 *
 * <p>Read-only. The LLM uses this to reason about network structure when
 * diagnosing flow issues or assessing incident propagation paths.</p>
 */
@AiTool(
    name        = "network_topology",
    description = "Retrieve pipeline network topology, segment configuration, and "
                + "upstream/downstream relationships. Use this to understand the "
                + "physical structure of the hydrocarbon network when diagnosing "
                + "flow anomalies, tracing incident propagation, or planning "
                + "routing changes.",
    domain      = AiTool.Domain.NETWORK,
    readOnly    = true
)
@Component
public class NetworkTopologyTool implements ToolFunction<NetworkTopologyTool.Input, ToolResult> {

    private static final Logger log = LoggerFactory.getLogger(NetworkTopologyTool.class);
    private static final String TOOL_NAME = "network_topology";

    private final PipelineService        pipelineService;
    private final PipelineSegmentService segmentService;
    private final PipelineSystemService  systemService;

    public NetworkTopologyTool(PipelineService pipelineService,
                                PipelineSegmentService segmentService,
                                PipelineSystemService systemService) {
        this.pipelineService = Objects.requireNonNull(pipelineService);
        this.segmentService  = Objects.requireNonNull(segmentService);
        this.systemService   = Objects.requireNonNull(systemService);
    }

    @Override
    public ToolResult apply(Input input) {
        Objects.requireNonNull(input, "NetworkTopologyTool input must not be null");

        log.debug("[NetworkTopologyTool] pipelineId={} includeSegments={} includeSystem={}",
                input.pipelineId(), input.includeSegments(), input.includeSystemContext());

        try {
            Object pipeline = pipelineService.getPipelineById(input.pipelineId());

            Object segments = null;
            if (input.includeSegments()) {
                segments = segmentService.getSegmentsByPipelineId(input.pipelineId());
            }

            Object systemContext = null;
            if (input.includeSystemContext()) {
                systemContext = systemService.getSystemContextForPipeline(input.pipelineId());
            }

            return ToolResult.ok(TOOL_NAME,
                    new TopologySummary(input.pipelineId(), pipeline, segments, systemContext));

        } catch (Exception e) {
            log.warn("[NetworkTopologyTool] query failed for pipelineId={}: {}",
                    input.pipelineId(), e.getMessage());
            return ToolResult.failure(TOOL_NAME, "NETWORK_TOPOLOGY_ERROR", e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Input / output records
    // -------------------------------------------------------------------------

    /**
     * @param pipelineId            the pipeline identifier (required)
     * @param includeSegments       whether to include segment-level detail
     * @param includeSystemContext  whether to include system topology context
     */
    public record Input(
            Long    pipelineId,
            boolean includeSegments,
            boolean includeSystemContext
    ) {
        public Input {
            Objects.requireNonNull(pipelineId, "pipelineId must not be null");
        }
    }

    public record TopologySummary(
            Long   pipelineId,
            Object pipeline,
            Object segments,
            Object systemContext
    ) {}
}