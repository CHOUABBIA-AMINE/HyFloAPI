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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents individuals in the system (employees, users, contacts).
 * Base class extended by Employee and User entities.
 */
@Schema(description = "Person entity with bilingual name support (Arabic and Latin) and comprehensive demographic information")
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

	@Schema(
		description = "Last name in Arabic script",
		example = "شعبية",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic last name must not exceed 100 characters")
	@Column(name="F_01", length=100, nullable=true)
	private String lastNameAr;

	@Schema(
		description = "First name in Arabic script",
		example = "أمين",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic first name must not exceed 100 characters")
	@Column(name="F_02", length=100, nullable=true)
	private String firstNameAr;

	@Schema(
		description = "Last name in Latin script (required)",
		example = "CHOUABBIA",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "Latin last name is mandatory")
	@Size(max = 100, message = "Latin last name must not exceed 100 characters")
	@Column(name="F_03", length=100, nullable=false)
	private String lastNameLt;

	@Schema(
		description = "First name in Latin script (required)",
		example = "Amine",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "Latin first name is mandatory")
	@Size(max = 100, message = "Latin first name must not exceed 100 characters")
	@Column(name="F_04", length=100, nullable=false)
	private String firstNameLt;

	@Schema(
		description = "Date of birth",
		example = "1990-05-15",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Past(message = "Birth date must be in the past")
	@Column(name="F_05", nullable=true)
	private Date birthDate;

	@Schema(
		description = "Place of birth in Arabic",
		example = "الجزائر العاصمة",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "Arabic birth place must not exceed 200 characters")
	@Column(name="F_06", length=200, nullable=true)
	private String birthPlaceAr;

	@Schema(
		description = "Place of birth in Latin script",
		example = "Alger",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "Latin birth place must not exceed 200 characters")
	@Column(name="F_07", length=200, nullable=true)
	private String birthPlaceLt;
	
	@Schema(
		description = "Physical address in Arabic",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "Arabic address must not exceed 200 characters")
	@Column(name="F_08", length=200, nullable=true)
	private String addressAr;
	
	@Schema(
		description = "Physical address in Latin script",
		example = "15 Rue Didouche Mourad, Alger",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "Latin address must not exceed 200 characters")
	@Column(name="F_09", length=200, nullable=true)
	private String addressLt;
	
	@Schema(
		description = "Locality where the person was born",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name="F_10", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_03_03_FK_01"), nullable=true)
	private Locality birthLocality;
	
	@Schema(
		description = "Locality of current residential address",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name="F_11", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_03_03_FK_02"), nullable=true)
	private Locality addressLocality;

	@Schema(
		description = "Country of citizenship or nationality",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name="F_12", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_03_03_FK_03"), nullable=true)
	private Country country;
	
	@Schema(
		description = "Profile picture or photo identification",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name="F_13", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_03_03_FK_04"), nullable=true)
	private File picture;

}
