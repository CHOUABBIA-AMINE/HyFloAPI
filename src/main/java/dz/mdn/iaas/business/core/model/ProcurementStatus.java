/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProcurementStatus
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Business / Core
 *
 **/

package dz.mdn.iaas.business.core.model;

import dz.mdn.iaas.configuration.template.GenericModel;
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
 * ProcurementStatus Entity - Extends GenericModel
 * 
 * Database table: T_02_01_05
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
@Entity(name="ProcurementStatus")
@Table(name="T_02_01_05", uniqueConstraints = { @UniqueConstraint(name = "T_02_01_05_UK_01", columnNames = { "F_03" })})
public class ProcurementStatus extends GenericModel {
	
	@Column(name="F_01", length=200)
	private String designationAr;

	@Column(name="F_02", length=200)
	private String designationEn;
	
	@Column(name="F_03", length=200, nullable=false)
	private String designationFr;

}
/*
INSERT INTO T_02_01_05 (F_00, F_01, F_02, F_03) VALUES
(1,'قيد الانتظار','On Hold','En Instance'),
(2,'قيد التنفيذ','Ongoing','En cours'),
(3,'غيرمجدي','Defective','Infrectieux'),
(4,'مُنجز','Finalized','Finalisé'),
(5,'ملغي','Canceled','Annulé'),
(6,'مؤجل','Deferred','Différé'),
(7,'منقول','Transferred','Transféré');
*/
