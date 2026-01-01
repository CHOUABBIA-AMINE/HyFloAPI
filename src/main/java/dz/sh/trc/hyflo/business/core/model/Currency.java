/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Currency
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Business / Core
 *
 **/

package dz.sh.trc.hyflo.business.core.model;

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
 * Currency Entity - Extends GenericModel
 * 
 * Database table: T_02_01_01
 * Primary key: F_00 (id) - inherited from GenericModel
 * 
 * Fields:
 * - F_00: id (inherited) - Primary key
 * - F_01: code - Currency code (unique, required)
 * - F_02: designationAr - Arabic designation (unique, required)
 * - F_03: designationEn - English designation (unique, required)
 * - F_04: designationFr - French designation (unique, required)
 * - F_05: acronymAr - Arabic acronym (unique, required)
 * - F_06: acronymEn - English acronym (unique, required)
 * - F_07: acronymFr - French acronym (unique, required)
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Currency")
@Table(name="T_02_01_01", uniqueConstraints = { @UniqueConstraint(name = "T_02_01_01_UK_01", columnNames = { "F_01" }), 
											 	@UniqueConstraint(name = "T_02_01_01_UK_02", columnNames = { "F_02" }), 
											 	@UniqueConstraint(name = "T_02_01_01_UK_03", columnNames = { "F_03" }), 
											 	@UniqueConstraint(name = "T_02_01_01_UK_04", columnNames = { "F_04" }), 
											 	@UniqueConstraint(name = "T_02_01_01_UK_05", columnNames = { "F_05" }), 
											 	@UniqueConstraint(name = "T_02_01_01_UK_05", columnNames = { "F_06" }), 
											 	@UniqueConstraint(name = "T_02_01_01_UK_05", columnNames = { "F_07" })})
public class Currency extends GenericModel {
	
	@Column(name="F_01", length=20, nullable=false)
	private String code;
	
	@Column(name="F_02", length=50, nullable=false)
	private String designationAr;

	@Column(name="F_03", length=50, nullable=false)
	private String designationEn;
	
	@Column(name="F_04", length=50, nullable=false)
	private String designationFr;
	
	@Column(name="F_05", length=20, nullable=false)
	private String acronymAr;
	
	@Column(name="F_06", length=20, nullable=false)
	private String acronymEn;
	
	@Column(name="F_07", length=20, nullable=false)
	private String acronymFr;
	
}
