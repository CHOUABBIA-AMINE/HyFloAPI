/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowPlanCommandDTO
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Command
 *  @Package    : Flow / Core
 *
 *  @Description: Write input DTO for FlowPlan create and update operations.
 *                Uses IDs for all FK references.
 *                Bean Validation enforced on mandatory fields.
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto.command;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "Command DTO for creating or updating a flow plan")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowPlanCommandDTO {

    // ---- Mandatory fields ----

    @Schema(description = "Calendar date this plan covers", example = "2026-04-01",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "planDate is required")
    private LocalDate planDate;

    @Schema(description = "Planned volume target in m\u00b3", example = "15000.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "plannedVolumeM3 is required")
    @Positive(message = "plannedVolumeM3 must be positive")
    private BigDecimal plannedVolumeM3;

    @Schema(description = "Planned volume target in MSCF", example = "529.72",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "plannedVolumeMscf is required")
    @Positive(message = "plannedVolumeMscf must be positive")
    private BigDecimal plannedVolumeMscf;

    @Schema(description = "Planned inlet pressure target in bar", example = "72.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "plannedInletPressureBar is required")
    @Positive(message = "plannedInletPressureBar must be positive")
    private BigDecimal plannedInletPressureBar;

    @Schema(description = "Pipeline ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "pipelineId is required")
    private Long pipelineId;

    @Schema(description = "PlanStatus ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "statusId is required")
    private Long statusId;

    // ---- Optional fields ----

    @Schema(description = "Planned outlet pressure in bar", example = "68.5")
    @Positive(message = "plannedOutletPressureBar must be positive")
    private BigDecimal plannedOutletPressureBar;

    @Schema(description = "Planned temperature in Celsius", example = "25.0")
    private BigDecimal plannedTemperatureCelsius;

    @Schema(description = "Scenario code: BASE, OPTIMISTIC, CONSERVATIVE", example = "BASE",
            maxLength = 50)
    @Size(max = 50, message = "scenarioCode must not exceed 50 characters")
    private String scenarioCode;

    @Schema(description = "Planning notes or assumptions", maxLength = 1000)
    @Size(max = 1000, message = "notes must not exceed 1000 characters")
    private String notes;

    @Schema(description = "ID of the employee submitting the plan")
    private Long submittedByEmployeeId;

    @Schema(description = "ID of the plan this revises (self-reference)")
    private Long revisedFromId;
}
