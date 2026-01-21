/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StructureType
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: General / Type
 *
 **/

package dz.sh.trc.hyflo.general.type.model;

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
 * Represents organizational structure types within SONATRACH.
 * Examples: Directorate, Division, Department, Section, Unit, Service, Bureau.
 */
@Schema(description = "Type classification for organizational structures (directorate, division, department, section, etc.)")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="StructureType")
@Table(name="T_01_01_01", uniqueConstraints = {@UniqueConstraint(name = "T_01_01_01_UK_01", columnNames = { "F_03" })})
public class StructureType extends GenericModel {
	
	@Schema(
		description = "Structure type designation in Arabic",
		example = "مديرية",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_01", length=100)
	private String designationAr;

	@Schema(
		description = "Structure type designation in English",
		example = "Directorate",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_02", length=100)
	private String designationEn;
	
	@Schema(
		description = "Structure type designation in French (required for SONATRACH operations)",
		example = "Direction",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_03", length=100, nullable=false)
	private String designationFr;
	
}
