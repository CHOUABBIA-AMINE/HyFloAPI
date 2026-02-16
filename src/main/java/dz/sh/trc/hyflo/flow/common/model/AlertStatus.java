/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AlertStatus
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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Alert lifecycle status tracking.
 * Monitors alert progression from creation through acknowledgment to resolution.
 */
@Schema(description = "Alert status for tracking alert lifecycle from creation to resolution")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "AlertStatus")
@Table(name = "T_03_02_02", uniqueConstraints = {@UniqueConstraint(name = "T_03_02_02_UK_01", columnNames = {"F_04"})})
public class AlertStatus extends GenericModel {
    
	@Schema(
		description = "Alert status code",
		example = "INFO",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 20
	)
	@Size(max = 20, message = "Code must not exceed 20 characters")
	@Column(name = "F_01", length = 20, nullable=false)
	private String code;
      
	@Schema(
		description = "Alert status designation in Arabic",
		example = "نشط",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name = "F_02", length = 100)
	private String designationAr;
    
	@Schema(
		description = "Alert status designation in English",
		example = "Active",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name = "F_03", length = 100)
	private String designationEn;
    
	@Schema(
		description = "Alert status designation in French (required)",
		example = "Actif",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name = "F_04", length = 100, nullable = false)
	private String designationFr;
}
