/**
 *
 * 	@Author: MEDJERAB Abir
 *
 * 	@Name: FlowForecast
 * 	@CreatedOn: 01-21-2026
 * 	@UpdatedOn: 01-22-2026
 *
 * 	@Type: Class
 * 	@Layer: Model
 * 	@Package: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.type.model.OperationType;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
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
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Flow volume forecast for production planning and capacity management.
 * Enables predictive analytics and comparison with actual performance.
 */
@Schema(description = "Flow volume forecast for production planning with accuracy tracking")
@Entity(name = "FlowForecast")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "T_03_03_02", indexes = {@Index(name = "T_03_03_02_IX_01", columnList = "F_01"),
									   @Index(name = "T_03_03_02_IX_02", columnList = "F_07")},
							uniqueConstraints = @UniqueConstraint(name = "T_03_03_02_UK_01", columnNames = {"F_01", "F_07", "F_08", "F_09"}))
public class FlowForecast extends GenericModel {
    
	@Schema(
		description = "Date for which forecast is made",
		example = "2026-01-25",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Forecast date is mandatory")
	@Future(message = "Forecast date must be in the future")
	@Column(name = "F_01", nullable = false)
	private LocalDate forecastDate;
    
	@Schema(
		description = "Predicted volume (cubic meters or barrels)",
		example = "28000.00",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Predicted volume is mandatory")
	@DecimalMin(value = "0.0", inclusive = true, message = "Predicted volume cannot be negative")
	@Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
	@Column(name = "F_02", precision = 15, scale = 2, nullable = false)
	private BigDecimal predictedVolume;
    
	@Schema(
		description = "Adjusted forecast volume after expert review",
		example = "27500.00",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@DecimalMin(value = "0.0", message = "Adjusted volume cannot be negative")
	@Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
	@Column(name = "F_03", precision = 15, scale = 2)
	private BigDecimal adjustedVolume;
    
	@Schema(
		description = "Actual volume recorded (filled after forecast date)",
		example = "27250.50",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@DecimalMin(value = "0.0", message = "Actual volume cannot be negative")
	@Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
	@Column(name = "F_04", precision = 15, scale = 2)
	private BigDecimal actualVolume;
    
	@Schema(
		description = "Forecast accuracy percentage (0-100%)",
		example = "98.5",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@DecimalMin(value = "0.0", message = "Accuracy cannot be negative")
	@DecimalMax(value = "100.0", message = "Accuracy cannot exceed 100%")
	@Digits(integer = 3, fraction = 4, message = "Accuracy must have at most 3 integer digits and 4 decimal places")
	@Column(name = "F_05", precision = 7, scale = 4)
	private BigDecimal accuracy;
    
	@Schema(
		description = "Notes explaining forecast adjustments",
		example = "Adjusted down due to planned maintenance at field A-5",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 500
	)
	@Size(max = 500, message = "Adjustment notes must not exceed 500 characters")
	@Column(name = "F_06", length = 500)
	private String adjustmentNotes;
    
	@Schema(
		description = "Infrastructure for which forecast is made",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Infrastructure is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_07", foreignKey = @ForeignKey(name = "T_03_03_02_FK_01"), nullable = false)
	private Infrastructure infrastructure;
    
	@Schema(
		description = "Product type being forecasted",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Product is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_08", foreignKey = @ForeignKey(name = "T_03_03_02_FK_02"), nullable = false)
	private Product product;
    
	@Schema(
		description = "Operation type (production, consumption)",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Operation type is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_09", foreignKey = @ForeignKey(name = "T_03_03_02_FK_03"), nullable = false)
	private OperationType operationType;
    
	@Schema(
		description = "Supervisor who approved/adjusted this forecast",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_10", foreignKey = @ForeignKey(name = "T_03_03_02_FK_04"))
	private Employee supervisor;
}
