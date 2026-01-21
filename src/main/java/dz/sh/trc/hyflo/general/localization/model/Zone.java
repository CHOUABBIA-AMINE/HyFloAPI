/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Zone
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
 * Represents geographic zones for SONATRACH operational regions.
 * Top-level administrative division above states (e.g., North, South, East, West).
 */
@Schema(description = "Geographic zone representing major operational regions within Algeria")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Zone")
@Table(name="T_01_02_01", uniqueConstraints = {@UniqueConstraint(name="T_01_02_01_UK_01", columnNames={"F_01"}),
											   @UniqueConstraint(name="T_01_02_01_UK_02", columnNames={"F_04"})})
public class Zone extends GenericModel {
    
	@Schema(
		description = "Unique code identifying the geographic zone",
		example = "ZONE-SUD",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 10
	)
	@NotBlank(message = "Zone code is mandatory")
	@Size(max = 10, message = "Zone code must not exceed 10 characters")
	@Column(name="F_01", length=10, nullable=false)
	private String code;

	@Schema(
		description = "Zone designation in Arabic",
		example = "الجنوب",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_02", length=100)
	private String designationAr;

	@Schema(
		description = "Zone designation in English",
		example = "South",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Schema(
		description = "Zone designation in French (required for SONATRACH operations)",
		example = "Sud",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
}
