/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AwardMethod
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Business / Consultation
 *
 **/

package dz.mdn.iaas.business.consultation.model;

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

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="AwardMethod")
@Table(name="T_02_04_01", uniqueConstraints = { @UniqueConstraint(name = "T_02_04_01_UK_01", columnNames = { "F_03" }), 
												@UniqueConstraint(name = "T_02_04_01_UK_02", columnNames = { "F_06" })})
public class AwardMethod extends GenericModel {
	
	@Column(name="F_01", length=150)
	private String designationAr;

	@Column(name="F_02", length=150)
	private String designationEn;
	
	@Column(name="F_03", length=150, nullable=false)
	private String designationFr;
	
	@Column(name="F_04", length=20)
	private String acronymAr;
	
	@Column(name="F_05", length=20)
	private String acronymEn;
	
	@Column(name="F_06", length=20, nullable=false)
	private String acronymFr;

}

/*
INSERT INTO T_02_04_01 (F_00, F_04, F_05, F_06, F_01, F_02, F_03) VALUES
(1, 'ط ع م  و', 'NOTC', 'AOON', 'طلب عروض مفتوح وطني', 'National Open Tender Call', 'Appel d'Offres Ouvert National'),
(2, 'ط ع م و د', 'NIOTC', 'AOONI', 'طلب عروض مفتوح وطني ودولي', 'National and International Open Tender Call', 'Appel d'Offres Ouvert National et International'),
(3, 'ت ب', 'SDN', 'GG / S', 'التراضي البسيط', 'Simple Direct Negotiation', 'Gré à Gré Simple'),
(4, 'ت ب إ', 'NAC', 'GG / AC', 'التراضي بعد الاستشارة', 'Negotiation after Consultation', 'Gré à Gré après Consultation'),
(5, 'إد', 'IC(AP)', 'CI(PA)', 'استشارة داخلية', 'Internal Consultation (Adapted Procedure)', 'Consultation Interne (Procédure Adaptée)');
*/
