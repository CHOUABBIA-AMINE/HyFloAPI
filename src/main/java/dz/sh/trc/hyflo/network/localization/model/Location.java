/**
 *	
 *	@author		: MEDJERAB ABIR
 *
 *	@Name		: Location
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
 *
 **/

package dz.sh.trc.hyflo.network.localization.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
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
 * Location Entity - Extends GenericModel
 * 
 * Database table: T_20_03
 * Primary key: F_00 (id) - inherited from GenericModel
 * 
 * Fields:
 * - F_00: id (inherited) - Primary key
 * - F_01: name - Location name (required)
 * - F_02: code - Location code (unique, required)
 * - F_03: latitude - Latitude coordinate (required)
 * - F_04: longitude - Longitude coordinate (required)
 * - F_05: region - Region relationship (required)
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Location")
@Table(name="T_03_02_03", uniqueConstraints = { @UniqueConstraint(name="T_03_02_03_UK_01", columnNames={"F_01"}),
												@UniqueConstraint(name="T_03_02_03_UK_02", columnNames={"F_04"})})
public class Location extends GenericModel {

    @Column(name="F_01", length=10, nullable=false)
    private String code;

	@Column(name="F_02", length=100)
	private String designationAr;

	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;

    @Column(name="F_05", nullable=false)
    private Double latitude;

    @Column(name="F_06", nullable=false)
    private Double longitude;

	@Column(name = "F_07")
    private Double elevation;

    @ManyToOne
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_02_03_FK_01"), nullable = false)
    private Locality locality;

}
