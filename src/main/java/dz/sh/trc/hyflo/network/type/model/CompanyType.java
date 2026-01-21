/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CompanyType
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Type
 *
 **/


package dz.sh.trc.hyflo.network.type.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Base classification for companies (partners and vendors) involved in SONATRACH operations.
 * Supports multilingual designations for international collaboration.
 */
@Schema(description = "Base type classification for companies (partners and vendors)")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="CompanyType")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_02_01_01", uniqueConstraints = {@UniqueConstraint(name="T_02_01_01_UK_01", columnNames={"F_03"})})
public class CompanyType extends GenericModel {

	@Schema(
		description = "Company type designation in Arabic",
		example = "مقاول رئيسي",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_01", length=100, nullable=true)
	private String designationAr;

	@Schema(
		description = "Company type designation in English",
		example = "Prime Contractor",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_02", length=100, nullable=true)
	private String designationEn;

	@Schema(
		description = "Company type designation in French (required for SONATRACH operations)",
		example = "Entrepreneur Principal",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_03", length=100, nullable=false)
	private String designationFr;
    
}