/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Location
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents specific geographic locations with coordinates.
 * Used for positioning facilities, pipelines, and infrastructure assets.
 */
@Schema(description = "Geographic location with precise coordinates for infrastructure positioning")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Location")
@Table(name="T_01_02_06")
public class Location extends GenericModel {

	@Schema(
		description = "Location designation in Arabic",
		example = "حاسي مسعود",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_01", length=100, nullable=true)
	private String designationAr;

	@Schema(
		description = "Location designation in English",
		example = "Hassi Messaoud",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_02", length=100, nullable=true)
	private String designationEn;

	@Schema(
		description = "Location designation in French (required)",
		example = "Hassi Messaoud",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_03", length=100, nullable=false)
	private String designationFr;

	@Schema(
		description = "Latitude in decimal degrees (WGS84)",
		example = "31.6806",
		requiredMode = Schema.RequiredMode.REQUIRED,
		minimum = "-90",
		maximum = "90"
	)
	@NotNull(message = "Latitude is mandatory")
	@DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90 degrees")
	@DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90 degrees")
	@Column(name="F_04", nullable=false)
	private Double latitude;

	@Schema(
		description = "Longitude in decimal degrees (WGS84)",
		example = "6.1416",
		requiredMode = Schema.RequiredMode.REQUIRED,
		minimum = "-180",
		maximum = "180"
	)
	@NotNull(message = "Longitude is mandatory")
	@DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180 degrees")
	@DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180 degrees")
	@Column(name="F_05", nullable=false)
	private Double longitude;

	@Schema(
		description = "Elevation above sea level in meters",
		example = "138.5",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Column(name = "F_06")
	private Double elevation;

	@Schema(
		description = "Administrative locality (village, town, city) where this location resides",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_07", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_02_06_FK_01"), nullable = true)
	private Locality locality;

}
