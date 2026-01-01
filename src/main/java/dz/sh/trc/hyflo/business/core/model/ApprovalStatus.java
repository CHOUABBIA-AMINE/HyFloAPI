/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ApprovalStatus
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
 * ApprovalStatus Entity - Extends GenericModel
 * 
 * Database table: T_02_01_02
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
@Entity(name="ApprovalStatus")
@Table(name="T_02_01_02", uniqueConstraints = {@UniqueConstraint(name = "T_02_01_02_UK_01", columnNames = { "F_03" })})
public class ApprovalStatus extends GenericModel {
	
	@Column(name="F_01", length=200)
	private String designationAr;

	@Column(name="F_02", length=200)
	private String designationEn;
	
	@Column(name="F_03", length=200, nullable=false)
	private String designationFr;

}
/*
INSERT INTO T_02_01_02 (F_00, F_01, F_02, F_03) VALUES
(1,'غير قابل للتطبيق﻿','Not Applicabale','Non applicable'),
(2,'قيد التحضير','Under Preparation','En cours de préparation'),
(3,'تأشيرة','Visa','Visa'),
(4,'تأشيرة مع توصيات','Visa with Recommendations','Visa avec recommandations'),
(5,'تأشيرة مع تحفظات','Visa under Reservations','Visa sous réserves'),
(6,'تأجيل','Adjournment','Ajournement'),
(7,'رفض','Rejection','Rejet'),
(8,'رفض مؤقت','Temporary Rejection','Rejet à titre temporaire'),
(9,'رفض نهائي','Definitive Rejection','Rejet définitif');
*/
