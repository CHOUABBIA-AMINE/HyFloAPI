/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Severity
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
 * Severity level classification for events, alerts, and incidents.
 * Indicates the criticality and urgency of operational situations.
 */
@Schema(description = "Severity level classification for events, alerts, and operational incidents")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Severity")
@Table(name = "T_03_02_03", uniqueConstraints = {@UniqueConstraint(name = "T_03_02_03_UK_01", columnNames = {"F_03"})})
public class Severity extends GenericModel {
      
	@Schema(
		description = "Severity level designation in Arabic",
		example = "حرج",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name = "F_01", length = 100)
	private String designationAr;
    
	@Schema(
		description = "Severity level designation in English",
		example = "Critical",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name = "F_02", length = 100)
	private String designationEn;
    
	@Schema(
		description = "Severity level designation in French (required)",
		example = "Critique",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name = "F_03", length = 100, nullable = false)
	private String designationFr;
}
