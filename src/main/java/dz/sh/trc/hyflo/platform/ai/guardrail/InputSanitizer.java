package dz.sh.trc.hyflo.platform.ai.guardrail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Sanitizes raw user or system input before it reaches the LLM.
 *
 * <h3>Checks performed (in order):</h3>
 * <ol>
 *   <li><strong>Length guard</strong> — rejects input exceeding
 *       {@code maxInputChars} (default 8 000, configurable via
 *       {@code hyflo.ai.guardrail.max-input-chars}).</li>
 *   <li><strong>Encoding hygiene</strong> — logs a WARN when non-UTF-8
 *       compatible characters are detected (rare in Spring Boot apps but
 *       possible with malformed multipart data).</li>
 *   <li><strong>Prompt injection patterns</strong> — detects known prompt
 *       injection templates (e.g. "Ignore previous instructions",
 *       "Disregard your system prompt") and produces a BLOCK violation.</li>
 * </ol>
 *
 * <h3>Design rule:</h3>
 * <p>This class performs text hygiene only. It does NOT redact PII
 * (that is {@link PiiRedactor}) and does NOT validate domain entity
 * references (that is {@link ContextIntegrityValidator}).</p>
 */
@Component
@ConfigurationProperties(prefix = "hyflo.ai.guardrail")
public class InputSanitizer {

    private static final Logger log = LoggerFactory.getLogger(InputSanitizer.class);

    static final String NAME = "InputSanitizer";

    // -------------------------------------------------------------------------
    // Injection detection patterns
    // -------------------------------------------------------------------------

    private static final List<Pattern> INJECTION_PATTERNS = List.of(
            Pattern.compile("(?i)ignore\\s+(previous|prior|all|the)\\s+(instructions?|prompt|context)"),
            Pattern.compile("(?i)disregard\\s+(your|the|all)\\s+(system\\s+prompt|instructions?|context)"),
            Pattern.compile("(?i)you\\s+are\\s+now\\s+(a|an)\\s+\\w+"),
            Pattern.compile("(?i)act\\s+as\\s+(if\\s+you\\s+are|a|an)"),
            Pattern.compile("(?i)jailbreak"),
            Pattern.compile("(?i)DAN\\s+mode"),
            Pattern.compile("(?i)\\[SYSTEM\\]\\s*:"),
            Pattern.compile("(?i)<\\s*system\\s*>"),
            Pattern.compile("(?i)pretend\\s+(you\\s+are|to\\s+be)\\s+(not|an?\\s+AI|unrestricted)")
    );

    // -------------------------------------------------------------------------
    // Configurable properties
    // -------------------------------------------------------------------------

    private int maxInputChars = 8_000;

    public int  getMaxInputChars()       { return maxInputChars; }
    public void setMaxInputChars(int v)  { this.maxInputChars = v; }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Runs all sanitization checks on {@code rawInput}.
     *
     * @param rawInput      the user-supplied text (never modified in-place)
     * @param correlationId for violation tracing
     * @return a list of {@link GuardrailViolation}s (empty = clean)
     */
    public List<GuardrailViolation> sanitize(String rawInput, String correlationId) {
        List<GuardrailViolation> violations = new ArrayList<>();

        if (rawInput == null || rawInput.isBlank()) {
            violations.add(GuardrailViolation.builder()
                    .severity(GuardrailViolation.Severity.BLOCK)
                    .category(GuardrailViolation.Category.INPUT_TOO_LONG) // reuse closest category
                    .guardrailName(NAME)
                    .detail("Input is null or blank.")
                    .correlationId(correlationId)
                    .build());
            return violations;
        }

        // Check 1: Length
        if (rawInput.length() > maxInputChars) {
            violations.add(GuardrailViolation.builder()
                    .severity(GuardrailViolation.Severity.BLOCK)
                    .category(GuardrailViolation.Category.INPUT_TOO_LONG)
                    .guardrailName(NAME)
                    .detail("Input length " + rawInput.length()
                            + " exceeds maximum of " + maxInputChars + " characters.")
                    .correlationId(correlationId)
                    .build());
        }

        // Check 2: Encoding anomaly (non-UTF-8)
        byte[] bytes = rawInput.getBytes(StandardCharsets.UTF_8);
        String reDecoded = new String(bytes, StandardCharsets.UTF_8);
        if (!rawInput.equals(reDecoded)) {
            violations.add(GuardrailViolation.builder()
                    .severity(GuardrailViolation.Severity.WARN)
                    .category(GuardrailViolation.Category.INPUT_ENCODING_ANOMALY)
                    .guardrailName(NAME)
                    .detail("Input contains characters that do not round-trip through UTF-8 cleanly.")
                    .correlationId(correlationId)
                    .build());
            log.warn("[InputSanitizer] encoding anomaly detected correlationId={}", correlationId);
        }

        // Check 3: Prompt injection patterns
        for (Pattern p : INJECTION_PATTERNS) {
            if (p.matcher(rawInput).find()) {
                violations.add(GuardrailViolation.builder()
                        .severity(GuardrailViolation.Severity.BLOCK)
                        .category(GuardrailViolation.Category.INPUT_INJECTION_DETECTED)
                        .guardrailName(NAME)
                        .detail("Prompt injection pattern detected: " + p.pattern())
                        .correlationId(correlationId)
                        .build());
                log.warn("[InputSanitizer] prompt injection detected correlationId={} pattern={}",
                        correlationId, p.pattern());
                break; // one injection detection is enough to BLOCK
            }
        }

        return violations;
    }
}