/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowReading
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-26-2026 — H6: Add @Version for optimistic locking
 * 	@UpdatedOn	: 03-26-2026 — fix: add readingSlot, recordedBy, validatedBy, recordedAt
 *                              (fields referenced by workflow, coverage, and mapper layers)
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import dz.sh.trc.hyflo.flow.common.model.DataSource;
import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.workflow.model.WorkflowInstance;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Operational flow reading recorded by a transport operator")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FlowReading")
@Table(name = "T_03_03_05")
public class FlowReading extends GenericModel {

    // ------------------------------------------------------------------
    // F_01..F_09 — Measurement fields
    // ------------------------------------------------------------------

    @Schema(description = "Date of the reading", example = "2026-03-20")
    @Column(name = "F_01", nullable = false)
    private LocalDate readingDate;

    @Schema(description = "Measured flow volume in m³", example = "12500.50")
    @Column(name = "F_02", precision = 18, scale = 4)
    private BigDecimal volumeM3;

    @Schema(description = "Measured flow volume in MSCF", example = "441.45")
    @Column(name = "F_03", precision = 18, scale = 4)
    private BigDecimal volumeMscf;

    @Schema(description = "Inlet pressure in bar", example = "68.5")
    @Column(name = "F_04", precision = 10, scale = 4)
    private BigDecimal inletPressureBar;

    @Schema(description = "Outlet pressure in bar", example = "65.2")
    @Column(name = "F_05", precision = 10, scale = 4)
    private BigDecimal outletPressureBar;

    @Schema(description = "Temperature in Celsius", example = "22.3")
    @Column(name = "F_06", precision = 8, scale = 4)
    private BigDecimal temperatureCelsius;

    @Schema(description = "Operator notes or observations")
    @Column(name = "F_07", length = 1000)
    private String notes;

    @Schema(description = "Timestamp when the record was submitted")
    @Column(name = "F_08")
    private LocalDateTime submittedAt;

    @Schema(description = "Timestamp when the record was validated")
    @Column(name = "F_09")
    private LocalDateTime validatedAt;

    // ------------------------------------------------------------------
    // F_10, F_11 — Core references
    // ------------------------------------------------------------------

    @Schema(description = "FK to pipeline (canonical network/core ownership)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_10", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_05_FK_01"), nullable = false)
    private Pipeline pipeline;

    @Schema(description = "FK to validation status reference (canonical flow/common ownership)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_11", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_05_FK_02"))
    private ValidationStatus validationStatus;

    // ------------------------------------------------------------------
    // F_12 — Workflow binding
    // ------------------------------------------------------------------

    @Schema(
            description = "FK to the workflow instance governing the validation lifecycle. "
                    + "Null when no workflow process has been opened yet.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_12", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_05_FK_03"))
    private WorkflowInstance workflowInstance;

    // ------------------------------------------------------------------
    // F_13 — Data provenance
    // ------------------------------------------------------------------

    @Schema(
            description = "FK to the data source (MANUAL, SCADA, CALCULATED, AI_ASSISTED).",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_13", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_05_FK_04"))
    private DataSource dataSource;

    // ------------------------------------------------------------------
    // F_14 — Optimistic locking (H6)
    // ------------------------------------------------------------------

    @Version
    @Column(name = "F_14")
    private Long version;

    // ------------------------------------------------------------------
    // F_15 — Reading slot (measurement period)
    // ------------------------------------------------------------------

    @Schema(
            description = "FK to the reading slot defining the measurement period (e.g., S1 06:00–12:00). "
                    + "Null when the reading is not tied to a specific shift slot.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_15", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_05_FK_05"))
    private ReadingSlot readingSlot;

    // ------------------------------------------------------------------
    // F_16 — Recorded by (operator who entered the reading)
    // ------------------------------------------------------------------

    @Schema(
            description = "FK to the employee who recorded (entered) this reading.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_16", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_05_FK_06"))
    private Employee recordedBy;

    // ------------------------------------------------------------------
    // F_17 — Validated by (validator/approver who approved or rejected)
    // ------------------------------------------------------------------

    @Schema(
            description = "FK to the employee who approved or rejected this reading.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_17", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_05_FK_07"))
    private Employee validatedBy;

    // ------------------------------------------------------------------
    // F_18 — Recorded at (timestamp when operator entered the reading)
    // ------------------------------------------------------------------

    @Schema(
            description = "Timestamp when the reading was first recorded (entered) by the operator.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_18")
    private LocalDateTime recordedAt;
}
