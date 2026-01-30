/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProcessingPlant
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import java.util.HashSet;
import java.util.Set;

import dz.sh.trc.hyflo.network.common.model.Partner;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.type.model.ProcessingPlantType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents hydrocarbon processing facilities including refineries,
 * gas treatment plants, and separation units.
 */
@Schema(description = "Hydrocarbon processing plant for refining, treatment, or separation operations")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ProcessingPlant")
@Table(name="T_02_03_05")
@PrimaryKeyJoinColumn(name = "F_00", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_05_FK_00"))
public class ProcessingPlant extends Facility {

	@Schema(
		description = "Processing capacity of the plant in cubic meters per day (0 if not yet operational)",
		example = "15000.0",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PositiveOrZero(message = "Capacity must be zero or positive")
	@Column(name="F_10", nullable=true)
	protected double capacity;

	@Schema(
		description = "Type of processing plant (e.g., refinery, gas treatment, separation unit)",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Processing plant type is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_11", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_05_FK_01"), nullable=false)
	private ProcessingPlantType processingPlantType;
	
	@Schema(
		description = "Collection of hydrocarbon products processed at this plant",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "R_T020305_T020201",
		joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020305_T020201_FK_01")),
		inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020305_T020201_FK_02")),
		uniqueConstraints = @UniqueConstraint(name = "R_T020305_T020201_UK_01", columnNames = {"F_01", "F_02"})
	)
	private Set<Product> products = new HashSet<>();
    
	@Schema(
		description = "Collection of business partners involved in operating this plant",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "R_T020305_T020204",
		joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020305_T020204_FK_01")),
		inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020305_T020204_FK_02")),
		uniqueConstraints = @UniqueConstraint(name = "R_T020305_T020204_UK_01", columnNames = {"F_01", "F_02"})
	)
	private Set<Partner> partners = new HashSet<>();

	@Schema(
		description = "Collection of pipelines connected to this processing plant",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "R_T020305_T020308",
		joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020305_T020308_FK_01")),
		inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020305_T020308_FK_02")),
		uniqueConstraints = @UniqueConstraint(name = "R_T020305_T020308_UK_01", columnNames = {"F_01", "F_02"})
	)
	private Set<Pipeline> pipelines = new HashSet<>();
    
}
