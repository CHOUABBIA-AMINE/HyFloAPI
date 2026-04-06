/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertFacadeDTO
 *  @CreatedOn  : 03-26-2026 — F1: facade read projection for IFlowAlertFacade boundary contract
 *
 *  @Type       : DTO (Read Projection)
 *  @Layer      : DTO / Facade
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Lightweight read projection returned by IFlowAlertFacade.
 *                Contains only the fields consumed by flow/intelligence services.
 *                No JPA entity types cross the module boundary.
 *
 *  F1 — replaces direct FlowAlert entity exposure across facade boundary.
 *
 **/

package dz.sh.trc.hyflo.intelligence.dto.facade;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Read projection for FlowAlert data consumed by flow/intelligence.
 *
 * Fields selected to exactly match PipelineIntelligenceService usage:
 *   - id, pipelineId, thresholdId, flowReadingId: identity and correlation keys
 *   - alertTimestamp: used in timeline ordering
 *   - severityCode: used in health status computation (CRITICAL check)
 *   - statusCode: used in unresolved alert filtering
 *   - message: optional human-readable alert description
 *
 * F1: replaces direct FlowAlert entity return from IFlowAlertFacade.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowAlertFacadeDTO {

    /** Alert primary key. */
    private Long id;

    /** Pipeline this alert belongs to. */
    private Long pipelineId;

    /** Threshold that triggered this alert (nullable). */
    private Long thresholdId;

    /** Source FlowReading that triggered this alert (nullable). */
    private Long flowReadingId;

    /** Timestamp when the alert was raised. */
    private LocalDateTime alertTimestamp;

    /**
     * Severity code string (e.g. "CRITICAL", "HIGH", "MEDIUM", "LOW").
     * Derived from FlowAlert.severity.code in the facade implementation.
     */
    private String severityCode;

    /**
     * Status code string (e.g. "OPEN", "ACKNOWLEDGED", "RESOLVED").
     * Derived from FlowAlert.alertStatus.code in the facade implementation.
     */
    private String statusCode;

    /** Optional human-readable alert message. */
    private String message;
}
