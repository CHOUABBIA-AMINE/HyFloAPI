/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: State
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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents Algerian provinces (wilayas) - first-level administrative divisions.
 * Algeria has 58 provinces, each identified by a 2-digit code (01-58).
 */
@Schema(description = "Algerian province (wilaya) representing first-level administrative division")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="State")
@Table(name="T_01_02_03", uniqueConstraints = {@UniqueConstraint(name = "T_01_02_03_UK_01", columnNames = { "F_01" }),
											   @UniqueConstraint(name = "T_01_02_03_UK_02", columnNames = { "F_04" })})
public class State extends GenericModel {
	
	@Schema(
		description = "Two-digit wilaya code (01-58)",
		example = "16",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 10
	)
	@NotBlank(message = "State code is mandatory")
	@Size(max = 10, message = "State code must not exceed 10 characters")
	@Pattern(regexp = "^\\d{2}$", message = "State code must be a 2-digit number")
	@Column(name="F_01", length=10, nullable=false)
	private String code;
	
	@Schema(
		description = "Wilaya name in Arabic",
		example = "الجزائر",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_02", length=100)
	private String designationAr;

	@Schema(
		description = "Wilaya name in English",
		example = "Algiers",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Schema(
		description = "Wilaya name in French (required)",
		example = "Alger",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;

}
