package dz.sh.trc.hyflo.platform.ai.guardrail;

import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Thrown by {@link GuardrailChain} when a BLOCK-severity violation
 * is detected in any guardrail stage.
 *
 * <p>Extends {@link AgentExecutionException} with {@code isTransient=false}:
 * a guardrail block is a deterministic safety decision, not an
 * infrastructure transient failure.</p>
 *
 * <p>Carries the blocking violations so that the
 * {@code GlobalExceptionHandler} can produce a structured error response
 * with the specific guardrail categories that triggered the block,
 * without leaking implementation details to the end user.</p>
 */
public class GuardrailException extends AgentExecutionException {

    private final String                     stage;
    private final List<GuardrailViolation>   blockingViolations;

    public GuardrailException(String correlationId,
                               String stage,
                               List<GuardrailViolation> blockingViolations) {
        super(buildMessage(stage, blockingViolations), correlationId, false);
        this.stage              = stage;
        this.blockingViolations = Collections.unmodifiableList(blockingViolations);
    }

    public String                   getStage()              { return stage; }
    public List<GuardrailViolation> getBlockingViolations() { return blockingViolations; }

    private static String buildMessage(String stage,
                                        List<GuardrailViolation> violations) {
        return "GuardrailChain BLOCK at stage='" + stage + "': "
                + violations.stream()
                            .map(v -> v.category() + " — " + v.detail())
                            .collect(Collectors.joining("; "));
    }
}