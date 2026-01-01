/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Clearance
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Business / Provider
 *
 **/

package dz.sh.trc.hyflo.business.provider.model;

import dz.sh.trc.hyflo.common.communication.model.Mail;
import dz.sh.trc.hyflo.configuration.template.GenericModel;
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

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Clearance")
@Table(name="T_02_03_06", uniqueConstraints = { @UniqueConstraint(name = "T_02_03_06_UK_01", columnNames = { "F_03" })})
public class Clearance extends GenericModel {
	
	@Column(name="F_01", length=200)
	private String designationAr;

	@Column(name="F_02", length=200)
	private String designationEn;
	
	@Column(name="F_03", length=200, nullable=false)
	private String designationFr;
	
	@ManyToOne
    @JoinColumn(name="F_04", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_06_FK_01"), nullable=false)
    private Provider provider;
	
	@ManyToOne
    @JoinColumn(name="F_05", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_06_FK_02"), nullable=false)
    private ProviderRepresentator providerRepresentator;

	@ManyToOne
    @JoinColumn(name="F_06", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_06_FK_03"), nullable=true)
    private Mail reference;

}
