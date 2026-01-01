/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Contract
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Business / Contract
 *
 **/

package dz.mdn.iaas.business.contract.model;

import java.util.Date;
import java.util.List;

import dz.mdn.iaas.configuration.template.GenericModel;
import dz.mdn.iaas.business.consultation.model.Consultation;
import dz.mdn.iaas.business.core.model.ApprovalStatus;
import dz.mdn.iaas.business.core.model.Currency;
import dz.mdn.iaas.business.core.model.ProcurementStatus;
import dz.mdn.iaas.business.plan.model.PlannedItem;
import dz.mdn.iaas.business.provider.model.Provider;
import dz.mdn.iaas.common.communication.model.Mail;
import dz.mdn.iaas.common.document.model.Document;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Entity(name="Contract")
@Table(name="T_02_05_04", uniqueConstraints = { @UniqueConstraint(name = "T_02_05_04_UK_01", columnNames = { "F_01" })})
public class Contract extends GenericModel {
	
	@Column(name="F_01", nullable=false)
	private String internalId;
	
	@Column(name="F_02")
	private String contractYear;
	
	@Column(name="F_03")
	private String reference;
	
	@Column(name="F_04", length=300)
	private String designationAr;

	@Column(name="F_05", length=300)
	private String designationEn;
	
	@Column(name="F_06", length=300, nullable=false)
	private String designationFr;
	
	@Column(name="F_07")
	private double amount;
	
	@Column(name="F_08")
	private double transferableAmount;
	
	@Column(name="F_09")
	private Date startDate;
	
	@Column(name="F_10")
	private String approvalReference;
	
	@Column(name="F_11")
	private Date approvalDate;
	
	@Column(name="F_12")
	private Date contractDate;
	
	@Column(name="F_13")
	private Date notifyDate;
	
	@Column(name="F_14")
	private int contractDuration;

	@Column(name="F_15", length=500)
	private String observation;
	
	@ManyToOne
    @JoinColumn(name="F_16", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_05_04_FK_01"), nullable=false)
    private ContractType contractType;
	
	@ManyToOne
    @JoinColumn(name="F_17", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_05_04_FK_02"), nullable=false)
    private Provider provider;

	@ManyToOne
    @JoinColumn(name="F_18", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_05_04_FK_03"), nullable=false)
    private ProcurementStatus procurementStatus;
	
	@ManyToOne
    @JoinColumn(name="F_19", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_05_04_FK_04"), nullable=false)
    private ContractStep contractStep;
	
	@ManyToOne
    @JoinColumn(name="F_20", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_05_04_FK_05"), nullable=true)
    private ApprovalStatus approvalStatus;
	
	@ManyToOne
    @JoinColumn(name="F_21", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_05_04_FK_06"), nullable=false)
    private Currency currency;

	@ManyToOne
    @JoinColumn(name="F_22", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_05_04_FK_07"), nullable=true)
    private Consultation consultation;

	@ManyToOne
    @JoinColumn(name="F_23", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_05_04_FK_08"), nullable=true)
    private Contract contractUp;
	
	@OneToMany(mappedBy="contract")
	private List<ContractItem> contractItems;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "R_T020504_T010302", 
			joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020504_T010302_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020504_T010302_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T020504_T010302_UK_01", columnNames = {"F_01", "F_02"}))
	private List<Document> documents;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "R_T020504_T010203", 
			joinColumns = @JoinColumn(name = "F_01", foreignKey = @ForeignKey(name = "R_T020504_T010203_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", foreignKey = @ForeignKey(name = "R_T020504_T010203_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T020504_T010203_UK_01", columnNames = {"F_01", "F_02"}))
	private List<Mail> mails;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "R_T020504_T020208", 
			joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020504_T020208_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020504_T020208_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T020504_T020208_UK_01", columnNames = {"F_01", "F_02"}))
	private List<PlannedItem> plannedItems;

}
