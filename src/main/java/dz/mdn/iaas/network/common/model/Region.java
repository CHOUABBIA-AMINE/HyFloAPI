/**
 *	
 *	@author		: MEDJERAB ABIR
 *
 *	@Name		: Region
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
 *
 **/

package dz.mdn.iaas.network.common.model;

import dz.mdn.iaas.configuration.template.GenericModel;
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
 * Region Entity - Extends GenericModel
 * 
 * Database table: T_20_02
 * Primary key: F_00 (id) - inherited from GenericModel
 * 
 * Fields:
 * - F_00: id (inherited) - Primary key
 * - F_01: name - Region name (required)
 * - F_02: zone - Zone relationship (required)
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Region")
@Table(name="T_03_02_02", uniqueConstraints = { @UniqueConstraint(name="T_03_02_02_UK_01", columnNames={"F_01"}),
												@UniqueConstraint(name="T_03_02_02_UK_02", columnNames={"F_04"})})
public class Region extends GenericModel {

    @Column(name="F_01", length=10, nullable=false)
    private String code;

	@Column(name="F_02", length=100)
	private String designationAr;

	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
    
    @Column(name="F_05", length=200)
    private String descriptionAr;
    
    @Column(name="F_06", length=200)
    private String descriptionEn;
    
    @Column(name="F_07", length=200)
    private String descriptionFr;
	
	@ManyToOne
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_02_02_FK_01"), nullable = false)
    private Zone zone;
	
	@ManyToOne
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_02_02_FK_02"), nullable = true)
    private Activity activity;
}
