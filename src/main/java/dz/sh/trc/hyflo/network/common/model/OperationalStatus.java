/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: OperationalStatus
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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents operational status states for infrastructure and equipment.
 * Examples: Operational, Under Maintenance, Decommissioned, Under Construction.
 */
@Schema(description = "Operational status classification for infrastructure and equipment lifecycle management")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="OperationalStatus")
@Table(name="T_02_02_02", uniqueConstraints = { 
	@UniqueConstraint(name="T_02_02_02_UK_01", columnNames={"F_01"}),
	@UniqueConstraint(name="T_02_02_02_UK_02", columnNames={"F_04"})
})
public class OperationalStatus extends GenericModel {

	@Schema(
		description = "Unique code identifying the operational status",
		example = "OPS-ACTIVE",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 20
	)
	@NotBlank(message = "Status code is mandatory")
	@Size(max = 20, message = "Status code must not exceed 20 characters")
	@Column(name="F_01", length=20, nullable=false)
	private String code;

	@Schema(
		description = "Status designation in Arabic",
		example = "قيد التشغيل",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_02", length=100)
	private String designationAr;

	@Schema(
		description = "Status designation in English",
		example = "Operational",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_03", length=100)
	private String designationEn;

	@Schema(
		description = "Status designation in French (required for SONATRACH operations)",
		example = "En Service",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
    
	@Schema(
		description = "Detailed description of the status in Arabic",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "Arabic description must not exceed 200 characters")
	@Column(name="F_05", length=200)
	private String descriptionAr;
    
	@Schema(
		description = "Detailed description of the status in English",
		example = "Asset is fully operational and in active service",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "English description must not exceed 200 characters")
	@Column(name="F_06", length=200)
	private String descriptionEn;
    
	@Schema(
		description = "Detailed description of the status in French",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "French description must not exceed 200 characters")
	@Column(name="F_07", length=200)
	private String descriptionFr;
}
