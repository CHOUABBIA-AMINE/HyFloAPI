/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Pipeline
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
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.network.common.model.Alloy;
import dz.sh.trc.hyflo.network.common.model.Vendor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents hydrocarbon transportation pipelines connecting terminals and facilities.
 * Contains technical specifications, material properties, and operational parameters.
 */
@Schema(description = "Hydrocarbon transportation pipeline with comprehensive technical and operational specifications")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Pipeline")
@Table(name="T_02_03_08")
@PrimaryKeyJoinColumn(name = "F_00", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_08_FK_00"))
public class Pipeline extends Infrastructure {  
    
	@Schema(
		description = "Nominal internal diameter of the pipeline (e.g., '48 inches', '1200 mm')",
		example = "48 inches",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 255
	)
	@NotBlank(message = "Nominal diameter is mandatory")
	@Size(max = 255, message = "Nominal diameter must not exceed 255 characters")
	@Column(name="F_08", nullable=false)
	private String nominalDiameter;

	@Schema(
		description = "Total length of the pipeline in kilometers",
		example = "850.5",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Pipeline length is mandatory")
	@Positive(message = "Pipeline length must be positive")
	@Column(name="F_09", nullable=false)
	private Double length;

	@Schema(
		description = "Nominal wall thickness of the pipeline (e.g., '12.7 mm', '0.5 inch')",
		example = "12.7 mm",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 255
	)
	@NotBlank(message = "Nominal thickness is mandatory")
	@Size(max = 255, message = "Nominal thickness must not exceed 255 characters")
	@Column(name="F_10", nullable=false)
	private String nominalThickness;

	@Schema(
		description = "Internal surface roughness of the pipeline material",
		example = "0.045 mm",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 255
	)
	@NotBlank(message = "Nominal roughness is mandatory")
	@Size(max = 255, message = "Nominal roughness must not exceed 255 characters")
	@Column(name="F_11", nullable=false)
	private String nominalRoughness;

	@Schema(
		description = "Maximum design service pressure in bar",
		example = "120.5",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Design maximum service pressure is mandatory")
	@Positive(message = "Design maximum service pressure must be positive")
	@Column(name="F_12", nullable=false)
	private Double designMaxServicePressure;

	@Schema(
		description = "Maximum operational service pressure in bar (typically lower than design pressure)",
		example = "100.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Operational maximum service pressure is mandatory")
	@Positive(message = "Operational maximum service pressure must be positive")
	@Column(name="F_13", nullable=false)
	private Double operationalMaxServicePressure;

	@Schema(
		description = "Minimum design service pressure in bar",
		example = "10.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Design minimum service pressure is mandatory")
	@Positive(message = "Design minimum service pressure must be positive")
	@Column(name="F_14", nullable=false)
	private Double designMinServicePressure;

	@Schema(
		description = "Minimum operational service pressure in bar",
		example = "15.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Operational minimum service pressure is mandatory")
	@Positive(message = "Operational minimum service pressure must be positive")
	@Column(name="F_15", nullable=false)
	private Double operationalMinServicePressure;

	@Schema(
		description = "Design capacity of the pipeline in cubic meters per day",
		example = "50000.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Design capacity is mandatory")
	@Positive(message = "Design capacity must be positive")
	@Column(name="F_16", nullable=false)
	private Double designCapacity;

	@Schema(
		description = "Operational capacity of the pipeline in cubic meters per day (typically lower than design capacity)",
		example = "45000.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Operational capacity is mandatory")
	@Positive(message = "Operational capacity must be positive")
	@Column(name="F_17", nullable=false)
	private Double operationalCapacity;

	@Schema(
		description = "Alloy or material used for pipeline construction",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@ManyToOne
	@JoinColumn(name="F_18", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_08_FK_01"), nullable=true)
	private Alloy nominalConstructionMaterial;

	@Schema(
		description = "Alloy or material used for exterior coating/protection",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@ManyToOne
	@JoinColumn(name="F_19", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_08_FK_02"), nullable=true)
	private Alloy nominalExteriorCoating;

	@Schema(
		description = "Alloy or material used for interior coating/lining",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@ManyToOne
	@JoinColumn(name="F_20", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_08_FK_03"), nullable=true)
	private Alloy nominalInteriorCoating;
	
	@Schema(
		description = "Pipeline system to which this pipeline belongs",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Pipeline system is mandatory")
	@ManyToOne
	@JoinColumn(name="F_21", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_08_FK_04"), nullable=false)
	private PipelineSystem pipelineSystem;

	@Schema(
		description = "Terminal where the pipeline originates",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Departure terminal is mandatory")
	@ManyToOne
	@JoinColumn(name="F_22", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_08_FK_05"), nullable=false)
	private Terminal departureTerminal;

	@Schema(
		description = "Terminal where the pipeline terminates",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Arrival terminal is mandatory")
	@ManyToOne
	@JoinColumn(name="F_23", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_08_FK_06"), nullable=false)
	private Terminal arrivalTerminal;
    
	@Schema(
		description = "Organizational structure responsible for managing this pipeline",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name="F_24", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_01_FK_07"), nullable = true)
	private Structure manager;

	@Schema(
		description = "Vendor who supplied or constructed the pipeline",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "R_T020308_T020205",
		joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020308_T020205_FK_01")),
		inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020308_T020205_FK_02")),
		uniqueConstraints = @UniqueConstraint(name = "R_T020308_T020205_UK_01", columnNames = {"F_01", "F_02"})
	)
	private Set<Vendor> vendors = new HashSet<>();
    
	@Schema(
		description = "Collection of geographic locations through which the pipeline passes",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "R_T020308_T010206",
		joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020308_T010206_FK_01")),
		inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020308_T010206_FK_02")),
		uniqueConstraints = @UniqueConstraint(name = "R_T020308_T010206_UK_01", columnNames = {"F_01", "F_02"})
	)
	private Set<Location> locations = new HashSet<>();
    
}
