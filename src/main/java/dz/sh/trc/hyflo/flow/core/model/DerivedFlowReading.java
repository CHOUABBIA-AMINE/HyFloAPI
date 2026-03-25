/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: DerivedFlowReading
 * 	@CreatedOn	: 03-25-2026
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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a derived (computed) flow reading on a pipeline segment,
 * calculated from one or more raw FlowReadings by the intelligence layer.
 */
@Schema(description = "Computed flow reading derived from raw readings by the analytics engine")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "DerivedFlowReading")
@Table(name = "T_04_01_02")
public class DerivedFlowReading extends GenericModel {

    @Schema(description = "Date the derived reading covers", example = "2026-03-20")
    @Column(name = "F_01", nullable = false)
    private LocalDate readingDate;

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

    @Schema(description = "FK to pipeline segment")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_04_01_02_FK_01"), nullable = false)
    private PipelineSegment pipelineSegment;

    @Schema(description = "FK to the source raw reading")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_04_01_02_FK_02"))
    private FlowReading sourceReading;
}
