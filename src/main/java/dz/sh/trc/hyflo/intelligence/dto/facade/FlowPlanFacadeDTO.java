/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowPlanFacadeDTO
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : DTO (Facade Read Projection)
 *  @Layer      : DTO / Facade
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Lightweight read projection for FlowPlan data consumed by
 *                flow.intelligence services (PlanComparisonService, etc.).
 *                No JPA entity types cross the facade boundary.
 *                Owned by flow.intelligence because it is the consumer.
 *
 **/

package dz.sh.trc.hyflo.intelligence.dto.facade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Facade read projection for FlowPlan consumed by flow.intelligence.
 * Fields selected to cover plan-vs-actual and plan-vs-forecast comparison needs.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowPlanFacadeDTO {

    private Long      id;
    private Long      pipelineId;
    private LocalDate planDate;
    private String    scenarioCode;
    private BigDecimal plannedVolumeM3;
    private BigDecimal plannedVolumeMscf;
    private BigDecimal plannedInletPressureBar;
    private BigDecimal plannedOutletPressureBar;
    private BigDecimal plannedTemperatureCelsius;
    private String    statusCode;
    private LocalDateTime approvedAt;
}
