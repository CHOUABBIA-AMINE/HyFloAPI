/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: DerivedFlowReading
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a derived (computed) flow reading on a pipeline segment,
 * calculated from one or more raw {@link FlowReading}s by the intelligence layer.
 *
 * <h3>Time-slot integrity</h3>
 * Each derived reading is scoped to exactly one {@link ReadingSlot} (e.g., SLOT_1
 * 06:00-08:00). The composite unique constraint
 * {@code T_04_01_02_UK_01 (readingDate, readingSlot, pipelineSegment)} ensures
 * the intelligence layer cannot produce duplicate aggregations for the same
 * segment and time window on a given day.
 *
 * <h3>Analytics index</h3>
 * {@code T_04_01_02_IX_01} on the same three columns supports the primary
 * analytical access pattern: time-range queries per segment
 * ({@code WHERE readingDate BETWEEN :start AND :end AND pipelineSegment = :seg}).
 */
@Schema(description = "Computed flow reading derived from raw readings by the analytics engine, scoped to a time slot")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "DerivedFlowReading")
@Table(
        name = "T_04_01_02",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "T_04_01_02_UK_01",
                        columnNames = {"F_01", "F_08", "F_06"}
                )
        },
        indexes = {
                @Index(
                        name = "T_04_01_02_IX_01",
                        columnList = "F_01, F_08, F_06"
                )
        }
)
public class DerivedFlowReading extends GenericModel {

    // ------------------------------------------------------------------
    // F_01 — Date dimension (part of unique key and index)
    // ------------------------------------------------------------------

    @Schema(description = "Date the derived reading covers", example = "2026-03-20")
    @Column(name = "F_01", nullable = false)
    private LocalDate readingDate;

    // ------------------------------------------------------------------
    // F_02..F_05 — Derived measurement fields (unchanged)
    // ------------------------------------------------------------------

    @Schema(description = "Derived average flow volume in m³", example = "12400.00")
    @Column(name = "F_02", precision = 18, scale = 4)
    private BigDecimal avgVolumeM3;

    @Schema(description = "Derived average inlet pressure in bar", example = "67.8")
    @Column(name = "F_03", precision = 10, scale = 4)
    private BigDecimal avgInletPressureBar;

    @Schema(description = "Derived average outlet pressure in bar", example = "65.0")
    @Column(name = "F_04", precision = 10, scale = 4)
    private BigDecimal avgOutletPressureBar;

    @Schema(description = "Name of the computation model used", example = "SEGMENT_AGGREGATION_V1")
    @Column(name = "F_05", length = 100)
    private String computationModel;

    // ------------------------------------------------------------------
    // F_06 — Segment (part of unique key and index, unchanged)
    // ------------------------------------------------------------------

    @Schema(description = "FK to pipeline segment (canonical network/core ownership)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_04_01_02_FK_01"), nullable = false)
    private PipelineSegment pipelineSegment;

    // ------------------------------------------------------------------
    // F_07 — Source raw reading (unchanged)
    // ------------------------------------------------------------------

    @Schema(description = "FK to the source raw FlowReading from which this reading was derived")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_04_01_02_FK_02"))
    private FlowReading sourceReading;

    // ------------------------------------------------------------------
    // F_08 — Time slot (part of unique key and index)
    // ------------------------------------------------------------------

    @Schema(
            description = "FK to the time slot this derived reading covers (e.g., SLOT_1 06:00-08:00). " +
                    "Together with readingDate and pipelineSegment forms the natural key of a derived reading.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_04_01_02_FK_03"), nullable = false)
    private ReadingSlot readingSlot;
}
