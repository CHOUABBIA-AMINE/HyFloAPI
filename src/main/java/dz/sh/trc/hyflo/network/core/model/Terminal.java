/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Terminal
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import java.util.HashSet;
import java.util.Set;

import dz.sh.trc.hyflo.network.type.model.TerminalType;
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
 * Facility Entity - Extends Facility
 * 
 * Database table: T_03_02_04
 * inherited from GenericModel:
 * - Primary key: F_00 (id) 
 * inherited from Infrastructure:
 * - Field: F_01 (code)
 * - Field: F_02 (name)
 * - Field: F_03 (installationDate)
 * - Field: F_04 (commissioningDate)
 * - Field: F_05 (decommissioningDate)
 * - Field: F_06 (operationalStatus)
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
@Entity(name="Terminal")
@Table(name="T_03_03_04")
public class Terminal extends Facility {

    @ManyToOne
    @JoinColumn(name="F_13", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_04_FK_01"), nullable=false)
    private TerminalType terminalType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "R_T030304_T030307",
        joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T030304_T030307_FK_01")),
        inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T030304_T030307_FK_02")),
        uniqueConstraints = @UniqueConstraint(name = "R_T030304_T030307_UK_01", columnNames = {"F_01", "F_02"})
    )
    private Set<Pipeline> pipelines = new HashSet<>();
    
}
