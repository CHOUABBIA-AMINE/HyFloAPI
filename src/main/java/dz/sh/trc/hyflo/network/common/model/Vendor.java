/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: Vendor
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
import dz.sh.trc.hyflo.general.localization.model.Country;
import dz.sh.trc.hyflo.network.type.model.VendorType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
 * Represents vendors, suppliers, and contractors providing infrastructure,
 * equipment, and services to SONATRACH operations.
 */
@Schema(description = "Vendor, supplier, or contractor providing infrastructure, equipment, and services")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Vendor")
@Table(name="T_02_02_05", uniqueConstraints = {@UniqueConstraint(name="T_02_02_05_UK_01", columnNames={"F_02"})})
public class Vendor extends GenericModel {

	@Schema(
		description = "Full legal name of the vendor",
		example = "Saipem S.p.A.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Vendor name must not exceed 100 characters")
	@Column(name="F_01", length=100, nullable=true)
	private String name;

	@Schema(
		description = "Short name or abbreviation for the vendor (unique identifier)",
		example = "SAIPEM",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 20
	)
	@NotBlank(message = "Short name is mandatory")
	@Size(max = 20, message = "Short name must not exceed 20 characters")
	@Column(name="F_02", length=20, nullable=false)
	private String shortName;

	@Schema(
		description = "Type or category of vendor",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Vendor type is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_03", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_02_05_FK_01"), nullable = false)
	private VendorType vendorType;
	
	@Schema(
		description = "Country where the vendor is registered or headquartered",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Country is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_04", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_02_05_FK_02"), nullable = false)
	private Country country;
}
