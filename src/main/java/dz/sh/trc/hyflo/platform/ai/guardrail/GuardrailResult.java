package dz.sh.trc.hyflo.platform.ai.guardrail;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Aggregated result of a complete guardrail chain pass.
 *
 * <p>A {@code GuardrailResult} is produced by {@link GuardrailChain} after
 * running all applicable guardrails for either an input or output pass.
 * It carries:</p>
 * <ul>
 *   <li>The final decision: {@link Outcome#PASS}, {@link Outcome#WARN},
 *       or {@link Outcome#BLOCK}.</li>
 *   <li>All collected {@link GuardrailViolation}s (regardless of severity).</li>
 *   <li>The (optionally mutated) text — e.g. the sanitized or PII-redacted
 *       version of the input, or the filtered version of the output.</li>
 * </ul>
 *
 * <h3>Outcome decision rules:</h3>
 * <ul>
 *   <li>Any {@link GuardrailViolation.Severity#BLOCK} violation → {@link Outcome#BLOCK}</li>
 *   <li>Any {@link GuardrailViolation.Severity#WARN} violation (no BLOCK) → {@link Outcome#WARN}</li>
 *   <li>Only {@link GuardrailViolation.Severity#INFO} or no violations → {@link Outcome#PASS}</li>
 * </ul>
 */
public final class GuardrailResult {

    public enum Outcome { PASS, WARN, BLOCK }

    private final Outcome                outcome;
    private final List<GuardrailViolation> violations;
    private final String                 processedText;

    private GuardrailResult(Outcome outcome,
                             List<GuardrailViolation> violations,
                             String processedText) {
        this.outcome       = Objects.requireNonNull(outcome, "outcome");
        this.violations    = Collections.unmodifiableList(violations);
        this.processedText = processedText;
    }

    // -------------------------------------------------------------------------
    // Factory methods
    // -------------------------------------------------------------------------

    public static GuardrailResult pass(String processedText) {
        return new GuardrailResult(Outcome.PASS, List.of(), processedText);
    }

    public static GuardrailResult of(List<GuardrailViolation> violations,
                                      String processedText) {
        Objects.requireNonNull(violations, "violations");

        Outcome outcome = violations.stream()
                .map(GuardrailViolation::severity)
                .reduce(Outcome.PASS, (current, sev) -> switch (sev) {
                    case BLOCK -> Outcome.BLOCK;
                    case WARN  -> current == Outcome.BLOCK ? Outcome.BLOCK : Outcome.WARN;
                    case INFO  -> current;
                }, (a, b) -> a == Outcome.BLOCK || b == Outcome.BLOCK ? Outcome.BLOCK
                           : a == Outcome.WARN  || b == Outcome.WARN  ? Outcome.WARN
                           : Outcome.PASS);

        return new GuardrailResult(outcome, violations, processedText);
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public Outcome                   outcome()       { return outcome; }
    public List<GuardrailViolation>  violations()    { return violations; }
    public Optional<String>          processedText() { return Optional.ofNullable(processedText); }

    public boolean isBlocked() { return outcome == Outcome.BLOCK; }
    public boolean hasWarnings() {
        return violations.stream()
                .anyMatch(v -> v.severity() == GuardrailViolation.Severity.WARN);
    }

    @Override
    public String toString() {
        return "GuardrailResult{outcome=" + outcome
                + ", violations=" + violations.size() + "}";
    }
}