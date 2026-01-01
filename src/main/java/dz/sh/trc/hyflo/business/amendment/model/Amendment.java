/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Amendment
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Business / Amendment
 *
 **/

package dz.sh.trc.hyflo.business.amendment.model;

import java.util.Date;
import java.util.List;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.business.contract.model.Contract;
import dz.sh.trc.hyflo.business.core.model.ApprovalStatus;
import dz.sh.trc.hyflo.business.core.model.Currency;
import dz.sh.trc.hyflo.business.core.model.ProcurementStatus;
import dz.sh.trc.hyflo.common.communication.model.Mail;
import dz.sh.trc.hyflo.common.document.model.Document;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Entity(name="Amendment")
@Table(name="T_02_06_04", uniqueConstraints = { @UniqueConstraint(name = "T_02_06_04_UK_01", columnNames = { "F_02" })})
public class Amendment extends GenericModel {
	
	@Column(name="F_01")
	private int internalId;
	
	@Column(name="F_02")
	private String reference;
	
	@Column(name="F_03", length=300)
	private String designationAr;

	@Column(name="F_04", length=300)
	private String designationEn;
	
	@Column(name="F_05", length=300)
	private String designationFr;
	
	@Column(name="F_06")
	private double amount;
	
	@Column(name="F_07")
	private double transferableAmount;
	
	@Column(name="F_08")
	private Date startDate;
	
	@Column(name="F_09")
	private Date approvalDate;
	
	@Column(name="F_10")
	private Date notifyDate;

	@Column(name="F_11", length=500)
	private String observation;
	
	@ManyToOne
    @JoinColumn(name="F_12", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_06_04_FK_01"), nullable=false)
    private Contract contract;
	
	@ManyToOne
    @JoinColumn(name="F_13", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_06_04_FK_02"), nullable=false)
    private AmendmentType amendmentType;

	@ManyToOne
    @JoinColumn(name="F_14", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_06_04_FK_03"), nullable=false)
    private ProcurementStatus procurementStatus;
	
	@ManyToOne
    @JoinColumn(name="F_15", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_06_04_FK_04"), nullable=false)
    private AmendmentPhase amendmentStep;
	
	@ManyToOne
    @JoinColumn(name="F_16", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_06_04_FK_05"), nullable=true)
    private ApprovalStatus approvalStatus;
	
	@ManyToOne
    @JoinColumn(name="F_17", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_06_04_FK_06"), nullable=false)
    private Currency currency;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "R_T020604_T010302", 
			joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020604_T010302_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020604_T010302_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T020604_T010302_UK_01", columnNames = {"F_01", "F_02"}))
	private List<Document> documents;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "R_T020604_T010203", 
			joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "R_T020604_T010203_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "R_T020604_T010203_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T020604_T010203_UK_01", columnNames = {"F_01", "F_02"}))
	private List<Mail> mails;

}
