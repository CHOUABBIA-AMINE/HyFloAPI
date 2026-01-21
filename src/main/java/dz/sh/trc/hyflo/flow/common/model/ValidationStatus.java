/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ValidationStatus
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
 * Validation status for flow data approval workflow.
 * Tracks the approval state of flow readings, operations, and forecasts.
 */
@Schema(description = "Validation status for flow data approval and quality control workflow")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ValidationStatus")
@Table(name = "T_03_02_06", uniqueConstraints = {@UniqueConstraint(name = "T_03_02_06_UK_01", columnNames = {"F_01"}),
												 @UniqueConstraint(name = "T_03_02_06_UK_02", columnNames = {"F_04"})})
public class ValidationStatus extends GenericModel {
    
	@Schema(
		description = "Unique code identifying the validation status",
		example = "PENDING",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 50
	)
	@NotBlank(message = "Validation status code is mandatory")
	@Size(min = 2, max = 50, message = "Code must be between 2 and 50 characters")
	@Pattern(regexp = "^[A-Z0-9_-]+$", message = "Code must contain only uppercase letters, numbers, hyphens, and underscores")
	@Column(name = "F_01", length = 50, nullable = false)
	private String code;
    
	@Schema(
		description = "Validation status designation in Arabic",
		example = "قيد الانتظار",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name = "F_02", length = 100)
	private String designationAr;
    
	@Schema(
		description = "Validation status designation in English",
		example = "Pending Validation",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name = "F_03", length = 100)
	private String designationEn;
    
	@Schema(
		description = "Validation status designation in French (required)",
		example = "En Attente de Validation",
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
}
