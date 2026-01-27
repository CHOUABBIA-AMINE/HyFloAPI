/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowOperation
 * 	@CreatedOn	: 01-21-2026
 * 	@UpdatedOn	: 01-22-2026
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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Daily flow operation recording hydrocarbon movements.
 * Represents production input, transportation, or consumption output operations.
 */
@Schema(description = "Daily flow operation tracking hydrocarbon movements (production, transport, consumption)")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(name = "FlowOperation")
@Table(name = "T_03_03_01",indexes = {@Index(name = "T_03_03_01_IX_01", columnList = "F_01"),
									  @Index(name = "T_03_03_01_IX_02", columnList = "F_06"),
									  @Index(name = "T_03_03_01_IX_03", columnList = "F_08")},
						   uniqueConstraints = @UniqueConstraint(name = "T_03_03_01_UK_01", columnNames = {"F_01", "F_06", "F_08", "F_09"}))
public class FlowOperation extends GenericModel {
    
	@Schema(
		description = "Date of the flow operation",
		example = "2026-01-22",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Operation date is mandatory")
	@PastOrPresent(message = "Operation date cannot be in the future")
	@Column(name = "F_01", nullable = false)
	private LocalDate date;
    
	@Schema(
		description = "Volume of product moved (in cubic meters or barrels)",
		example = "25000.50",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Volume is mandatory")
	@DecimalMin(value = "0.0", inclusive = true, message = "Volume cannot be negative")
	@Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
	@Column(name = "F_02", precision = 15, scale = 2, nullable = false)
	private BigDecimal volume;
    
	@Schema(
		description = "Timestamp when this operation was validated by supervisor",
		example = "2026-01-22T08:30:00",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PastOrPresent(message = "Record time cannot be in the future")
	@Column(name = "F_03")
	private LocalDateTime recordedAt;
    
	@Schema(
		description = "Timestamp when this operation was validated by supervisor",
		example = "2026-01-22T08:30:00",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PastOrPresent(message = "Validation time cannot be in the future")
	@Column(name = "F_04")
	private LocalDateTime validatedAt;
    
	@Schema(
		description = "Additional notes or comments about this operation",
		example = "Normal operation, no anomalies detected",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 500
	)
	@Size(max = 500, message = "Notes must not exceed 500 characters")
	@Column(name = "F_05", length = 500)
	private String notes;
    
	@Schema(
		description = "Infrastructure where this operation occurred (facility, station, terminal)",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Infrastructure is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_06", foreignKey = @ForeignKey(name = "T_03_03_01_FK_01"), nullable = false)
	private Infrastructure infrastructure;
    
	@Schema(
		description = "Product type being moved (crude oil, natural gas, condensate, etc.)",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Product is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_07", foreignKey = @ForeignKey(name = "T_03_03_01_FK_02"), nullable = false)
	private Product product;
    
	@Schema(
		description = "Type of operation (production input, transport, consumption output)",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Operation type is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_08", foreignKey = @ForeignKey(name = "T_03_03_01_FK_03"), nullable = false)
	private OperationType type;
    
	@Schema(
		description = "Employee who recorded this operation",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Recording employee is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_09", foreignKey = @ForeignKey(name = "T_03_03_01_FK_04"), nullable = false)
	private Employee recordedBy;
    
	@Schema(
		description = "Supervisor who validated this operation",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_10", foreignKey = @ForeignKey(name = "T_03_03_01_FK_05"))
	private Employee validatedBy;
    
	@Schema(
		description = "Current validation status (pending, approved, rejected)",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Validation status is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_11", foreignKey = @ForeignKey(name = "T_03_03_01_FK_06"), nullable = false)
	private ValidationStatus validationStatus;
}
