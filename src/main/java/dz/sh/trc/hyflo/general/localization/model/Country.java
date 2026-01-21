/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Country
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Represents countries in the global localization hierarchy.
 * Used for vendor/partner registration and international operations.
 */
@Schema(description = "Country entity for international localization and partner registration")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Country")
@Table(name="T_01_02_02", uniqueConstraints = {@UniqueConstraint(name = "T_01_02_02_UK_01", columnNames = { "F_04" })})
public class Country extends GenericModel {
	
	@Schema(
		description = "ISO 3166-1 alpha-3 country code",
		example = "DZA",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 3
	)
	@NotBlank(message = "Country code is mandatory")
	@Size(min = 3, max = 3, message = "Country code must be exactly 3 characters (ISO 3166-1 alpha-3)")
	@Column(name="F_01", length=3, nullable=false)
	private String code;
	
	@Schema(
		description = "Country name in Arabic",
		example = "الجزائر",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_02", length=100)
	private String designationAr;

	@Schema(
		description = "Country name in English",
		example = "Algeria",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Schema(
		description = "Country name in French (required for SONATRACH operations)",
		example = "Algérie",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
}
