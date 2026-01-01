/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Consultation
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Business / Consultation
 *
 **/

package dz.sh.trc.hyflo.business.consultation.model;

import java.util.Date;
import java.util.List;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.business.core.model.ApprovalStatus;
import dz.sh.trc.hyflo.business.core.model.ProcurementDirector;
import dz.sh.trc.hyflo.business.core.model.ProcurementNature;
import dz.sh.trc.hyflo.business.core.model.ProcurementStatus;
import dz.sh.trc.hyflo.business.plan.model.BudgetType;
import dz.sh.trc.hyflo.business.plan.model.PlannedItem;
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
@Entity(name="Consultation")
@Table(name="T_02_04_04", uniqueConstraints = { @UniqueConstraint(name = "T_02_04_04_UK_01", columnNames = { "F_01", "F_02" })})
public class Consultation extends GenericModel {
	
	@Column(name="F_01", length=3, nullable=false)
	private String internalId;
	
	@Column(name="F_02", length=4, nullable=false)
	private String consultationYear;
	
	@Column(name="F_03", length=20)
	private String reference;
	
	@Column(name="F_04", length=300)
	private String designationAr;

	@Column(name="F_05", length=300)
	private String designationEn;
	
	@Column(name="F_06", length=300, nullable=false)
	private String designationFr;
	
	@Column(name="F_07")
	private double allocatedAmount;
	
	@Column(name="F_08")
	private double financialEstimation;
	
	@Column(name="F_09")
	private Date startDate;
	
	@Column(name="F_10")
	private String approvalReference;
	
	@Column(name="F_11")
	private Date approvalDate;
	
	@Column(name="F_12")
	private Date publishDate;
	
	@Column(name="F_13")
	private Date deadline;

	@Column(name="F_14", length=500)
	private String observation;
	
	@ManyToOne
    @JoinColumn(name="F_15", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_04_04_FK_01"), nullable=false)
    private AwardMethod awardMethod;
	
	@ManyToOne
    @JoinColumn(name="F_16", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_04_04_FK_02"), nullable=false)
    private ProcurementNature procurementNature;
	
	@ManyToOne
    @JoinColumn(name="F_17", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_04_04_FK_03"), nullable=false)
    private BudgetType budgetType;
	
	@ManyToOne
    @JoinColumn(name="F_18", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_04_04_FK_04"), nullable=false)
    private ProcurementStatus procurementStatus;
	
	@ManyToOne
    @JoinColumn(name="F_19", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_04_04_FK_05"), nullable=false)
    private ApprovalStatus approvalStatus;
	
	@ManyToOne
    @JoinColumn(name="F_20", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_04_04_FK_06"), nullable=false)
    private ProcurementDirector procurementDirector;
	
	@ManyToOne
    @JoinColumn(name="F_21", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_04_04_FK_07"), nullable=false)
    private ConsultationStep consultationStep;
	
	//@ManyToOne
    //@JoinColumn(name="F_22", foreignKey=@ForeignKey(name="T_22_04_FK_08"), nullable=false)
    //private Project project;
	
	/*@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "R_22_04_02_03", 
			joinColumns = @JoinColumn(name = "F_01", foreignKey=@ForeignKey(name="R_22_04_02_03_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", foreignKey=@ForeignKey(name="R_22_04_02_03_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_22_04_02_03_UK_01", columnNames = {"F_01", "F_02"}))
	private List<Structure> beneficiaries;*/
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "R_T020404_T010302", 
			joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020404_T010302_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020404_T010302_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T020404_T010302_UK_01", columnNames = {"F_01", "F_02"}))
	private List<Document> documents;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "R_T020404_T010203", 
			joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "R_T020404_T010203_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "R_T020404_T010203_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T020404_T010203_UK_01", columnNames = {"F_01", "F_02"}))
	private List<Mail> mails;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "R_T020404_T020208", 
			joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020404_T020208_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020404_T020208_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T020404_T020208_UK_01", columnNames = {"F_01", "F_02"}))
	private List<PlannedItem> plannedItems;
	
	@OneToMany(mappedBy ="consultation")
	private List<Submission> submissions;

}
