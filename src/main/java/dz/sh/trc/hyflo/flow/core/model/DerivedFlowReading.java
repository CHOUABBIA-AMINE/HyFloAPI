/**
 * 
 * 	@Author		: HyFlo v2 Model
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
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.common.model.QualityFlag;
import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.model.DataSource;
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
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * System-generated distributed flow reading anchored to pipeline segments.
 * Derived from one or more direct FlowReading instances for intelligence and analytics.
 */
@Schema(description = "System-generated distributed flow reading anchored to pipeline segments")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "DerivedFlowReading")
@Table(
        name = "T_03_03_08",
        indexes = {
                @Index(name = "T_03_03_08_IX_01", columnList = "F_01"),
                @Index(name = "T_03_03_08_IX_02", columnList = "F_08")
        },
        uniqueConstraints = {
                // Unique per date, slot, and pipeline segment
                @UniqueConstraint(name = "T_03_03_08_UK_01", columnNames = {"F_01", "F_13", "F_08"})
        }
)
public class DerivedFlowReading extends GenericModel {

    @Schema(
            description = "Reference date of this derived reading",
            example = "2026-01-27",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @PastOrPresent(message = "Derived reading date cannot be in the future")
    @Column(name = "F_01", nullable = false)
    private LocalDate readingDate;

    @Schema(
            description = "Derived pressure in bar",
            example = "85.50",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            minimum = "0.0",
            maximum = "500.0"
    )
    @DecimalMin(value = "0.0", message = "Pressure cannot be negative")
    @DecimalMax(value = "500.0", message = "Pressure exceeds maximum safe operating limit (500 bar)")
    @Column(name = "F_02", precision = 8, scale = 4)
    private BigDecimal pressure;

    @Schema(
            description = "Derived temperature in degrees Celsius",
            example = "65.5",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            minimum = "-50.0",
            maximum = "200.0"
    )
    @DecimalMin(value = "-50.0", message = "Temperature below minimum operating range")
    @DecimalMax(value = "200.0", message = "Temperature exceeds maximum operating range")
    @Column(name = "F_03", precision = 8, scale = 4)
    private BigDecimal temperature;

    @Schema(
            description = "Derived flow rate in m³/h or bpd",
            example = "1250.75",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PositiveOrZero(message = "Flow rate must be zero or positive")
    @Column(name = "F_04", precision = 10, scale = 4)
    private BigDecimal flowRate;

    @Schema(
            description = "Derived contained volume in cubic meters",
            example = "5000.00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PositiveOrZero(message = "Contained volume must be zero or positive")
    @Column(name = "F_05", precision = 15, scale = 4)
    private BigDecimal containedVolume;

    @Schema(
            description = "Timestamp when this derived reading was calculated",
            example = "2026-01-22T01:15:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @PastOrPresent(message = "Calculation time cannot be in the future")
    @Column(name = "F_06", nullable = false)
    private LocalDateTime calculatedAt;

    @Schema(
            description = "Direct reading this derived reading is based on",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_08_FK_01"), nullable = false)
    private FlowReading sourceReading;

    @Schema(
            description = "Pipeline segment this derived reading is associated with",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_08_FK_02"), nullable = false)
    private PipelineSegment pipelineSegment;

    @Schema(
            description = "Validation status of this derived reading",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_08_FK_03"))
    private ValidationStatus validationStatus;

    @Schema(
            description = "Quality flag assigned to this derived reading",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_10", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_08_FK_04"))
    private QualityFlag qualityFlag;

    @Schema(
            description = "Data source classification for this derived reading",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_11", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_08_FK_05"))
    private DataSource dataSource;

    @Schema(
            description = "Reading slot for which this derived reading is calculated",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_13", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_03_08_FK_06"), nullable = false)
    private ReadingSlot readingSlot;
}
