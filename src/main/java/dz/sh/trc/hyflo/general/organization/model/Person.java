/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Person
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.model;

import java.util.Date;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.general.localization.model.Country;
import dz.sh.trc.hyflo.general.localization.model.Locality;
import dz.sh.trc.hyflo.system.utility.model.File;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Person")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_01_03_03")
public class Person extends GenericModel {

	@Column(name="F_01", length=100, nullable=true)
	private String lastNameAr;

	@Column(name="F_02", length=100, nullable=true)
	private String firstNameAr;

	@Column(name="F_03", length=100, nullable=false)
	private String lastNameLt;

	@Column(name="F_04", length=100, nullable=false)
	private String firstNameLt;

	@Column(name="F_05", nullable=true)
	private Date birthDate;

	@Column(name="F_06", length=200, nullable=true)
	private String birthPlaceAr;

	@Column(name="F_07", length=200, nullable=true)
	private String birthPlaceLt;
	
	@Column(name="F_08", length=200, nullable=true)
	private String addressAr;
	
	@Column(name="F_09", length=200, nullable=true)
	private String addressLt;
	
	@ManyToOne
    @JoinColumn(name="F_10", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_03_03_FK_01"), nullable=true)
    private Locality birthLocality;
	
	@ManyToOne
    @JoinColumn(name="F_11", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_03_03_FK_02"), nullable=true)
    private Locality addressLocality;

	@ManyToOne
	@JoinColumn(name="F_12", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_03_03_FK_03"), nullable=true)
	private Country country;
	
	@ManyToOne
    @JoinColumn(name="F_13", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_03_03_FK_04"), nullable=true)
    private File picture;

}
