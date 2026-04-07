package dz.sh.trc.hyflo.platform.ai.guardrail;

import dz.sh.trc.hyflo.platform.ai.agent.AgentContext;
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
 * Validates the domain entity references declared in
 * {@link AgentContext#getDomainContext()} before the context is
 * injected into any LLM prompt.
 *
 * <h3>Problem this solves:</h3>
 * <p>An AI agent receiving stale or non-existent entity IDs (e.g. a
 * {@code pipelineId} that was deleted, or a {@code stationId} that belongs
 * to a different network) will produce hallucinated or contradictory
 * responses. Validating entity existence here, before the first LLM call,
 * prevents that class of contamination entirely.</p>
 *
 * <h3>Design: port-based entity existence, no direct repository coupling</h3>
 * <p>This class does NOT import any domain repository directly.
 * Instead, business modules register an {@link EntityExistencePort}
 * for each domain key they own (e.g. {@code "pipelineId"},
 * {@code "stationId"}, {@code "networkId"}). The port is a single-method
 * functional interface, easy to implement and stub in tests.</p>
 *
 * <h3>Domain context keys validated:</h3>
 * <p>Only keys that have a registered {@link EntityExistencePort} are
 * validated. Unknown keys are silently accepted (they may be system hints
 * or future extension keys). This avoids false positives for legitimate
 * context enrichment keys like {@code "urgencyLevel"} or
 * {@code "requestedLanguage"}.</p>
 *
 * <h3>Violation types produced:</h3>
 * <ul>
 *   <li>{@link GuardrailViolation.Category#CONTEXT_ENTITY_NOT_FOUND} — BLOCK:
 *       the entity ID in the context does not exist in the database.</li>
 *   <li>{@link GuardrailViolation.Category#CONTEXT_DOMAIN_VALUE_INVALID} — WARN:
 *       the value is present but its format is invalid (e.g. blank, whitespace).</li>
 *   <li>{@link GuardrailViolation.Category#CONTEXT_DOMAIN_KEY_MISSING} — WARN:
 *       a key declared as mandatory is absent from the context map.</li>
 * </ul>
 */
@Component
public class ContextIntegrityValidator {

    private static final Logger log = LoggerFactory.getLogger(ContextIntegrityValidator.class);

    static final String NAME = "ContextIntegrityValidator";

    // -------------------------------------------------------------------------
    // Port — registered by domain modules at startup
    // -------------------------------------------------------------------------

    /**
     * Single-method port for entity existence checks.
     * Implement once per aggregate root (Pipeline, Station, Network, Alert, etc.)
     * and register via {@link #registerExistencePort(String, EntityExistencePort)}.
     */
    @FunctionalInterface
    public interface EntityExistencePort {
        /**
         * @param entityId the value from {@link AgentContext#getDomainContext()}
         * @return true if the entity exists in the domain store, false otherwise
         */
        boolean exists(String entityId);
    }

    // -------------------------------------------------------------------------
    // Registry of domain key → existence port
    // -------------------------------------------------------------------------

    private final Map<String, EntityExistencePort> existencePorts  = new ConcurrentHashMap<>();
    private final Set<String>                      mandatoryKeys   = ConcurrentHashMap.newKeySet();

    /**
     * Registers an existence port for a domain context key.
     *
     * <p>Called during application startup by domain-module configuration
     * classes (e.g. {@code NetworkAiConfiguration}, {@code FlowAiConfiguration}).</p>
     *
     * @param domainContextKey the key used in {@link AgentContext#getDomainContext()}
     *                         (e.g. {@code "pipelineId"}, {@code "stationId"})
     * @param port             the existence check implementation
     */
    public void registerExistencePort(String domainContextKey, EntityExistencePort port) {
        Objects.requireNonNull(domainContextKey, "domainContextKey");
        Objects.requireNonNull(port,             "port");
        existencePorts.put(domainContextKey, port);
        log.info("[ContextIntegrityValidator] registered existence port for key='{}'",
                domainContextKey);
    }

    /**
     * Declares a context key as mandatory — its absence in the
     * {@link AgentContext#getDomainContext()} map produces a WARN violation.
     *
     * @param domainContextKey the mandatory key
     */
    public void declareMandatoryKey(String domainContextKey) {
        Objects.requireNonNull(domainContextKey, "domainContextKey");
        mandatoryKeys.add(domainContextKey);
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Validates the domain context of an {@link AgentContext}.
     *
     * <p>For each registered {@link EntityExistencePort}, looks up the
     * corresponding key in {@link AgentContext#getDomainContext()} and
     * verifies existence. Also checks mandatory key presence.</p>
     *
     * @param context       the agent context to validate
     * @param correlationId for violation tracing (may differ from context's own)
     * @return a list of {@link GuardrailViolation}s (empty = context is clean)
     */
    public List<GuardrailViolation> validate(AgentContext context, String correlationId) {
        Objects.requireNonNull(context, "AgentContext must not be null");

        List<GuardrailViolation>  violations = new ArrayList<>();
        Map<String, String>       domainCtx  = context.getDomainContext();

        // Check 1: Mandatory keys present
        for (String mandatoryKey : mandatoryKeys) {
            if (!domainCtx.containsKey(mandatoryKey)
                    || domainCtx.get(mandatoryKey) == null
                    || domainCtx.get(mandatoryKey).isBlank()) {
                violations.add(GuardrailViolation.builder()
                        .severity(GuardrailViolation.Severity.WARN)
                        .category(GuardrailViolation.Category.CONTEXT_DOMAIN_KEY_MISSING)
                        .guardrailName(NAME)
                        .detail("Mandatory domain context key '" + mandatoryKey
                                + "' is missing or blank.")
                        .correlationId(correlationId)
                        .build());
                log.warn("[ContextIntegrityValidator] missing mandatory key='{}' correlationId={}",
                        mandatoryKey, correlationId);
            }
        }

        // Check 2: Entity existence for registered ports
        for (Map.Entry<String, EntityExistencePort> entry : existencePorts.entrySet()) {
            String key   = entry.getKey();
            String value = domainCtx.get(key);

            if (value == null) {
                // Key not present → skip (only mandatory check above applies)
                continue;
            }

            if (value.isBlank()) {
                violations.add(GuardrailViolation.builder()
                        .severity(GuardrailViolation.Severity.WARN)
                        .category(GuardrailViolation.Category.CONTEXT_DOMAIN_VALUE_INVALID)
                        .guardrailName(NAME)
                        .detail("Domain context key '" + key
                                + "' is present but has a blank value.")
                        .correlationId(correlationId)
                        .build());
                continue;
            }

            try {
                boolean exists = entry.getValue().exists(value);
                if (!exists) {
                    violations.add(GuardrailViolation.builder()
                            .severity(GuardrailViolation.Severity.BLOCK)
                            .category(GuardrailViolation.Category.CONTEXT_ENTITY_NOT_FOUND)
                            .guardrailName(NAME)
                            .detail("Entity not found for domain context key='"
                                    + key + "' value='" + value + "'.")
                            .correlationId(correlationId)
                            .build());
                    log.warn("[ContextIntegrityValidator] entity not found key='{}' value='{}' "
                            + "correlationId={}", key, value, correlationId);
                }
            } catch (Exception e) {
                // Existence check itself failed (DB unavailable, etc.)
                // Treat as a WARN — do not let infrastructure failure silently block AI.
                violations.add(GuardrailViolation.builder()
                        .severity(GuardrailViolation.Severity.WARN)
                        .category(GuardrailViolation.Category.CONTEXT_ENTITY_NOT_FOUND)
                        .guardrailName(NAME)
                        .detail("Existence check for key='" + key + "' value='" + value
                                + "' threw an exception: " + e.getMessage()
                                + ". Proceeding with warning.")
                        .correlationId(correlationId)
                        .build());
                log.warn("[ContextIntegrityValidator] existence port error key='{}' "
                        + "correlationId={}: {}", key, correlationId, e.getMessage());
            }
        }

        return violations;
    }
}