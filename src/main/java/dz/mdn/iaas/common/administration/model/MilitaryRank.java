/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: MilitaryRank
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Model
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Model
 *
 **/

package dz.mdn.iaas.common.administration.model;

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
 * MilitaryRank Entity - Extends GenericModel
 * Represents military ranks with multilingual designations and hierarchy
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="MilitaryRank")
@Table(name="T_01_04_05", uniqueConstraints = { @UniqueConstraint(name = "T_01_04_05_UK_01", columnNames = { "F_03" })})
public class MilitaryRank extends GenericModel {
	
	@Column(name="F_01", length=100)
	private String designationAr;

	@Column(name="F_02", length=100)
	private String designationEn;
	
	@Column(name="F_03", length=100, nullable=false)
	private String designationFr;
	
	@Column(name="F_04", length=100)
	private String acronymAr;

	@Column(name="F_05", length=100)
	private String acronymEn;
	
	@Column(name="F_06", length=100)
	private String acronymFr;
	
	@ManyToOne
	@JoinColumn(name="F_07", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_04_05_FK_01"), nullable=false)
	private MilitaryCategory militaryCategory;
	
}
