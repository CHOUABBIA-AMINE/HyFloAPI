package dz.sh.trc.hyflo.platform.ai.tool.tools;

import dz.sh.trc.hyflo.flow.core.service.FlowOperationQueryService;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingQueryService;
import dz.sh.trc.hyflo.platform.ai.tool.AiTool;
import dz.sh.trc.hyflo.platform.ai.tool.ToolFunction;
import dz.sh.trc.hyflo.platform.ai.tool.ToolResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * AI tool: queries active flow operations and recent sensor readings.
 *
 * <h3>Delegation:</h3>
 * <ul>
 *   <li>{@link FlowOperationQueryService} — retrieves current pipeline
 *       operation status (flow rate, pressure, volume).</li>
 *   <li>{@link FlowReadingQueryService} — retrieves the most recent
 *       sensor readings for a given pipeline segment.</li>
 * </ul>
 *
 * <p>This tool is read-only and safe to call multiple times per turn.
 * It never writes data and never accesses repositories directly.</p>
 */
@AiTool(
    name        = "flow_query",
    description = "Query active flow operations and recent sensor readings for a pipeline "
                + "or flow segment. Returns flow rate, pressure, volume, and the latest "
                + "sensor values. Use this to understand current flow state before "
                + "diagnosing anomalies or recommending adjustments.",
    domain      = AiTool.Domain.FLOW,
    readOnly    = true
)
@Component
public class FlowQueryTool implements ToolFunction<FlowQueryTool.Input, ToolResult> {

    private static final Logger log = LoggerFactory.getLogger(FlowQueryTool.class);
    private static final String TOOL_NAME = "flow_query";

    private final FlowOperationQueryService operationQueryService;
    private final FlowReadingQueryService   readingQueryService;

    public FlowQueryTool(FlowOperationQueryService operationQueryService,
                          FlowReadingQueryService readingQueryService) {
        this.operationQueryService = Objects.requireNonNull(operationQueryService);
        this.readingQueryService   = Objects.requireNonNull(readingQueryService);
    }

    @Override
    public ToolResult apply(Input input) {
        Objects.requireNonNull(input, "FlowQueryTool input must not be null");

        log.debug("[FlowQueryTool] querying segmentId={} includeReadings={}",
                input.segmentId(), input.includeReadings());

        try {
            // Delegate to existing query services — no repository access here
            Object operations = operationQueryService
                    .getOperationsBySegmentId(input.segmentId());

            if (!input.includeReadings()) {
                return ToolResult.ok(TOOL_NAME, new FlowSummary(
                        input.segmentId(), operations, null));
            }

            Object readings = readingQueryService
                    .getLatestReadingsBySegmentId(input.segmentId(), input.maxReadings());

            return ToolResult.ok(TOOL_NAME, new FlowSummary(
                    input.segmentId(), operations, readings));

        } catch (Exception e) {
            log.warn("[FlowQueryTool] query failed for segmentId={}: {}",
                    input.segmentId(), e.getMessage());
            return ToolResult.failure(TOOL_NAME, "FLOW_QUERY_ERROR", e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Input / output records
    // -------------------------------------------------------------------------

    /**
     * Input to the flow query tool.
     *
     * @param segmentId      the pipeline segment identifier (required)
     * @param includeReadings whether to include the latest sensor readings
     * @param maxReadings     maximum number of readings to return (default 10)
     */
    public record Input(
            String  segmentId,
            boolean includeReadings,
            int     maxReadings
    ) {
        public Input {
            Objects.requireNonNull(segmentId, "segmentId must not be null");
            if (maxReadings <= 0) maxReadings = 10;
        }
    }

    /** Compact payload sent back to the LLM. */
    public record FlowSummary(
            String segmentId,
            Object operations,
            Object readings
    ) {}
}