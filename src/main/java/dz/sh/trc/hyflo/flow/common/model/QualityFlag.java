/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: QualityFlag
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-22-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.model;

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
 * Quality indicators for flow measurement data.
 * Flags data quality issues such as sensor malfunctions, calibration needs, or suspicious readings.
 */
@Schema(description = "Data quality flag for flow measurement validation and quality assurance")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "QualityFlag")
@Table(name = "T_03_02_05", uniqueConstraints = {@UniqueConstraint(name="T_03_02_05_UK_01", columnNames={"F_01"}),
												 @UniqueConstraint(name="T_03_02_05_UK_02", columnNames={"F_04"})})
public class QualityFlag extends GenericModel {
    
	@Schema(
		description = "Unique code identifying the quality flag type",
		example = "SENSOR_ERROR",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 50
	)
	@NotBlank(message = "Quality flag code is mandatory")
	@Size(min = 2, max = 50, message = "Code must be between 2 and 50 characters")
	@Pattern(regexp = "^[A-Z0-9_-]+$", message = "Code must contain only uppercase letters, numbers, hyphens, and underscores")
	@Column(name = "F_01", length = 50, nullable = false)
	private String code;
    
	@Schema(
		description = "Quality flag designation in Arabic",
		example = "خطأ في الحساس",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name = "F_02", length = 100)
	private String designationAr;
    
	@Schema(
		description = "Quality flag designation in English",
		example = "Sensor Error",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name = "F_03", length = 100)
	private String designationEn;
    
	@Schema(
		description = "Quality flag designation in French (required)",
		example = "Erreur de capteur",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name = "F_04", length = 100, nullable = false)
	private String designationFr;
    
	@Schema(
		description = "Detailed description in Arabic",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 255
	)
	@Size(max = 255, message = "Arabic description must not exceed 255 characters")
	@Column(name = "F_05", length = 255)
	private String descriptionAr;
    
	@Schema(
		description = "Detailed description in English",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 255
	)
	@Size(max = 255, message = "English description must not exceed 255 characters")
	@Column(name = "F_06", length = 255)
	private String descriptionEn;
    
	@Schema(
		description = "Detailed description in French",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 255
	)
	@Size(max = 255, message = "French description must not exceed 255 characters")
	@Column(name = "F_07", length = 255)
	private String descriptionFr;
    
	@Schema(
		description = "Severity level of the quality issue",
		example = "HIGH",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 20,
		allowableValues = {"LOW", "MEDIUM", "HIGH", "CRITICAL"}
	)
	@Pattern(regexp = "^(LOW|MEDIUM|HIGH|CRITICAL)$", message = "Severity level must be LOW, MEDIUM, HIGH, or CRITICAL")
	@Size(max = 20, message = "Severity level must not exceed 20 characters")
	@Column(name = "F_08", length = 20)
	private String severityLevel;
}
