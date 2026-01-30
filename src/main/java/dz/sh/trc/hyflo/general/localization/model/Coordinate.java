/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Coordinate
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-19-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents ordered waypoint coordinates for linear infrastructure (pipelines, segments).
 * Used to trace the precise route of pipelines through geographic space.
 */
@Schema(description = "Ordered waypoint coordinate for tracing linear infrastructure routes")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Coordinate")
@Table(name="T_01_02_07")
public class Coordinate extends GenericModel {

	@Schema(
		description = "Sequence number defining the order of this coordinate along the infrastructure route",
		example = "15",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Sequence number is mandatory")
	@Positive(message = "Sequence number must be positive")
	@Column(name="F_01", nullable=false)
	private int sequence;

	@Schema(
		description = "Latitude in decimal degrees (WGS84)",
		example = "31.7520",
		requiredMode = Schema.RequiredMode.REQUIRED,
		minimum = "-90",
		maximum = "90"
	)
	@NotNull(message = "Latitude is mandatory")
	@DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90 degrees")
	@DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90 degrees")
	@Column(name="F_02", nullable=false)
	private Double latitude;

	@Schema(
		description = "Longitude in decimal degrees (WGS84)",
		example = "6.2850",
		requiredMode = Schema.RequiredMode.REQUIRED,
		minimum = "-180",
		maximum = "180"
	)
	@NotNull(message = "Longitude is mandatory")
	@DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180 degrees")
	@DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180 degrees")
	@Column(name="F_03", nullable=false)
	private Double longitude;

	@Schema(
		description = "Elevation above sea level in meters at this coordinate",
		example = "145.2",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Column(name = "F_04")
	private Double elevation;
	
	@Schema(
		description = "Infrastructure asset to which this coordinate belongs",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Infrastructure is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_05", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_02_07_FK_01"), nullable=false)
	private Infrastructure infrastructure;

}
