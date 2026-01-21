/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: EventStatus
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
 * Event lifecycle status tracking.
 * Monitors operational events from reporting through investigation to closure.
 */
@Schema(description = "Event status for tracking operational event lifecycle from reporting to closure")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "EventStatus")
@Table(name = "T_03_02_01", uniqueConstraints = {@UniqueConstraint(name = "T_03_02_01_UK_01", columnNames = {"F_03"})})
public class EventStatus extends GenericModel {
      
	@Schema(
		description = "Event status designation in Arabic",
		example = "مفتوح",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name = "F_01", length = 100)
	private String designationAr;
    
	@Schema(
		description = "Event status designation in English",
		example = "Open",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name = "F_02", length = 100)
	private String designationEn;
    
	@Schema(
		description = "Event status designation in French (required)",
		example = "Ouvert",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name = "F_03", length = 100, nullable = false)
	private String designationFr;
}
