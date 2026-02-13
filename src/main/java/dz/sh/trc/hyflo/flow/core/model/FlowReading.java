/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowReading
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-22-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Real-time or periodic flow measurement readings from pipelines.
 * Captures pressure, temperature, flow rate, and volume for monitoring and analysis.
 */
@Schema(description = "Flow measurement reading capturing real-time pipeline operational parameters")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="FlowReading")
@Table(name="T_03_03_03", indexes = {@Index(name="T_03_03_03_IX_01", columnList="F_01"),
									 @Index(name="T_03_03_03_IX_02", columnList="F_01, F_13"),
									 @Index(name="T_03_03_03_IX_03", columnList="F_11"),
									 @Index(name="T_03_03_03_IX_04", columnList="F_09"),
									 @Index(name="T_03_03_03_IX_05", columnList="F_12, F_11")},
						  uniqueConstraints = {@UniqueConstraint(name="T_03_03_03_UK_01", columnNames={"F_01", "F_12", "F_13"})})
public class FlowReading extends GenericModel {
    
	@Schema(
		description = "Reading date is required",
		example = "2026-01-27",
		requiredMode = Schema.RequiredMode.REQUIRED,
		minimum = "0.0",
		maximum = "500.0"
	)
	@NotNull(message = "Reading date is required")
	@PastOrPresent(message = "Reading date cannot be in the future")
	@Column(name = "F_01", nullable = false)
	private LocalDate readingDate;
	
	@Schema(
		description = "Pressure measurement in bar",
		example = "85.50",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		minimum = "0.0",
		maximum = "500.0"
	)
	@PositiveOrZero(message = "Pressure must be zero or positive")
	@DecimalMin(value = "0.0", message = "Pressure cannot be negative")
	@DecimalMax(value = "500.0", message = "Pressure exceeds maximum safe operating limit (500 bar)")
	@Column(name = "F_02", precision = 8, scale = 4)
	private BigDecimal pressure;
    
	@Schema(
		description = "Temperature measurement in degrees Celsius",
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
		description = "Flow rate in cubic meters per hour (m³/h) or barrels per day (bpd)",
		example = "1250.75",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PositiveOrZero(message = "Flow rate must be zero or positive")
	@Column(name = "F_04", precision = 10, scale = 4)
	private BigDecimal flowRate;
    
	@Schema(
		description = "Total volume contained in pipeline segment (cubic meters)",
		example = "5000.00",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PositiveOrZero(message = "Contained volume must be zero or positive")
	@Column(name = "F_05", precision = 15, scale = 4)
	private BigDecimal containedVolume;
    
	@Schema(
		description = "Timestamp when this reading was recorded",
		example = "2026-01-22T00:15:00",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Recording timestamp is mandatory")
	@PastOrPresent(message = "Recording time cannot be in the future")
	@Column(name = "F_06", nullable = false)
	private LocalDateTime recordedAt;
	
	@Schema(
		description = "Timestamp when this reading was validated by supervisor",
		example = "2026-01-22T08:30:00",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PastOrPresent(message = "Validation time cannot be in the future")
	@Column(name = "F_07")
	private LocalDateTime validatedAt;
    
	@Schema(
		description = "Additional notes about this reading (anomalies, calibration, etc.)",
		example = "Sensor calibrated this morning, reading within normal parameters",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 500
	)
	@Size(max = 500, message = "Notes must not exceed 500 characters")
	@Column(name = "F_08", length = 500)
	private String notes;
    
	@Schema(
		description = "Employee who recorded this reading",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Recording employee is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_09", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_03_FK_01"), nullable = false)
	private Employee recordedBy;
    
	@Schema(
		description = "Supervisor who validated this reading",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_10", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_03_FK_02"))
	private Employee validatedBy;
    
	@Schema(
		description = "Current validation status of this reading",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Validation status is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_11", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_03_FK_03"), nullable = false)
	private ValidationStatus validationStatus;
    
	@Schema(
		description = "Pipeline where this reading was taken",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Pipeline reference is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_12", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_03_FK_04"), nullable = false)
	private Pipeline pipeline;
	
	@Schema(
		description = "Scheduled reading slot",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
    @NotNull(message = "Reading slot is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_13", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_03_FK_05"), nullable = false)
    private ReadingSlot readingSlot;
}
