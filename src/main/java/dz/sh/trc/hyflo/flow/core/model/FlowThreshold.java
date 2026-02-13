/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowThreshold
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-22-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import java.math.BigDecimal;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Operating thresholds for pipeline monitoring and alerting.
 * Defines safe operating ranges for pressure, temperature, and flow rates.
 */
@Schema(description = "Pipeline operating thresholds for monitoring and automated alerting")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="FlowThreshold")
@Table(name="T_03_03_06", uniqueConstraints = {@UniqueConstraint(name="T_03_03_06_UK_01", columnNames={"F_09", "F_10"})})
public class FlowThreshold extends GenericModel {
    
	@Schema(
		description = "Minimum safe pressure (bar)",
		example = "50.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Minimum pressure is mandatory")
	@PositiveOrZero(message = "Minimum pressure must be zero or positive")
	@Column(name="F_01", precision = 8, scale = 4, nullable = false)
	private BigDecimal pressureMin;
    
	@Schema(
		description = "Maximum safe pressure (bar)",
		example = "120.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Maximum pressure is mandatory")
	@DecimalMax(value = "500.0", message = "Maximum pressure exceeds absolute limit")
	@Column(name="F_02", precision = 8, scale = 4, nullable = false)
	private BigDecimal pressureMax;
    
	@Schema(
		description = "Minimum safe temperature (°C)",
		example = "5.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Minimum temperature is mandatory")
	@DecimalMin(value = "-50.0", message = "Minimum temperature below absolute limit")
	@Column(name="F_03", precision = 8, scale = 4, nullable = false)
	private BigDecimal temperatureMin;
    
	@Schema(
		description = "Maximum safe temperature (°C)",
		example = "85.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Maximum temperature is mandatory")
	@DecimalMax(value = "200.0", message = "Maximum temperature exceeds absolute limit")
	@Column(name="F_04", precision = 8, scale = 4, nullable = false)
	private BigDecimal temperatureMax;
    
	@Schema(
		description = "Minimum acceptable flow rate (m³/h or bpd)",
		example = "500.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Minimum flow rate is mandatory")
	@PositiveOrZero(message = "Minimum flow rate must be zero or positive")
	@Column(name="F_05", precision = 10, scale = 4, nullable = false)
	private BigDecimal flowRateMin;
    
	@Schema(
		description = "Maximum acceptable flow rate (m³/h or bpd)",
		example = "2000.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Maximum flow rate is mandatory")
	@PositiveOrZero(message = "Maximum flow rate must be positive")
	@Column(name="F_06", precision = 10, scale = 4, nullable = false)
	private BigDecimal flowRateMax;
    
	@Schema(
		description = "Minimum acceptable contained volume (m³)",
		example = "500.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Minimum contained volume is mandatory")
	@PositiveOrZero(message = "Minimum contained volume must be zero or positive")
	@Column(name="F_07", precision = 15, scale = 4, nullable = false)
	private BigDecimal containedVolumeMin;
    
	@Schema(
		description = "Maximum acceptable contained volume (m³)",
		example = "40000.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Maximum contained volume is mandatory")
	@PositiveOrZero(message = "Maximum contained volume must be positive")
	@Column(name="F_08", precision = 15, scale = 4, nullable = false)
	private BigDecimal containedVolumeMax;
    
	@Schema(
		description = "Alert tolerance percentage for threshold breaches (e.g., 5.0 for ±5%)",
		example = "5.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Alert tolerance is mandatory")
	@DecimalMin(value = "0.0", message = "Alert tolerance cannot be negative")
	@DecimalMax(value = "50.0", message = "Alert tolerance cannot exceed 50%")
	@Column(name="F_09", precision = 7, scale = 4, nullable = false)
	private BigDecimal alertTolerance;
    
	@Schema(
		description = "Indicates if this threshold configuration is currently active",
		example = "true",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Active status is mandatory")
	@Column(name="F_10", nullable = false)
	private Boolean active;
    
	@Schema(
		description = "Pipeline to which these thresholds apply",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Pipeline is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_11", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_06_FK_01"), nullable=false)
	private Pipeline pipeline;
}
