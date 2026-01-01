/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProcurementDirector
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
 * ProcurementDirector Entity - Extends GenericModel
 * 
 * Database table: T_02_01_03
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
@Entity(name="ProcurementDirector")
@Table(name="T_02_01_03", uniqueConstraints = { @UniqueConstraint(name = "T_02_01_03_UK_01", columnNames = { "F_03" })})
public class ProcurementDirector extends GenericModel {
	
	@Column(name="F_01", length=300)
	private String designationAr;

	@Column(name="F_02", length=300)
	private String designationEn;
	
	@Column(name="F_03", length=300, nullable=false)
	private String designationFr;

}

/*
INSERT INTO T_02_01_03 (F_00, F_01, F_02, F_03) VALUES
(1, 'المديرية الفرعية للإنجازات', 'Sub Directorat of Procurements', 'Sous Direction Réalisations'),
(2, 'دائرة المؤن', 'Approvisionment Department', 'Département des Approvisionnements');
 */
