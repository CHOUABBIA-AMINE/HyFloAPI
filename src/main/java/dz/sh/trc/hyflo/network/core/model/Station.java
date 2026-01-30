/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Station
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

import dz.sh.trc.hyflo.network.type.model.StationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents pumping and compression stations along pipeline routes.
 * Stations maintain pressure and flow rates for hydrocarbon transportation.
 */
@Schema(description = "Pumping or compression station for maintaining pipeline pressure and flow")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Station")
@Table(name="T_02_03_03")
@PrimaryKeyJoinColumn(name = "F_00", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_03_FK_00"))
public class Station extends Facility {

	@Schema(
		description = "Type of station (e.g., pumping station, compression station, metering station)",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Station type is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_10", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_03_FK_01"), nullable=false)
	private StationType stationType;

	@Schema(
		description = "Pipeline system this station belongs to, if applicable",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_11", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_03_FK_02"), nullable=true)
	private PipelineSystem pipelineSystem;

	@Schema(
		description = "Collection of pipelines served by this station",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "R_T020303_T020308",
		joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020303_T020308_FK_01")),
		inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020303_T020308_FK_02")),
		uniqueConstraints = @UniqueConstraint(name = "R_T020303_T020308_UK_01", columnNames = {"F_01", "F_02"})
	)
	private Set<Pipeline> pipelines = new HashSet<>();
    
}
