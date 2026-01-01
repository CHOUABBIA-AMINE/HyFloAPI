/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: MailNature
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Model
 *	@Layer		: Common / Communication
 *	@Package	: Common / Communication / Model
 *
 **/

package dz.sh.trc.hyflo.common.communication.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * MailNature Entity - Extends GenericModel
 * Represents nature/category of mail with multilingual designations
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="MailNature")
@Table(name="T_01_02_01", uniqueConstraints = { @UniqueConstraint(name = "T_01_02_01_UK_01", columnNames = { "F_03" })})
public class MailNature extends GenericModel {
	
	@Column(name="F_01", length=100)
	private String designationAr;

	@Column(name="F_02", length=100)
	private String designationEn;
	
	@Column(name="F_03", length=100, nullable=false)
	private String designationFr;
}
