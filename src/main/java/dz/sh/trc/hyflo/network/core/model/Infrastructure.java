/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Infrastructure
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-16-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import java.time.LocalDate;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.general.organization.model.Region;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
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
 * Database table: T_03_01_03
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
@Entity(name="Infrastructure")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_03_03_01", uniqueConstraints = { @UniqueConstraint(name="T_03_03_01_UK_01", columnNames={"F_01"}) })
public class Infrastructure extends GenericModel {

	@Column(name="F_01", length=20, nullable=false)
	protected String code;

	@Column(name="F_02", length=100, nullable=false)
	protected String name;
    
    @Column(name = "F_03", nullable = true)
    protected LocalDate installationDate;

    @Column(name = "F_04", nullable = true)
    protected LocalDate commissioningDate;

    @Column(name = "F_05", nullable = true)
    protected LocalDate decommissioningDate;
    
    @ManyToOne
    @JoinColumn(name = "F_06", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_01_FK_01"), nullable=false)
    protected OperationalStatus operationalStatus;
    
    @ManyToOne
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_01_FK_02"), nullable = true)
    private Region region;
    
}
