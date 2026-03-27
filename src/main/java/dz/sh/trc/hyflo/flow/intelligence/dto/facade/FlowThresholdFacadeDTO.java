/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdFacadeDTO
 *  @CreatedOn  : 03-26-2026 — F1: facade read projection for IFlowThresholdFacade boundary contract
 *
 *  @Type       : DTO (Read Projection)
 *  @Layer      : DTO / Facade
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Lightweight read projection returned by IFlowThresholdFacade.
 *                Contains only the fields consumed by flow/intelligence services.
 *                No JPA entity types cross the module boundary.
 *
 *  F1 — replaces direct FlowThreshold entity exposure across facade boundary.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto.facade;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Read projection for FlowThreshold data consumed by flow/intelligence.
 *
 * Fields selected to exactly match PipelineIntelligenceService usage:
 *   - id: identity key
 *   - pipelineId: owning pipeline
 *   - metricCode: the metric being threshold-guarded (e.g. "PRESSURE", "FLOW_RATE")
 *   - operator: comparison operator code (e.g. "GT", "LT", "GTE", "LTE")
 *   - thresholdValue: the numeric boundary value
 *   - active: whether this threshold is currently enforced
 *
 * F1: replaces direct FlowThreshold entity return from IFlowThresholdFacade.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowThresholdFacadeDTO {

    /** Threshold primary key. */
    private Long id;

    /** Pipeline this threshold is configured for. */
    private Long pipelineId;

    /**
     * Metric code being guarded (e.g. "PRESSURE", "FLOW_RATE", "TEMPERATURE").
     * Derived from FlowThreshold.metric.code in the facade implementation.
     */
    private String metricCode;

    /**
     * Comparison operator code (e.g. "GT", "GTE", "LT", "LTE", "EQ").
     * Derived from FlowThreshold.operator.code in the facade implementation.
     */
    private String operator;

    /** The numeric boundary value for this threshold. */
    private BigDecimal thresholdValue;

    /** Whether this threshold is currently active and enforced. */
    private boolean active;
}
