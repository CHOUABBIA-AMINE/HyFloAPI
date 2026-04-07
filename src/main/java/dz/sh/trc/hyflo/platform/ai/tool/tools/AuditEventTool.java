package dz.sh.trc.hyflo.platform.ai.tool.tools;

import dz.sh.trc.hyflo.domain.audit.service.AuditedService;
import dz.sh.trc.hyflo.platform.ai.tool.AiTool;
import dz.sh.trc.hyflo.platform.ai.tool.ToolFunction;
import dz.sh.trc.hyflo.platform.ai.tool.ToolResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

/**
 * AI tool: queries audit event history for a user, entity, or time range.
 *
 * <h3>Delegation:</h3>
 * <ul>
 *   <li>{@link AuditedService} — retrieves audit trail records filtered by
 *       entityType, entityId, actorId, and/or time range.</li>
 * </ul>
 *
 * <h3>Safety note:</h3>
 * <p>This tool exposes audit records to the LLM. The agent layer must enforce
 * RBAC before dispatching this tool — only principals with AUDIT_READ
 * permission should reach it. The tool itself is read-only and performs
 * no authorization checks.</p>
 */
@AiTool(
    name        = "audit_events",
    description = "Query the audit event trail for a specific entity or user. "
                + "Use this to understand what actions were performed, when, and by whom. "
                + "Provide entityType and entityId to scope the query. "
                + "Optionally filter by actorId (user who performed the action) "
                + "or a time window (fromInstant / toInstant in ISO-8601).",
    domain      = AiTool.Domain.AUDIT,
    readOnly    = true
)
@Component
public class AuditEventTool implements ToolFunction<AuditEventTool.Input, ToolResult> {

    private static final Logger log = LoggerFactory.getLogger(AuditEventTool.class);
    private static final String TOOL_NAME = "audit_events";

    private static final int MAX_RESULTS = 50;

    private final AuditedService auditedService;

    public AuditEventTool(AuditedService auditedService) {
        this.auditedService = Objects.requireNonNull(auditedService,
                "AuditedService must not be null");
    }

    @Override
    public ToolResult apply(Input input) {
        Objects.requireNonNull(input, "AuditEventTool input must not be null");

        log.debug("[AuditEventTool] entityType={} entityId={} actorId={} from={} to={}",
                input.entityType(), input.entityId(),
                input.actorId(), input.from(), input.to());

        try {
            int limit = input.maxResults() > 0
                    ? Math.min(input.maxResults(), MAX_RESULTS) : MAX_RESULTS;

            Object events = auditedService.findAuditEvents(
                    input.entityType(),
                    input.entityId(),
                    input.actorId(),
                    input.from(),
                    input.to(),
                    limit
            );

            return ToolResult.ok(TOOL_NAME, new AuditSummary(
                    input.entityType(), input.entityId(), events));

        } catch (Exception e) {
            log.warn("[AuditEventTool] query failed for entityType={} entityId={}: {}",
                    input.entityType(), input.entityId(), e.getMessage());
            return ToolResult.failure(TOOL_NAME, "AUDIT_QUERY_ERROR", e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Input / output records
    // -------------------------------------------------------------------------

    /**
     * @param entityType  the audited entity class name (e.g. "Pipeline", "Incident")
     * @param entityId    the entity's primary key as string (nullable for actor-only queries)
     * @param actorId     the user who performed the action (nullable)
     * @param from        start of time window (nullable = unbounded start)
     * @param to          end of time window (nullable = now)
     * @param maxResults  max records to return (capped at 50)
     */
    public record Input(
            String  entityType,
            String  entityId,
            String  actorId,
            Instant from,
            Instant to,
            int     maxResults
    ) {}

    public record AuditSummary(
            String entityType,
            String entityId,
            Object events
    ) {}
}