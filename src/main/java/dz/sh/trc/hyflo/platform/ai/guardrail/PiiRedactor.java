package dz.sh.trc.hyflo.platform.ai.guardrail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Domain-aware PII redactor applied to input text before LLM exposure.
 *
 * <h3>Redaction targets:</h3>
 * <ul>
 *   <li>Email addresses → {@code [REDACTED-EMAIL]}</li>
 *   <li>Algerian and international phone numbers → {@code [REDACTED-PHONE]}</li>
 *   <li>National ID card numbers (DZ format: 18-digit) → {@code [REDACTED-NID]}</li>
 *   <li>GPS coordinates (lat/lon pairs) → {@code [REDACTED-COORDS]}</li>
 *   <li>Numeric personal identifiers embedded in
 *       "Identifiant:", "N° dossier:", "matricule:" prefixes
 *       → {@code [REDACTED-ID]}</li>
 * </ul>
 *
 * <h3>Design rules:</h3>
 * <ul>
 *   <li>Returns a <em>new string</em> — never modifies the input in-place.</li>
 *   <li>Produces {@link GuardrailViolation.Severity#INFO} violations (not BLOCK/WARN)
 *       for each redaction applied — purely informational for audit.</li>
 *   <li>Does NOT classify text as harmful — that is {@link OutputContentFilter}.</li>
 *   <li>Does NOT validate entity existence — that is {@link ContextIntegrityValidator}.</li>
 * </ul>
 *
 * <h3>Hydrocarbon domain note:</h3>
 * <p>In HyFlo, PII primarily enters through free-text fields in flow reports,
 * incident descriptions, and operator notes. Coordinates of wellheads and
 * station sites are flagged for selective redaction when they appear in
 * conversational input but allowed in structured tool outputs
 * (see {@link ToolOutputValidator} for the distinction).</p>
 */
@Component
public class PiiRedactor {

    private static final Logger log = LoggerFactory.getLogger(PiiRedactor.class);

    static final String NAME = "PiiRedactor";

    // -------------------------------------------------------------------------
    // PII patterns
    // -------------------------------------------------------------------------

    private record RedactionRule(Pattern pattern, String replacement,
                                  GuardrailViolation.Category category) {}

    private static final List<RedactionRule> RULES = List.of(
            new RedactionRule(
                    Pattern.compile("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}"),
                    "[REDACTED-EMAIL]",
                    GuardrailViolation.Category.PII_REDACTED),
            new RedactionRule(
                    Pattern.compile("(?:(?:\\+213|0)(5|6|7)\\d{8}|0[2-9]\\d{7})"),
                    "[REDACTED-PHONE]",
                    GuardrailViolation.Category.PII_REDACTED),
            new RedactionRule(
                    // Algerian NID: 18 digits (Numéro d'identification nationale)
                    Pattern.compile("\\b\\d{18}\\b"),
                    "[REDACTED-NID]",
                    GuardrailViolation.Category.PII_REDACTED),
            new RedactionRule(
                    // GPS: lat lon pairs like "36.7538, 3.0588" or "36.7538 N 3.0588 E"
                    Pattern.compile("-?\\d{1,3}\\.\\d{4,}[,\\s]+(?:[NS][,\\s]+)?-?\\d{1,3}\\.\\d{4,}\\s*[EW]?"),
                    "[REDACTED-COORDS]",
                    GuardrailViolation.Category.PII_REDACTED),
            new RedactionRule(
                    // "Identifiant : 1234567" / "N° dossier: 9876543" / "matricule: AB1234"
                    Pattern.compile("(?i)(?:identifiant|n°\\s*dossier|matricule)\\s*:?\\s*[A-Za-z0-9\\-]+"),
                    "[REDACTED-ID]",
                    GuardrailViolation.Category.PII_REDACTED)
    );

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Applies all PII redaction rules to {@code text}.
     *
     * @param text          the input text (may already be partially sanitized)
     * @param correlationId for violation tracing
     * @return a {@link RedactionResult} containing the redacted text and
     *         any INFO violations for audit purposes
     */
    public RedactionResult redact(String text, String correlationId) {
        if (text == null || text.isBlank()) {
            return new RedactionResult(text, List.of());
        }

        List<GuardrailViolation> violations = new ArrayList<>();
        String current = text;

        for (RedactionRule rule : RULES) {
            Matcher matcher = rule.pattern().matcher(current);
            if (matcher.find()) {
                String redacted = matcher.replaceAll(rule.replacement());
                violations.add(GuardrailViolation.builder()
                        .severity(GuardrailViolation.Severity.INFO)
                        .category(rule.category())
                        .guardrailName(NAME)
                        .detail("PII redacted with '" + rule.replacement()
                                + "' using pattern: " + rule.pattern().pattern())
                        .correlationId(correlationId)
                        .build());
                log.debug("[PiiRedactor] redacted PII correlationId={} replacement={}",
                        correlationId, rule.replacement());
                current = redacted;
            }
        }

        return new RedactionResult(current, violations);
    }

    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------

    /**
     * Carrier for the redacted text and the list of INFO violations produced.
     */
    public record RedactionResult(
            String                   redactedText,
            List<GuardrailViolation> violations
    ) {}
}