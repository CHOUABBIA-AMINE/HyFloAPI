/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Facility
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import java.util.HashSet;
import java.util.Set;

import dz.sh.trc.hyflo.general.localization.model.Location;
import dz.sh.trc.hyflo.network.common.model.Vendor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents fixed-location infrastructure facilities including stations,
 * terminals, processing plants, and production fields.
 */
@Schema(description = "Fixed-location infrastructure facilities with specific geographic positioning and associated equipment")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Facility")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_02_03_02")
@PrimaryKeyJoinColumn(name = "F_00", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_02_FK_00"))
public class Facility extends Infrastructure {

	@Schema(
		description = "Vendor or contractor who supplied or constructed the facility",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Vendor is mandatory for facilities")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_08", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_02_FK_01"), nullable = false)
	protected Vendor vendor;

	@Schema(
		description = "Geographic location of the facility including coordinates and administrative details",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Location is mandatory for facilities")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_09", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_02_FK_02"), nullable = false)
	private Location location;
    
	@Schema(
		description = "Collection of equipment installed at this facility",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Builder.Default
	@OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
	protected Set<Equipment> equipments = new HashSet<>();

}
