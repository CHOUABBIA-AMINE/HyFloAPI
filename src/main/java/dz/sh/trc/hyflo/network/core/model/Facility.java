/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Facility
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import java.util.HashSet;
import java.util.Set;

import dz.sh.trc.hyflo.network.common.model.Vendor;
import dz.sh.trc.hyflo.system.localization.model.Locality;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Facility")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_03_03_02")
public class Facility extends Infrastructure {
	
	@Column(name="F_07", length=100, nullable=false)
	private String placeName;

    @Column(name="F_08", nullable=false)
    private Double latitude;

    @Column(name="F_09", nullable=false)
    private Double longitude;

	@Column(name="F_10")
    private Double elevation;

	@ManyToOne
    @JoinColumn(name = "F_11", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_02_FK_01"), nullable = false)
    protected Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "F_12", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_02_FK_02"), nullable = false)
    private Locality locality;
    
    @Builder.Default
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
    protected Set<Equipment> equipments = new HashSet<>();

}
