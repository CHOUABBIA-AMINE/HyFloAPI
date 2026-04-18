/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowPlan
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Model
 *  @Package    : Flow / Planning
 *
 *  @Description: Management planning intent for a pipeline on a given date.
 *                Represents officially entered target values — NOT predictions.
 *                FlowForecast (flow.intelligence) handles system predictions.
 *
 *  Granularity: one row = one pipeline + one calendar date + one scenario.
 *  Unique constraint: (pipeline_id, planDate, scenarioCode).
 *
 **/

package dz.sh.trc.hyflo.core.flow.planning.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.core.flow.reference.model.PlanStatus;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.core.network.topology.model.Pipeline;
import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Management-entered operational planning targets for a pipeline")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FlowPlan")
@Table(name = "T_03_03_02", uniqueConstraints = {@UniqueConstraint(name = "T_03_03_02_UK_01", columnNames = {"F_05", "F_01", "F_09"})})
public class FlowPlan extends GenericModel {

    // ------------------------------------------------------------------
    // F_01 — Planning date (mandatory)
    // ------------------------------------------------------------------

    @Schema(
    	description = "Calendar date this plan covers", example = "2026-04-01",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_01", nullable = false)
    private LocalDate planDate;

    // ------------------------------------------------------------------
    // F_02..F_04 — Mandatory planned measurement targets
    // ------------------------------------------------------------------

    @Schema(description = "Planned volume target in m\u00b3", example = "15000.0000",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_02", precision = 18, scale = 4, nullable = false)
    private BigDecimal plannedVolumeM3;

    @Schema(description = "Planned volume target in MSCF", example = "529.7200",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_03", precision = 18, scale = 4, nullable = false)
    private BigDecimal plannedVolumeMscf;

    @Schema(description = "Planned inlet pressure target in bar", example = "72.0000",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_04", precision = 10, scale = 4, nullable = false)
    private BigDecimal plannedInletPressureBar;

    // ------------------------------------------------------------------
    // F_05 — Pipeline FK (mandatory)
    // ------------------------------------------------------------------

    @Schema(description = "Pipeline this plan targets",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_02_FK_01"), nullable = false)
    private Pipeline pipeline;

    // ------------------------------------------------------------------
    // F_06 — PlanStatus FK (mandatory)
    // ------------------------------------------------------------------

    @Schema(description = "Lifecycle status: DRAFT, APPROVED, SUPERSEDED",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_02_FK_02"), nullable = false)
    private PlanStatus status;

    // ------------------------------------------------------------------
    // F_07..F_08 — Optional planned targets
    // ------------------------------------------------------------------

    @Schema(description = "Planned outlet pressure target in bar", example = "68.5000")
    @Column(name = "F_07", precision = 10, scale = 4)
    private BigDecimal plannedOutletPressureBar;

    @Schema(description = "Planned operating temperature in Celsius", example = "25.0000")
    @Column(name = "F_08", precision = 8, scale = 4)
    private BigDecimal plannedTemperatureCelsius;

    // ------------------------------------------------------------------
    // F_09 — Scenario code (optional, part of unique key)
    // ------------------------------------------------------------------

    @Schema(description = "Scenario label: BASE, OPTIMISTIC, CONSERVATIVE. Null treated as BASE.",
            example = "BASE", maxLength = 50)
    @Column(name = "F_09", length = 50)
    private String scenarioCode;

    // ------------------------------------------------------------------
    // F_10 — Planning notes (optional)
    // ------------------------------------------------------------------

    @Schema(description = "Planning assumptions, justification, or notes", maxLength = 1000)
    @Column(name = "F_10", length = 1000)
    private String notes;

    // ------------------------------------------------------------------
    // F_11 — Approval timestamp (optional, set on approve)
    // ------------------------------------------------------------------

    @Schema(description = "Timestamp when this plan was approved")
    @Column(name = "F_11")
    private LocalDateTime approvedAt;

    // ------------------------------------------------------------------
    // F_12 — Approved by (optional, set on approve)
    // ------------------------------------------------------------------

    @Schema(description = "Employee who approved this plan")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_12", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_02_FK_03"))
    private Employee approvedBy;

    // ------------------------------------------------------------------
    // F_13 — Submitted by (optional)
    // ------------------------------------------------------------------

    @Schema(description = "Employee who entered this plan")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_13", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_02_FK_04"))
    private Employee submittedBy;

    // ------------------------------------------------------------------
    // F_14 — Revised from (self-reference, optional)
    // ------------------------------------------------------------------

    @Schema(description = "Plan this revision supersedes (self-reference)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_14", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_02_FK_05"))
    private FlowPlan revisedFrom;

    // ------------------------------------------------------------------
    // F_15 — Optimistic locking
    // ------------------------------------------------------------------

    @Version
    @Column(name = "F_15")
    private Long version;
}
