/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Equipment
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import java.time.LocalDate;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.model.Vendor;
import dz.sh.trc.hyflo.network.type.model.EquipmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
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
 * Represents equipment and machinery installed at facilities.
 * Includes pumps, compressors, valves, meters, and other operational equipment.
 */
@Schema(description = "Equipment and machinery installed at hydrocarbon transportation facilities")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Equipment")
@Table(name="T_02_03_10", uniqueConstraints = { @UniqueConstraint(name="T_02_03_10_UK_01", columnNames={"F_02"}) })
public class Equipment extends GenericModel {

	@Schema(
		description = "Unique identification code for the equipment",
		example = "PMP-HM-001",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 50
	)
	@NotBlank(message = "Equipment code is mandatory")
	@Size(max = 50, message = "Equipment code must not exceed 50 characters")
	@Column(name="F_01", length=50, nullable=false)
	private String code;

	@Schema(
		description = "Name or designation of the equipment",
		example = "Centrifugal Pump Unit A1",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "Equipment name is mandatory")
	@Size(max = 100, message = "Equipment name must not exceed 100 characters")
	@Column(name="F_02", length=100, nullable=false)
	private String name;

	@Schema(
		description = "Manufacturer's model number",
		example = "BB5-200-350",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 50
	)
	@Size(max = 50, message = "Model number must not exceed 50 characters")
	@Column(name="F_03", length=50, nullable=true)
	private String modelNumber;

	@Schema(
		description = "Manufacturer's serial number for traceability",
		example = "SN-2020-HM-12345",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Serial number must not exceed 100 characters")
	@Column(name="F_04", length=100, nullable=true)
	private String serialNumber;

	@Schema(
		description = "Date when the equipment was manufactured",
		example = "2020-03-15",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PastOrPresent(message = "Manufacturing date cannot be in the future")
	@Column(name="F_05", nullable=true)
	private LocalDate manufacturingDate;

	@Schema(
		description = "Date when the equipment was physically installed at the facility",
		example = "2020-06-20",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PastOrPresent(message = "Installation date cannot be in the future")
	@Column(name="F_06", nullable=true)
	private LocalDate installationDate;

	@Schema(
		description = "Date when the equipment was commissioned for operational use",
		example = "2020-07-01",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PastOrPresent(message = "Commissioning date cannot be in the future")
	@Column(name="F_07", nullable=true)
	private LocalDate commissioningDate;

	@Schema(
		description = "Date of the most recent maintenance service",
		example = "2025-12-15",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PastOrPresent(message = "Last maintenance date cannot be in the future")
	@Column(name="F_08", nullable=true)
	private LocalDate lastMaintenanceDate;

	@Schema(
		description = "Date when the equipment was decommissioned or retired from service",
		example = "2040-12-31",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Column(name="F_09", nullable=true)
	private LocalDate decommissioningDate;

	@Schema(
		description = "Current operational status of the equipment",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Operational status is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_10", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_10_FK_01"), nullable=false)
	private OperationalStatus operationalStatus;

	@Schema(
		description = "Type or category of the equipment",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Equipment type is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_11", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_10_FK_02"), nullable=false)
	private EquipmentType equipmentType;

	@Schema(
		description = "Facility where this equipment is installed",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Facility is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_12", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_10_FK_03"), nullable=false)
	private Facility facility;

	@Schema(
		description = "Manufacturer of the equipment",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Manufacturer is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_13", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_10_FK_04"), nullable=false)
	private Vendor manufacturer;
}
