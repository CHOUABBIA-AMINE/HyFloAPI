/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: HydrocarbonField
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
 *
 **/

package dz.mdn.iaas.network.core.model;

import java.util.HashSet;
import java.util.Set;

import dz.mdn.iaas.network.common.model.Partner;
import dz.mdn.iaas.network.common.model.Product;
import dz.mdn.iaas.network.type.model.HydrocarbonFieldType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Facility Entity - Extends GenericModel
 * 
 * Database table: T_20_08
 * Primary key: F_00 (id) - inherited from GenericModel
 * 
 * Fields:
 * - F_00: id (inherited) - Primary key
 * - F_01: name - Facility name (required)
 * - F_02: code - Facility code (unique, required)
 * - F_03: operationalStatus - Operational status relationship (required)
 * - F_04: location - Location relationship (required)
 * - F_05: facilityType - Facility type relationship (required)
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="HydrocarbonField")
@Table(name="T_03_03_05")
public class HydrocarbonField extends Facility {

    @ManyToOne
    @JoinColumn(name="F_13", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_05_FK_01"), nullable=false)
    private HydrocarbonFieldType hydrocarbonFieldType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "R_T030305_T030307",
        joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T030305_T030307_FK_01")),
        inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T030305_T030307_FK_02")),
        uniqueConstraints = @UniqueConstraint(name = "R_T030305_T030307_UK_01", columnNames = {"F_01", "F_02"})
    )
    private Set<Pipeline> pipelines = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "R_T030305_T030207",
        joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T030305_T030207_FK_01")),
        inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T030305_T030207_FK_02")),
        uniqueConstraints = @UniqueConstraint(name = "R_T030305_T030207_UK_01", columnNames = {"F_01", "F_02"})
    )
    private Set<Partner> partners = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "R_T030305_T030204",
        joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T030305_T030204_FK_01")),
        inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T030305_T030204_FK_02")),
        uniqueConstraints = @UniqueConstraint(name = "R_T030305_T030204_UK_01", columnNames = {"F_01", "F_02"})
    )
    private Set<Product> products = new HashSet<>();
    
}
