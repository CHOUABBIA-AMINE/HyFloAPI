/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Provider
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Business / Provider
 *
 **/

package dz.mdn.iaas.business.provider.model;

import java.util.Date;
import java.util.List;

import dz.mdn.iaas.business.consultation.model.Submission;
import dz.mdn.iaas.common.administration.model.Country;
import dz.mdn.iaas.common.administration.model.State;
import dz.mdn.iaas.configuration.template.GenericModel;
import dz.mdn.iaas.system.utility.model.File;
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
@Entity(name="Provider")
@Table(name="T_02_03_04")
public class Provider extends GenericModel {
	
	@Column(name="F_01", length=200)
	private String designationLt;
	
	@Column(name="F_02", length=200)
	private String designationAr;
	
	@Column(name="F_03", length=20)
	private String acronymLt;
	
	@Column(name="F_04", length=20)
	private String acronymAr;
	
	@Column(name="F_05", length=200)
	private String address;
	
	@Column(name="F_06", length=200)
	private double capital;
	
	@Column(name="F_07", length=200)
	private String comercialRegistryNumber;
	
	@Column(name="F_08")
	private Date comercialRegistryDate;
	
	@Column(name="F_09", length=200)
	private String taxeIdentityNumber;
	
	@Column(name="F_10", length=200)
	private String statIdentityNumber;
	
	@Column(name="F_11", length=200)
	private String bank;
	
	@Column(name="F_12", length=50)
	private String bankAccount;
	
	@Column(name="F_13", length=50)
	private String swiftNumber;
	
	@Column(name="F_14", length=200)
	private String phoneNumbers;
	
	@Column(name="F_15", length=200)
	private String faxNumbers;
	
	@Column(name="F_16", length=300)
	private String mail;
	
	@Column(name="F_17", length=200)
	private String website;
	
	@ManyToOne
    @JoinColumn(name="F_18", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_04_FK_01"), nullable=true)
	private File logo;	
	
	@ManyToOne
    @JoinColumn(name="F_19", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_04_FK_02"), nullable=false)
    private EconomicNature economicNature;
	
	@ManyToOne
    @JoinColumn(name="F_20", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_04_FK_03"), nullable=false)
    private Country country;
	
	@ManyToOne
    @JoinColumn(name="F_21", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_04_FK_04"), nullable=true)
    private State state;
	
	@OneToMany(mappedBy ="provider")
	private List<ProviderExclusion> providerExclusions;
	
	@OneToMany(mappedBy ="provider")
	private List<ProviderRepresentator> providerRepresentators;
	
	@OneToMany(mappedBy ="provider")
	private List<Clearance> clearances;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "R_T020304_T020301", 
			joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020304_T020301_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020304_T020301_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T020304_T020301_UK_01", columnNames = {"F_01", "F_02"}))
	private List<EconomicDomain> economicDomains;
	
	@OneToMany(mappedBy ="tender")
	private List<Submission> submissions;

}
