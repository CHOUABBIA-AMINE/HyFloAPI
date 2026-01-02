/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Structure
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Organization
 *
 **/

package dz.sh.trc.hyflo.network.organization.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.network.type.model.StructureType;
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
 * Structure Entity - Extends GenericModel
 * Represents organizational structures with multilingual designations and hierarchy
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Structure")
@Table(name="T_01_04_07", uniqueConstraints = { @UniqueConstraint(name = "T_01_04_07_UK_01", columnNames = { "F_01" }),
												@UniqueConstraint(name = "T_01_04_07_UK_02", columnNames = { "F_04" })})
public class Structure extends GenericModel {
	
	@Column(name="F_01", length=50, nullable=false)
	private String code;
	
	@Column(name="F_02", length=100)
	private String designationAr;

	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
	
	@ManyToOne
	@JoinColumn(name="F_05", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_04_07_FK_01"), nullable=false)
	private StructureType structureType;
	
	@ManyToOne
	@JoinColumn(name="F_06", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_04_07_FK_02"), nullable=true)
	private Structure parentStructure;
}
