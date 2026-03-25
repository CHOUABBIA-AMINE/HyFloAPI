/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowReading
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
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
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
 * Represents a manual flow reading recorded by an operator for a pipeline.
 * Each record captures measured volume, pressure, temperature and links to
 * the validation lifecycle status.
 */
@Schema(description = "Operational flow reading recorded by a transport operator")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FlowReading")
@Table(name = "T_04_01_01")
public class FlowReading extends GenericModel {

    @Schema(description = "Date of the reading", example = "2026-03-20")
    @Column(name = "F_01", nullable = false)
    private LocalDate readingDate;

    @Schema(description = "Measured flow volume in m\u00b3", example = "12500.50")
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

    @Schema(description = "FK to pipeline (canonical network/core ownership)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_10", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_04_01_01_FK_01"), nullable = false)
    private Pipeline pipeline;

    @Schema(description = "FK to validation status reference (canonical flow/common ownership)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_11", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_04_01_01_FK_02"))
    private ValidationStatus validationStatus;
}
