/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Locality
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
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents Algerian communes (baladiyahs) - municipalities at the local level.
 * Third-level administrative division below daïras.
 */
@Schema(description = "Algerian commune (baladiyah) representing third-level administrative subdivision - municipalities")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Locality")
@Table(name="T_01_02_05", uniqueConstraints = {@UniqueConstraint(name = "T_01_02_05_UK_01", columnNames = { "F_01" })})
public class Locality extends GenericModel {
	
	@Schema(
		description = "Unique code identifying the commune",
		example = "160101",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 10
	)
	@NotBlank(message = "Locality code is mandatory")
	@Size(max = 10, message = "Locality code must not exceed 10 characters")
	@Column(name="F_01", length=10, nullable=false)
	private String code;
	
	@Schema(
		description = "Commune name in Arabic",
		example = "باب الوادي",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_02", length=100)
	private String designationAr;

	@Schema(
		description = "Commune name in English",
		example = "Bab El Oued",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Schema(
		description = "Commune name in French (required)",
		example = "Bab El Oued",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
	
	@Schema(
		description = "Parent district (daïra) to which this commune belongs",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "District is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_05", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_02_05_FK_01"), nullable=false)
	private District district;
}
