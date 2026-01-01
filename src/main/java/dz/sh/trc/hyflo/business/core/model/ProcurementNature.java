/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProcurementNature
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
 * ProcurementNature Entity - Extends GenericModel
 * 
 * Database table: T_02_01_04
 * Primary key: F_00 (id) - inherited from GenericModel
 * 
 * Fields:
 * - F_00: id (inherited) - Primary key
 * - F_01: designationAr - Arabic designation (optional)
 * - F_02: designationEn - English designation (optional)
 * - F_03: designationFr - French designation (unique, required)
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ProcurementNature")
@Table(name="T_02_01_04", uniqueConstraints = { @UniqueConstraint(name = "T_02_01_04_UK_01", columnNames = { "F_03" })})
public class ProcurementNature extends GenericModel {
	
	@Column(name="F_01", length=200)
	private String designationAr;

	@Column(name="F_02", length=200)
	private String designationEn;
	
	@Column(name="F_03", length=200, nullable=false)
	private String designationFr;

}
/*
INSERT INTO T_02_01_04 (F_00, F_01, F_02, F_03) VALUES
(1,'الوسائل الكبرى','Major Means','Moyens Majeurs'),
(2,'تسيير','Operation','Fonctionnement'),
(3,'تجهيز','Equipment','Équipement'),
(4,'قطع غيار','Spare Parts','Pièces de Rechange'),
(5,'أشغال','Constructions','Travaux'),
(6,'خدمات','Services','Prestations'),
(7,'دراسات','Studies','Études');
*/
