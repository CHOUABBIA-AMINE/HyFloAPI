/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: Product
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents hydrocarbon products transported through the pipeline network.
 * Contains physical and chemical properties essential for safe transportation.
 */
@Schema(description = "Hydrocarbon product with physical and chemical properties for transportation")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Product")
@Table(name="T_02_02_01", uniqueConstraints = {@UniqueConstraint(name="T_02_02_01_UK_01", columnNames={"F_01"}),
											   @UniqueConstraint(name="T_02_02_01_UK_02", columnNames={"F_04"})})
public class Product extends GenericModel {

	@Schema(
		description = "Unique code identifying the hydrocarbon product",
		example = "CRD-001",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 20
	)
	@NotBlank(message = "Product code is mandatory")
	@Size(max = 20, message = "Product code must not exceed 20 characters")
	@Column(name="F_01", length=20, nullable=false)
	private String code;

	@Schema(
		description = "Product designation in Arabic",
		example = "النفط الخام",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_02", length=100, nullable=true)
	private String designationAr;

	@Schema(
		description = "Product designation in English",
		example = "Crude Oil",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_03", length=100, nullable=true)
	private String designationEn;

	@Schema(
		description = "Product designation in French (required for SONATRACH operations)",
		example = "Pétrole Brut",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;

	@Schema(
		description = "Density of the product in kg/m³ at standard conditions",
		example = "850.5",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Density is mandatory")
	@PositiveOrZero(message = "Density must be zero or positive")
	@Column(name="F_05", nullable=false)
	private Double density;

	@Schema(
		description = "Dynamic viscosity of the product in cP (centipoise) at standard temperature",
		example = "10.5",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Viscosity is mandatory")
	@PositiveOrZero(message = "Viscosity must be zero or positive")
	@Column(name="F_06", nullable=false)
	private Double viscosity;

	@Schema(
		description = "Flash point temperature of the product in degrees Celsius",
		example = "45.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Flash point is mandatory")
	@Column(name="F_07", nullable=false)
	private Double flashPoint;

	@Schema(
		description = "Sulfur content percentage by weight",
		example = "0.5",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Sulfur content is mandatory")
	@PositiveOrZero(message = "Sulfur content must be zero or positive")
	@Column(name="F_08", nullable=false)
	private Double sulfurContent;

	@Schema(
		description = "Indicates whether the product is classified as hazardous material",
		example = "true",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Hazardous classification is mandatory")
	@Column(name="F_09", nullable=false)
	private Boolean isHazardous;
}
