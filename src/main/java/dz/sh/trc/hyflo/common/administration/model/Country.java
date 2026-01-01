/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: Country
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Model
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Model
 *
 **/

package dz.sh.trc.hyflo.common.administration.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

/**
 * Country Entity - Extends GenericModel
 * Represents countries with multilingual designations
 */
@Setter
@Getter
@Entity(name="Country")
@Table(name="T_01_04_01", uniqueConstraints = { @UniqueConstraint(name = "T_01_04_01_UK_01", columnNames = { "F_03" })})
public class Country extends GenericModel {
	
	@Column(name="F_01", length=3, nullable=false)
	private String code;
	
	@Column(name="F_02", length=100)
	private String designationAr;

	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
}
