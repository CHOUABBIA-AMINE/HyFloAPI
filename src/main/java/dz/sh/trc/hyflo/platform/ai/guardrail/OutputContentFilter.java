package dz.sh.trc.hyflo.platform.ai.guardrail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Filters and flags generated model output before it is returned to callers.
 *
 * <h3>Checks performed:</h3>
 * <ol>
 *   <li><strong>Empty output guard</strong> — blank model responses are
 *       BLOCKED (prevents cascading failures in downstream parsers).</li>
 *   <li><strong>Sensitive content patterns</strong> — detects accidental
 *       leakage of internal prompts, API keys, or debug tokens in model
 *       output and produces a BLOCK.</li>
 *   <li><strong>Hallucination signals</strong> — detects specific patterns
 *       that indicate the model is fabricating pipeline/station/network
 *       references (e.g. references to IDs with known prefix patterns that
 *       do not match HyFlo's actual ID format). Produces a WARN — not a
 *       BLOCK, to avoid over-suppression of legitimate responses.</li>
 * </ol>
 *
 * <h3>Design rule:</h3>
 * <p>This class does NOT parse structured JSON — that is
 * {@link dz.sh.trc.hyflo.platform.ai.format.JsonSchemaValidator}.
 * It operates on the raw model response string and applies pattern-based
 * heuristics only. It is intentionally lightweight and fast.</p>
 *
 * <h3>HyFlo domain note:</h3>
 * <p>Hallucination signals are tuned to the HyFlo domain:
 * pipeline IDs follow the pattern {@code PIPE-[0-9]+}, station IDs
 * {@code ST-[0-9]+}, network IDs {@code NET-[0-9]+}, and alert IDs
 * {@code ALT-[0-9]+}. A model generating IDs like {@code PIPELINE_123}
 * or {@code station_A} is hallucinating non-canonical identifiers and
 * should be flagged.</p>
 */
@Component
public class OutputContentFilter {

    private static final Logger log = LoggerFactory.getLogger(OutputContentFilter.class);

    static final String NAME = "OutputContentFilter";

    // -------------------------------------------------------------------------
    // Sensitive content patterns (BLOCK)
    // -------------------------------------------------------------------------

    private static final List<Pattern> SENSITIVE_PATTERNS = List.of(
            // Possible system prompt leakage
            Pattern.compile("(?i)system\\s*prompt\\s*:\\s*\\["),
            Pattern.compile("(?i)<\\s*/?system\\s*>"),
            // Possible API key / token patterns
            Pattern.compile("(?i)(api[_-]?key|bearer\\s+token|Authorization)\\s*[:=]\\s*[A-Za-z0-9\\-_.]{20,}"),
            // Internal debug traces
            Pattern.compile("(?i)\\[DEBUG\\].*hyflo"),
            Pattern.compile("(?i)stack\\s*trace:.*at\\s+dz\\.sh\\.trc")
    );

    // -------------------------------------------------------------------------
    // Hallucination signal patterns (WARN)
    // -------------------------------------------------------------------------

    private static final List<Pattern> HALLUCINATION_SIGNALS = List.of(
            // Non-canonical HyFlo entity ID formats
            Pattern.compile("\\bPIPELINE_\\d+\\b"),
            Pattern.compile("\\bstation_[A-Za-z]\\b"),
            Pattern.compile("\\bNETWORK-[a-z]+\\b"),
            Pattern.compile("\\bALERT_\\d+\\b"),
            // Fabricated "example" data markers
            Pattern.compile("(?i)\\b(example|placeholder|dummy|fake|fictional)\\s+(pipeline|station|network|alert)"),
            // Confidence-hedging hallucination phrases specific to LLMs
            Pattern.compile("(?i)I\\s+(don'?t|do\\s+not)\\s+(have|possess)\\s+access\\s+to\\s+(real|actual|live|current)"),
            Pattern.compile("(?i)as\\s+of\\s+my\\s+(last|knowledge)\\s+(cutoff|update|training)")
    );

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Filters the model's raw output string.
     *
     * @param modelOutput   the raw LLM response text
     * @param correlationId for violation tracing
     * @return a list of {@link GuardrailViolation}s (empty = output is clean)
     */
    public List<GuardrailViolation> filter(String modelOutput, String correlationId) {
        List<GuardrailViolation> violations = new ArrayList<>();

        // Check 1: Empty guard
        if (modelOutput == null || modelOutput.isBlank()) {
            violations.add(GuardrailViolation.builder()
                    .severity(GuardrailViolation.Severity.BLOCK)
                    .category(GuardrailViolation.Category.OUTPUT_EMPTY)
                    .guardrailName(NAME)
                    .detail("Model produced a null or blank output.")
                    .correlationId(correlationId)
                    .build());
            return violations;
        }

        // Check 2: Sensitive content
        for (Pattern p : SENSITIVE_PATTERNS) {
            if (p.matcher(modelOutput).find()) {
                violations.add(GuardrailViolation.builder()
                        .severity(GuardrailViolation.Severity.BLOCK)
                        .category(GuardrailViolation.Category.OUTPUT_SENSITIVE_CONTENT)
                        .guardrailName(NAME)
                        .detail("Sensitive content pattern detected in model output: "
                                + p.pattern())
                        .correlationId(correlationId)
                        .build());
                log.warn("[OutputContentFilter] sensitive content detected correlationId={} "
                        + "pattern={}", correlationId, p.pattern());
                break; // one BLOCK is sufficient
            }
        }

        // Check 3: Hallucination signals
        for (Pattern p : HALLUCINATION_SIGNALS) {
            if (p.matcher(modelOutput).find()) {
                violations.add(GuardrailViolation.builder()
                        .severity(GuardrailViolation.Severity.WARN)
                        .category(GuardrailViolation.Category.OUTPUT_HALLUCINATION_SIGNAL)
                        .guardrailName(NAME)
                        .detail("Hallucination signal detected in model output: "
                                + p.pattern())
                        .correlationId(correlationId)
                        .build());
                log.warn("[OutputContentFilter] hallucination signal detected correlationId={} "
                        + "pattern={}", correlationId, p.pattern());
            }
        }

        return violations;
    }
}