/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: EquipmentType
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
 * Classification for equipment and machinery types installed at facilities.
 * Examples: Centrifugal Pump, Reciprocating Compressor, Control Valve, Flow Meter, 
 * Heat Exchanger, Storage Tank, Separator.
 */
@Schema(description = "Type classification for equipment and machinery (pumps, compressors, valves, meters, etc.)")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="EquipmentType")
@Table(name="T_02_01_09", uniqueConstraints = {@UniqueConstraint(name="T_02_01_09_UK_01", columnNames={"F_01"}),
											   @UniqueConstraint(name="T_02_01_09_UK_02", columnNames={"F_04"})})
public class EquipmentType extends GenericModel {

	@Schema(
		description = "Unique code identifying the equipment type",
		example = "PMP-CENT",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 20
	)
	@NotBlank(message = "Equipment type code is mandatory")
	@Size(max = 20, message = "Equipment type code must not exceed 20 characters")
	@Column(name="F_01", length=20, nullable=false)
	private String code;

	@Schema(
		description = "Equipment type designation in Arabic",
		example = "مضخة طرد مركزي",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_02", length=100, nullable=true)
	private String designationAr;

	@Schema(
		description = "Equipment type designation in English",
		example = "Centrifugal Pump",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_03", length=100, nullable=true)
	private String designationEn;

	@Schema(
		description = "Equipment type designation in French (required for SONATRACH operations)",
		example = "Pompe Centrifuge",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
    
}