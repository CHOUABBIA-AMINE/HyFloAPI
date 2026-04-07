package dz.sh.trc.hyflo.platform.ai.tool.tools;

import dz.sh.trc.hyflo.crisis.core.service.IncidentImpactService;
import dz.sh.trc.hyflo.crisis.core.service.IncidentService;
import dz.sh.trc.hyflo.platform.ai.tool.AiTool;
import dz.sh.trc.hyflo.platform.ai.tool.ToolFunction;
import dz.sh.trc.hyflo.platform.ai.tool.ToolResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * AI tool: retrieves crisis incident data and associated impact assessments.
 *
 * <h3>Delegation:</h3>
 * <ul>
 *   <li>{@link IncidentService} — retrieves incident records (type, severity,
 *       status, location, timestamps).</li>
 *   <li>{@link IncidentImpactService} — retrieves the impact assessment linked
 *       to an incident (affected volume, population, infrastructure nodes).</li>
 * </ul>
 *
 * <p>Read-only. Does not modify incident status or create new records.
 * The LLM must not be allowed to escalate or close incidents through this tool.</p>
 */
@AiTool(
    name        = "crisis_data",
    description = "Retrieve active crisis incidents and their impact assessments. "
                + "Use this to understand the severity, location, and downstream effects "
                + "of an ongoing incident before recommending a crisis response. "
                + "Provide an incidentId for a specific incident, or leave null to "
                + "retrieve all active incidents.",
    domain      = AiTool.Domain.CRISIS,
    readOnly    = true
)
@Component
public class CrisisDataTool implements ToolFunction<CrisisDataTool.Input, ToolResult> {

    private static final Logger log = LoggerFactory.getLogger(CrisisDataTool.class);
    private static final String TOOL_NAME = "crisis_data";

    private final IncidentService       incidentService;
    private final IncidentImpactService incidentImpactService;

    public CrisisDataTool(IncidentService incidentService,
                           IncidentImpactService incidentImpactService) {
        this.incidentService       = Objects.requireNonNull(incidentService);
        this.incidentImpactService = Objects.requireNonNull(incidentImpactService);
    }

    @Override
    public ToolResult apply(Input input) {
        Objects.requireNonNull(input, "CrisisDataTool input must not be null");

        log.debug("[CrisisDataTool] incidentId={} includeImpact={}",
                input.incidentId(), input.includeImpact());

        try {
            Object incident;

            if (input.incidentId() != null) {
                // Specific incident lookup
                incident = incidentService.getIncidentById(input.incidentId());
            } else {
                // All active incidents
                incident = incidentService.getActiveIncidents();
            }

            if (!input.includeImpact() || input.incidentId() == null) {
                return ToolResult.ok(TOOL_NAME,
                        new CrisisSummary(input.incidentId(), incident, null));
            }

            Object impact = incidentImpactService
                    .getImpactByIncidentId(input.incidentId());

            return ToolResult.ok(TOOL_NAME,
                    new CrisisSummary(input.incidentId(), incident, impact));

        } catch (Exception e) {
            log.warn("[CrisisDataTool] query failed for incidentId={}: {}",
                    input.incidentId(), e.getMessage());
            return ToolResult.failure(TOOL_NAME, "CRISIS_QUERY_ERROR", e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Input / output records
    // -------------------------------------------------------------------------

    /**
     * @param incidentId    specific incident ID, or null to fetch all active incidents
     * @param includeImpact whether to fetch the impact assessment (only used when
     *                      incidentId is non-null)
     */
    public record Input(
            Long    incidentId,
            boolean includeImpact
    ) {}

    public record CrisisSummary(
            Long   incidentId,
            Object incident,
            Object impact
    ) {}
}