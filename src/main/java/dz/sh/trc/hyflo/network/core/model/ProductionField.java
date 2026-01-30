/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProductionField
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
import dz.sh.trc.hyflo.network.type.model.ProductionFieldType;
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
 * Represents oil and gas production fields where hydrocarbons are extracted.
 * Production fields are the source points in the transportation network.
 */
@Schema(description = "Oil or gas production field where hydrocarbons are extracted from reservoirs")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ProductionField")
@Table(name="T_02_03_06")
@PrimaryKeyJoinColumn(name = "F_00", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_06_FK_00"))
public class ProductionField extends Facility {

	@Schema(
		description = "Production capacity of the field in cubic meters per day (0 if not yet producing)",
		example = "25000.0",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PositiveOrZero(message = "Capacity must be zero or positive")
	@Column(name="F_10", nullable=true)
	protected double capacity;

	@Schema(
		description = "Type of production field (e.g., oil field, gas field, condensate field)",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Production field type is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_11", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_06_FK_01"), nullable=false)
	private ProductionFieldType productionFieldType;
    
	@Schema(
		description = "Processing plant where field output is processed, if applicable",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_12", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_06_FK_02"), nullable=true)
	private ProcessingPlant processingPlant;
	
	@Schema(
		description = "Collection of hydrocarbon products extracted from this field",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "R_T020306_T020201",
		joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020306_T020201_FK_01")),
		inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020306_T020201_FK_02")),
		uniqueConstraints = @UniqueConstraint(name = "R_T020306_T020201_UK_01", columnNames = {"F_01", "F_02"})
	)
	private Set<Product> products = new HashSet<>();
    
	@Schema(
		description = "Collection of business partners involved in field operations",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "R_T020306_T020204",
		joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020306_T020204_FK_01")),
		inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020306_T020204_FK_02")),
		uniqueConstraints = @UniqueConstraint(name = "R_T020306_T020204_UK_01", columnNames = {"F_01", "F_02"})
	)
	private Set<Partner> partners = new HashSet<>();
    
}
