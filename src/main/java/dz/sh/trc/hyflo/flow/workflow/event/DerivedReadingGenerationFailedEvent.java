/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DerivedReadingGenerationFailedEvent
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Event
 *  @Layer      : Event
 *  @Package    : Flow / Workflow / Event
 *
 *  @Description: Published when async derived reading generation fails
 *                for an approved FlowReading.
 *
 *                The approval remains valid — this event is ONLY a
 *                notification of failed side-effect generation.
 *                Downstream handlers should alert operators or schedule retry.
 *
 *  H5 — Resilience: failure event for derived reading generation.
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.event;

import java.time.LocalDateTime;

import lombok.Getter;

/**
 * Event published when async derived reading generation fails.
 *
 * Published inside SegmentDistributionServiceImpl.asyncGenerateDerivedReadings()
 * exceptionally() handler via ApplicationEventPublisher.
 *
 * The FlowReading approval is NOT rolled back.
 * Handlers should notify operators or trigger manual regeneration.
 */
@Getter
public class DerivedReadingGenerationFailedEvent {

    /** The ID of the approved FlowReading whose derived readings were not generated. */
    private final Long sourceReadingId;

    /** The pipeline ID associated with the reading. */
    private final Long pipelineId;

    /** The exception message describing the failure cause. */
    private final String failureMessage;

    /** When the failure was detected. */
    private final LocalDateTime failedAt;

    private DerivedReadingGenerationFailedEvent(
            Long sourceReadingId,
            Long pipelineId,
            String failureMessage,
            LocalDateTime failedAt) {
        this.sourceReadingId = sourceReadingId;
        this.pipelineId      = pipelineId;
        this.failureMessage  = failureMessage;
        this.failedAt        = failedAt;
    }

    /**
     * Factory method matching the static-factory pattern used by
     * ReadingValidatedEvent and ReadingRejectedEvent.
     *
     * @param sourceReadingId ID of the FlowReading whose generation failed
     * @param pipelineId      Pipeline ID (may be null if unavailable at failure point)
     * @param failureMessage  Exception message or summary
     * @return new DerivedReadingGenerationFailedEvent
     */
    public static DerivedReadingGenerationFailedEvent create(
            Long sourceReadingId,
            Long pipelineId,
            String failureMessage) {
        return new DerivedReadingGenerationFailedEvent(
                sourceReadingId,
                pipelineId,
                failureMessage,
                LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "DerivedReadingGenerationFailedEvent{" +
               "sourceReadingId=" + sourceReadingId +
               ", pipelineId=" + pipelineId +
               ", failureMessage='" + failureMessage + "'" +
               ", failedAt=" + failedAt +
               '}';
    }
}
