package dz.sh.trc.hyflo.platform.ai.guardrail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Validates tool call results before they are re-injected into the model context.
 *
 * <h3>Problem this solves:</h3>
 * <p>A tool that returns {@code null}, an HTTP error payload, or a stale
 * entity reference will silently pollute the model's next reasoning turn.
 * This validator intercepts tool outputs at the agent boundary and flags
 * or blocks problematic payloads before the model sees them.</p>
 *
 * <h3>Checks performed:</h3>
 * <ol>
 *   <li><strong>Null / empty guard</strong> — a null or blank result from
 *       any tool produces a BLOCK (prevents model reasoning on missing data).</li>
 *   <li><strong>Schema validation</strong> — if a required-field set is
 *       registered for the tool name, the JSON payload is checked for
 *       required field presence (same principle as
 *       {@link dz.sh.trc.hyflo.platform.ai.format.JsonSchemaValidator}
 *       but lighter — no full schema descriptor needed).</li>
 *   <li><strong>Stale reference detection</strong> — if the payload contains
 *       an {@code "id"} field, its value is checked against any registered
 *       existence port to detect stale references that survived cache.</li>
 * </ol>
 *
 * <h3>Design rule:</h3>
 * <p>This class does NOT transform tool output. It only validates.
 * Transformation/mapping is the responsibility of the tool implementation.</p>
 *
 * <h3>Registration model:</h3>
 * <p>Domain modules register tool-specific required fields at startup
 * via {@link #registerRequiredFields(String, Set)}. Tools with no
 * registration are only subject to the null guard.</p>
 */
@Component
public class ToolOutputValidator {

    private static final Logger log = LoggerFactory.getLogger(ToolOutputValidator.class);

    static final String NAME = "ToolOutputValidator";

    private final ObjectMapper objectMapper;

    /** tool name → required field names in its JSON output */
    private final Map<String, Set<String>> requiredFieldRegistry = new ConcurrentHashMap<>();

    /** tool name → existence port for its primary entity ID field */
    private final Map<String, ContextIntegrityValidator.EntityExistencePort> staleness
            = new ConcurrentHashMap<>();

    public ToolOutputValidator(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper");
    }

    // -------------------------------------------------------------------------
    // Registration
    // -------------------------------------------------------------------------

    /**
     * Registers the required fields for a named tool's JSON output.
     * Any absent field produces a WARN violation.
     *
     * @param toolName       the exact tool name used in the agent loop
     * @param requiredFields the set of required top-level JSON field names
     */
    public void registerRequiredFields(String toolName, Set<String> requiredFields) {
        Objects.requireNonNull(toolName,       "toolName");
        Objects.requireNonNull(requiredFields, "requiredFields");
        requiredFieldRegistry.put(toolName, Set.copyOf(requiredFields));
        log.info("[ToolOutputValidator] registered required fields for tool='{}'", toolName);
    }

    /**
     * Registers a staleness check port for a named tool.
     * When the tool output contains a top-level {@code "id"} field,
     * this port is called to verify the entity still exists.
     *
     * @param toolName the tool name
     * @param port     an existence port (e.g. pipeline repo {@code existsById})
     */
    public void registerStalenessPort(String toolName,
                                       ContextIntegrityValidator.EntityExistencePort port) {
        Objects.requireNonNull(toolName, "toolName");
        Objects.requireNonNull(port,     "port");
        staleness.put(toolName, port);
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Validates the raw string result of a tool call.
     *
     * @param toolName      the name of the tool that produced the result
     * @param rawResult     the raw result string (JSON expected)
     * @param correlationId for violation tracing
     * @return a list of {@link GuardrailViolation}s (empty = result is valid)
     */
    public List<GuardrailViolation> validate(String toolName, String rawResult,
                                              String correlationId) {
        Objects.requireNonNull(toolName, "toolName must not be null");

        List<GuardrailViolation> violations = new ArrayList<>();

        // Check 1: Null / empty guard
        if (rawResult == null || rawResult.isBlank()) {
            violations.add(GuardrailViolation.builder()
                    .severity(GuardrailViolation.Severity.BLOCK)
                    .category(GuardrailViolation.Category.TOOL_OUTPUT_NULL)
                    .guardrailName(NAME)
                    .detail("Tool '" + toolName + "' returned a null or blank result.")
                    .correlationId(correlationId)
                    .build());
            log.warn("[ToolOutputValidator] null/blank result from tool='{}' correlationId={}",
                    toolName, correlationId);
            return violations;
        }

        // Check 2: Required fields (only if registered)
        Set<String> requiredFields = requiredFieldRegistry.get(toolName);
        if (requiredFields != null && !requiredFields.isEmpty()) {
            try {
                JsonNode root = objectMapper.readTree(rawResult);
                if (root.isObject()) {
                    for (String field : requiredFields) {
                        JsonNode node = root.get(field);
                        if (node == null || node.isNull()) {
                            violations.add(GuardrailViolation.builder()
                                    .severity(GuardrailViolation.Severity.WARN)
                                    .category(GuardrailViolation.Category.TOOL_OUTPUT_SCHEMA_INVALID)
                                    .guardrailName(NAME)
                                    .detail("Tool '" + toolName + "' output missing required field '"
                                            + field + "'.")
                                    .correlationId(correlationId)
                                    .build());
                        }
                    }
                } else {
                    violations.add(GuardrailViolation.builder()
                            .severity(GuardrailViolation.Severity.WARN)
                            .category(GuardrailViolation.Category.TOOL_OUTPUT_SCHEMA_INVALID)
                            .guardrailName(NAME)
                            .detail("Tool '" + toolName
                                    + "' output is not a JSON object — required-field"
                                    + " validation skipped.")
                            .correlationId(correlationId)
                            .build());
                }
            } catch (Exception e) {
                violations.add(GuardrailViolation.builder()
                        .severity(GuardrailViolation.Severity.WARN)
                        .category(GuardrailViolation.Category.TOOL_OUTPUT_SCHEMA_INVALID)
                        .guardrailName(NAME)
                        .detail("Tool '" + toolName + "' output is not valid JSON: "
                                + e.getMessage())
                        .correlationId(correlationId)
                        .build());
            }
        }

        // Check 3: Staleness check for top-level "id" field
        ContextIntegrityValidator.EntityExistencePort port = staleness.get(toolName);
        if (port != null) {
            try {
                JsonNode root = objectMapper.readTree(rawResult);
                JsonNode idNode = root.path("id");
                if (!idNode.isMissingNode() && !idNode.isNull()) {
                    String id = idNode.asText();
                    if (!port.exists(id)) {
                        violations.add(GuardrailViolation.builder()
                                .severity(GuardrailViolation.Severity.WARN)
                                .category(GuardrailViolation.Category.TOOL_OUTPUT_STALE_REFERENCE)
                                .guardrailName(NAME)
                                .detail("Tool '" + toolName + "' output references id='"
                                        + id + "' which no longer exists in the domain store.")
                                .correlationId(correlationId)
                                .build());
                        log.warn("[ToolOutputValidator] stale reference detected tool='{}' "
                                + "id='{}' correlationId={}", toolName, id, correlationId);
                    }
                }
            } catch (Exception e) {
                log.debug("[ToolOutputValidator] staleness check error tool='{}' "
                        + "correlationId={}: {}", toolName, correlationId, e.getMessage());
            }
        }

        return violations;
    }
}