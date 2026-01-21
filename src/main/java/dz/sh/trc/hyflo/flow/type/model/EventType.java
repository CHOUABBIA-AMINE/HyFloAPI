/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: EventType
 * 	@CreatedOn	: 01-21-2026
 * 	@UpdatedOn	: 01-22-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.model;

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
 * Classification of operational events in hydrocarbon infrastructure.
 * Categories include maintenance, incidents, shutdowns, startups, and inspections.
 */
@Schema(description = "Event type classification for operational activities and incidents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(name = "EventType")
@Table(name = "T_03_01_02", uniqueConstraints = {@UniqueConstraint(name = "T_03_01_02_UK_01", columnNames = {"F_01"}),
												 @UniqueConstraint(name = "T_03_01_02_UK_02", columnNames = {"F_03"})})
public class EventType extends GenericModel {
    
	@Schema(
		description = "Unique code identifying the event type",
		example = "EMERGENCY_SHUTDOWN",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 20
	)
	@NotBlank(message = "Event type code is mandatory")
	@Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
	@Pattern(regexp = "^[A-Z0-9_-]+$", message = "Code must contain only uppercase letters, numbers, hyphens, and underscores")
	@Column(name = "F_01", length = 20, nullable = false)
	private String code;
    
	@Schema(
		description = "Event type designation in Arabic",
		example = "إيقاف طارئ",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name = "F_02", length = 100)
	private String designationAr;
    
	@Schema(
		description = "Event type designation in French (required)",
		example = "Arrêt d'Urgence",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name = "F_03", length = 100, nullable = false)
	private String designationFr;
    
	@Schema(
		description = "Event type designation in English",
		example = "Emergency Shutdown",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name = "F_04", length = 100)
	private String designationEn;
}
