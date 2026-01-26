/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Infrastructure
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
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
 * Base entity representing hydrocarbon transportation infrastructure assets.
 * Serves as the parent class for all infrastructure types including pipelines,
 * stations, terminals, processing plants, and production fields.
 */
@Schema(description = "Base entity for all hydrocarbon transportation infrastructure assets within the SONATRACH network")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Infrastructure")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_02_03_01", uniqueConstraints = { @UniqueConstraint(name="T_02_03_01_UK_01", columnNames={"F_01"}) })
public class Infrastructure extends GenericModel {

	@Schema(
		description = "Unique identification code for the infrastructure asset",
		example = "OLZ-PL-001",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 20
	)
	@NotBlank(message = "Infrastructure code is mandatory")
	@Size(max = 20, message = "Infrastructure code must not exceed 20 characters")
	@Column(name="F_01", length=20, nullable=false)
	protected String code;

	@Schema(
		description = "Official name of the infrastructure asset",
		example = "Hassi Messaoud - Skikda Pipeline",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "Infrastructure name is mandatory")
	@Size(max = 100, message = "Infrastructure name must not exceed 100 characters")
	@Column(name="F_02", length=100, nullable=false)
	protected String name;
    
	@Schema(
		description = "Date when the infrastructure was physically installed",
		example = "2020-05-15",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PastOrPresent(message = "Installation date cannot be in the future")
	@Column(name = "F_03", nullable = true)
	protected LocalDate installationDate;

	@Schema(
		description = "Date when the infrastructure was officially commissioned for operational use",
		example = "2020-08-01",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PastOrPresent(message = "Commissioning date cannot be in the future")
	@Column(name = "F_04", nullable = true)
	protected LocalDate commissioningDate;

	@Schema(
		description = "Date when the infrastructure was decommissioned or retired from service",
		example = "2045-12-31",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Column(name = "F_05", nullable = true)
	protected LocalDate decommissioningDate;
    
	@Schema(
		description = "Current operational status of the infrastructure",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Operational status is mandatory")
	@ManyToOne
	@JoinColumn(name = "F_06", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_01_FK_01"), nullable=false)
	protected OperationalStatus operationalStatus;
    
	@Schema(
		description = "Organizational structure owning this infrastructure",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name = "F_07", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_01_FK_02"), nullable = true)
	private Structure owner;
    
}
