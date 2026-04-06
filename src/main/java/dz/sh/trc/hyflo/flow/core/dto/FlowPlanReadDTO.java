/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowPlanReadDTO
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : Flow / Core
 *
 *  @Description: Read projection for FlowPlan.
 *                Returned by all query operations.
 *                Contains denormalized labels for display.
 *                All mapping logic lives in FlowPlanMapper.
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.platform.kernel.GenericDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowPlan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Read projection for a management flow plan")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowPlanReadDTO extends GenericDTO<FlowPlan> {

    @Schema(description = "Calendar date covered by this plan", example = "2026-04-01")
    private LocalDate planDate;

    @Schema(description = "Planned volume target in m\u00b3", example = "15000.0000")
    private BigDecimal plannedVolumeM3;

    @Schema(description = "Planned volume target in MSCF", example = "529.7200")
    private BigDecimal plannedVolumeMscf;

    @Schema(description = "Planned inlet pressure target in bar", example = "72.0000")
    private BigDecimal plannedInletPressureBar;

    @Schema(description = "Planned outlet pressure target in bar", example = "68.5000")
    private BigDecimal plannedOutletPressureBar;

    @Schema(description = "Planned operating temperature in Celsius", example = "25.0000")
    private BigDecimal plannedTemperatureCelsius;

    @Schema(description = "Scenario label", example = "BASE")
    private String scenarioCode;

    @Schema(description = "Planning notes or assumptions")
    private String notes;

    // ---- Pipeline ----

    @Schema(description = "ID of the target pipeline")
    private Long pipelineId;

    @Schema(description = "Code of the target pipeline", example = "GZ1-HASSI-ARZEW")
    private String pipelineCode;

    @Schema(description = "Name of the target pipeline", example = "Gazoduc 1")
    private String pipelineName;

    // ---- Status ----

    @Schema(description = "ID of the plan status")
    private Long statusId;

    @Schema(description = "Code of the plan status", example = "DRAFT")
    private String statusCode;

    // ---- Approval ----

    @Schema(description = "Timestamp when the plan was approved")
    private LocalDateTime approvedAt;

    @Schema(description = "ID of the approving employee")
    private Long approvedById;

    @Schema(description = "Full name of the approving employee")
    private String approverName;

    // ---- Submission ----

    @Schema(description = "ID of the employee who entered the plan")
    private Long submittedById;

    @Schema(description = "Full name of the employee who entered the plan")
    private String submitterName;

    // ---- Revision chain ----

    @Schema(description = "ID of the plan this revision supersedes")
    private Long revisedFromId;

    // ---- NO mapping logic ----

    @Override
    public FlowPlan toEntity() {
        throw new UnsupportedOperationException("Use FlowPlanMapper for write operations");
    }

    @Override
    public void updateEntity(FlowPlan entity) {
        throw new UnsupportedOperationException("Use FlowPlanMapper for update operations");
    }
}
