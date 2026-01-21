/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Alloy
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Common
 *
 **/


package dz.sh.trc.hyflo.network.common.model;

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
 * Represents alloys and materials used in pipeline construction and coating.
 * Supports multilingual designations (Arabic, English, French) for international operations.
 */
@Schema(description = "Alloy or material specification for pipeline construction, coating, and protection")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Alloy")
@Table(name="T_02_02_03", uniqueConstraints = {@UniqueConstraint(name="T_02_02_03_UK_01", columnNames = "F_01"),
											   @UniqueConstraint(name="T_02_02_03_UK_02", columnNames = "F_04")})
public class Alloy extends GenericModel {

	@Schema(
		description = "Unique code identifying the alloy or material",
		example = "API-5L-X65",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 20
	)
	@NotBlank(message = "Alloy code is mandatory")
	@Size(max = 20, message = "Alloy code must not exceed 20 characters")
	@Column(name="F_01", length=20, nullable=false)
	private String code;

	@Schema(
		description = "Designation of the alloy in Arabic",
		example = "فولاذ API 5L X65",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_02", length=100)
	private String designationAr;

	@Schema(
		description = "Designation of the alloy in English",
		example = "API 5L X65 Steel",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_03", length=100)
	private String designationEn;

	@Schema(
		description = "Designation of the alloy in French (required for SONATRACH operations)",
		example = "Acier API 5L X65",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
    
	@Schema(
		description = "Detailed description of the alloy in Arabic",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "Arabic description must not exceed 200 characters")
	@Column(name="F_05", length=200)
	private String descriptionAr;
    
	@Schema(
		description = "Detailed description of the alloy in English",
		example = "High-strength low-alloy steel for pipeline construction with excellent weldability",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "English description must not exceed 200 characters")
	@Column(name="F_06", length=200)
	private String descriptionEn;
    
	@Schema(
		description = "Detailed description of the alloy in French",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "French description must not exceed 200 characters")
	@Column(name="F_07", length=200)
	private String descriptionFr;
    
}
