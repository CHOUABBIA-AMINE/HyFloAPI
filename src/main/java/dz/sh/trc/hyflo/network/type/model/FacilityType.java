/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FacilityType
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
 * Base classification for facility types in the hydrocarbon transportation network.
 * Extended by specialized types for stations, terminals, processing plants, and production fields.
 */
@Schema(description = "Base type classification for hydrocarbon transportation facilities")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="FacilityType")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_02_01_04", uniqueConstraints = {@UniqueConstraint(name="T_02_01_04_UK_01", columnNames={"F_01"}),
											   @UniqueConstraint(name="T_02_01_04_UK_02", columnNames={"F_04"})})
public class FacilityType extends GenericModel {

	@Schema(
		description = "Unique code identifying the facility type",
		example = "STN-PUMP",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 20
	)
	@NotBlank(message = "Facility type code is mandatory")
	@Size(max = 20, message = "Facility type code must not exceed 20 characters")
	@Column(name="F_01", length=20, nullable=false)
	private String code;

	@Schema(
		description = "Facility type designation in Arabic",
		example = "محطة ضخ",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_02", length=100, nullable=true)
	private String designationAr;

	@Schema(
		description = "Facility type designation in English",
		example = "Pumping Station",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_03", length=100, nullable=true)
	private String designationEn;

	@Schema(
		description = "Facility type designation in French (required for SONATRACH operations)",
		example = "Station de Pompage",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
    
}
