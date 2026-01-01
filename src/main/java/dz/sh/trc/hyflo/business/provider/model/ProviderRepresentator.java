/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProviderRepresentator
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Business / Provider
 *
 **/

package dz.sh.trc.hyflo.business.provider.model;

import java.util.List;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity(name="ProviderRepresentator")
@Table(name="T_02_03_05")
public class ProviderRepresentator extends GenericModel {
	
	@Column(name="F_01", length=50, nullable=false)
	private String firstname;

	@Column(name="F_02", length=50, nullable=false)
	private String lastname;
	
	@Column(name="F_03", length=200)
	private String birthDate;
	
	@Column(name="F_04", length=100)
	private String birthPlace;
	
	@Column(name="F_05", length=100)
	private String address;
	
	@Column(name="F_06", length=50)
	private String jobTitle;
	
	@Column(name="F_07", length=100)
	private String mobilePhoneNumber;
	
	@Column(name="F_08", length=100)
	private String fixPhoneNumber;
	
	@Column(name="F_09", length=100)
	private String mail;
	
	@ManyToOne
    @JoinColumn(name="F_10", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_05_FK_01"), nullable=false)
    private Provider provider;

	@OneToMany(mappedBy ="providerRepresentator")
	private List<Clearance> clearances;
}
