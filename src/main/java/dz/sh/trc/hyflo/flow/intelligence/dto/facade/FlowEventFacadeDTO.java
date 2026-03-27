/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowEventFacadeDTO
 *  @CreatedOn  : 03-26-2026 — F1: facade read projection for IFlowEventFacade boundary contract
 *
 *  @Type       : DTO (Read Projection)
 *  @Layer      : DTO / Facade
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Lightweight read projection returned by IFlowEventFacade.
 *                Contains only the fields consumed by flow/intelligence services.
 *                No JPA entity types cross the module boundary.
 *
 *  F1 — replaces direct FlowEvent entity exposure across facade boundary.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto.facade;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Read projection for FlowEvent data consumed by flow/intelligence.
 *
 * Fields selected to exactly match PipelineIntelligenceService usage:
 *   - id, pipelineId, flowReadingId: identity and correlation keys
 *   - eventTimestamp: used in timeline ordering
 *   - severityCode: carried into TimelineItemDTO
 *   - eventTypeCode: optional classification of event type
 *   - description: optional human-readable event description
 *
 * F1: replaces direct FlowEvent entity return from IFlowEventFacade.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowEventFacadeDTO {

    /** Event primary key. */
    private Long id;

    /** Pipeline this event belongs to. */
    private Long pipelineId;

    /** Source FlowReading that produced this event (nullable). */
    private Long flowReadingId;

    /** Timestamp when the event occurred. */
    private LocalDateTime eventTimestamp;

    /**
     * Severity code string (e.g. "CRITICAL", "HIGH", "MEDIUM", "LOW", "INFO").
     * Derived from FlowEvent.severity.code in the facade implementation.
     */
    private String severityCode;

    /**
     * Event type code (e.g. "ANOMALY", "THRESHOLD_BREACH", "SYSTEM").
     * Derived from FlowEvent.eventType.code in the facade implementation.
     */
    private String eventTypeCode;

    /** Optional human-readable description of the event. */
    private String description;
}
